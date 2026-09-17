package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractMagicDomainEntity;
import com.gamunhagol.genesismod.world.item.weapon.CatalystItem;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisCombatEvents {

    private static DamageSnapshot getSnapshot(Entity attackerEntity, Entity sourceEntity, LivingEntity target) {
        if (target.getPersistentData().contains("GenesisSkipSnapshot")) return null;

        DamageSnapshot snapshot = null;

        if (sourceEntity != null && sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).isPresent()) {
            var cap = sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).orElse(null);
            if (cap != null && !cap.getSnapshot().isEmpty()) {
                snapshot = cap.getSnapshot();
            }
        } else if (attackerEntity instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (WeaponDataManager.hasData(weapon.getItem())) {
                if (weapon.getItem() instanceof BowItem || weapon.getItem() instanceof CrossbowItem) {
                    return null;
                }

                float enchantBonus = EnchantmentHelper.getDamageBonus(weapon, target.getMobType());

                if (weapon.getItem() instanceof TridentItem) {
                    int impalingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.IMPALING, weapon);
                    if (impalingLevel > 0 && target.getMobType() == MobType.WATER) {
                        enchantBonus += (impalingLevel * 2.5f);
                    }
                }

                DamageSnapshot rawSnapshot = WeaponRequirementHelper.calculateTotalDamage(player, weapon, enchantBonus);

                if (weapon.getItem() instanceof CatalystItem) {
                    snapshot = new DamageSnapshot(rawSnapshot.physical(), 0, 0, 0, 0, 0, rawSnapshot.destruction());
                } else {
                    snapshot = rawSnapshot;
                }
            }
        }

        return snapshot;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypes.SONIC_BOOM)) return;

        LivingEntity target = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();
        Entity sourceEntity = event.getSource().getDirectEntity();

        DamageSnapshot snapshot = getSnapshot(attackerEntity, sourceEntity, target);

        if (snapshot != null && !snapshot.isEmpty()) {
            if (snapshot.physical() > 0) {
                event.setAmount(snapshot.physical());
            }
        }
        else if (attackerEntity instanceof Player player && !event.getSource().isIndirect()) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                float strengthScale = StatApplier.calculateScaling(stats.getStrength());
                event.setAmount(event.getAmount() * (1.0f + strengthScale));
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurtArmor(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        double totalArmor = target.getAttributeValue(Attributes.ARMOR);

        if (totalArmor > 20.0) {
            double alpha = totalArmor - 20.0;
            float extraReductionMult = (float) (1.0 - (alpha / (alpha + 100.0)));
            extraReductionMult = Math.max(0.25f, extraReductionMult);
            event.setAmount(event.getAmount() * extraReductionMult);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().is(DamageTypes.SONIC_BOOM)) return;
        if (event.getAmount() <= 0) return;

        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getDirectEntity();
        Entity attackerEntity = event.getSource().getEntity();

        float finalDamage = event.getAmount();

        DamageSnapshot snapshot = getSnapshot(attackerEntity, sourceEntity, target);

        if (snapshot != null && !snapshot.isEmpty() && attackerEntity instanceof LivingEntity caster) {

            float magicMultiplier = AbstractMagicDomainEntity.getMultiplierAt(caster);
            if (caster.getPersistentData().contains("GenesisStarSeaBuffEndTick")) {
                long endTick = caster.getPersistentData().getLong("GenesisStarSeaBuffEndTick");
                if (target.level().getGameTime() <= endTick) {
                    magicMultiplier *= 1.5F;
                } else {
                    caster.getPersistentData().remove("GenesisStarSeaBuffEndTick");
                }
            }

            if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO) && magicMultiplier > 1.0F) {
                finalDamage *= magicMultiplier;
            }

            float summonMultiplier = 1.0f;
            if (caster instanceof Mob mob && mob instanceof ISummonable) {
                if (mob.getPersistentData().contains("GenesisSummonDamageMultiplier")) {
                    summonMultiplier += (float) mob.getPersistentData().getDouble("GenesisSummonDamageMultiplier");
                }
            }

            float elementalDmg = GenesisDamageCalculator.calculateElementalDamageAndApplyEffects(target, caster, snapshot, magicMultiplier, summonMultiplier);
            finalDamage += elementalDmg;

        } else {
            if (event.getSource().is(GenesisDamageTypes.DESTRUCTION)) {
                GenesisDamageCalculator.applyDestructionEffect(target, event.getAmount());
            }
        }

        event.setAmount(finalDamage);
    }

    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        GenesisDamageCalculator.removeDestructionEffect(event.getEntity());
    }
}