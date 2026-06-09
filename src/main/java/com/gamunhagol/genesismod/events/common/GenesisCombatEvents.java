package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.item.weapon.CatalystItem;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisCombatEvents {
    private static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity sourceEntity = event.getSource().getDirectEntity();
        Entity attackerEntity = event.getSource().getEntity();

        DamageSnapshot snapshot = null;

        if (sourceEntity != null && sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).isPresent()) {
            var cap = sourceEntity.getCapability(ProjectileStatsProvider.CAPABILITY).orElse(null);
            if (cap != null && !cap.getSnapshot().isEmpty()) {
                snapshot = cap.getSnapshot();
            }
        }
        else if (attackerEntity instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (WeaponDataManager.hasData(weapon.getItem())) {
                if (weapon.getItem() instanceof BowItem || weapon.getItem() instanceof CrossbowItem) {
                    return;
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
                    snapshot = new DamageSnapshot(
                            rawSnapshot.physical(),
                            0, 0, 0, 0, 0,
                            rawSnapshot.destruction()
                    );
                } else {
                    snapshot = rawSnapshot;
                }
            }
        }

        if (snapshot != null && !snapshot.isEmpty()) {
            if (snapshot.physical() > 0) {
                event.setAmount(snapshot.physical());
            }
            applyElementalMarkers(target, snapshot);
        }
        else if (attackerEntity instanceof Player player && snapshot == null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            });
        }
    }

    private static void applyElementalMarkers(LivingEntity target, DamageSnapshot snapshot) {
        if (snapshot.magic() > 0) target.getPersistentData().putFloat("GenesisExtraMagic", snapshot.magic());
        if (snapshot.fire() > 0) target.getPersistentData().putFloat("GenesisExtraFire", snapshot.fire());
        if (snapshot.lightning() > 0) target.getPersistentData().putFloat("GenesisExtraLightning", snapshot.lightning());
        if (snapshot.frost() > 0) target.getPersistentData().putFloat("GenesisExtraFrost", snapshot.frost());
        if (snapshot.holy() > 0) target.getPersistentData().putFloat("GenesisExtraHoly", snapshot.holy());
        if (snapshot.destruction() > 0) target.getPersistentData().putFloat("GenesisExtraDestruction", snapshot.destruction());
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;
        LivingEntity target = event.getEntity();
        float finalDamage = event.getAmount();

        if (target.getPersistentData().contains("GenesisExtraMagic")) {
            float magicDmg = target.getPersistentData().getFloat("GenesisExtraMagic");
            finalDamage += calculateMagicDamage(target, magicDmg);
            target.getPersistentData().remove("GenesisExtraMagic");
        }

        if (target.getPersistentData().contains("GenesisExtraFire")) {
            float fireDmg = target.getPersistentData().getFloat("GenesisExtraFire");
            fireDmg = calculateFireDamage(target, fireDmg); // 저항 계산
            finalDamage += fireDmg;
            if (fireDmg > 0) target.setSecondsOnFire(3);
            target.getPersistentData().remove("GenesisExtraFire");
        }

        if (target.getPersistentData().contains("GenesisExtraLightning")) {
            float lightDmg = target.getPersistentData().getFloat("GenesisExtraLightning");
            if (target.isInWaterOrRain()) lightDmg *= 1.5f;
            finalDamage += lightDmg;
            target.getPersistentData().remove("GenesisExtraLightning");
        }

        if (target.getPersistentData().contains("GenesisExtraFrost")) {
            float frostDmg = target.getPersistentData().getFloat("GenesisExtraFrost");
            if (target.getType().is(net.minecraft.tags.EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                frostDmg *= 0.5f;
            }
            finalDamage += frostDmg;
            target.setTicksFrozen(target.getTicksFrozen() + 150);
            target.getPersistentData().remove("GenesisExtraFrost");
        }

        if (target.getPersistentData().contains("GenesisExtraHoly")) {
            float holyDmg = target.getPersistentData().getFloat("GenesisExtraHoly");
            finalDamage += calculateHolyDamage(target, holyDmg);
            target.getPersistentData().remove("GenesisExtraHoly");
        }

        if (target.getPersistentData().contains("GenesisExtraDestruction")) {
            float destDmg = target.getPersistentData().getFloat("GenesisExtraDestruction");
            applyDestructionEffect(target, destDmg);
            finalDamage += destDmg;
            target.getPersistentData().remove("GenesisExtraDestruction");
        }

        if (event.getSource().is(GenesisDamageTypes.DESTRUCTION)) {
            applyDestructionEffect(target, event.getAmount());
        }

        event.setAmount(finalDamage);
    }

    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        removeDestructionEffect(event.getEntity());
    }

    //Helper Methods

    private static float calculateHolyDamage(LivingEntity target, float damage) {
        float result = damage;


        AttributeInstance holyDef = target.getAttribute(GenesisAttributes.HOLY_DEFENSE.get());
        if (holyDef != null && holyDef.getValue() > 0) {
            float reductionMultiplier = (float) (1.0 - (holyDef.getValue() / (holyDef.getValue() + 30.0)));
            result *= reductionMultiplier;
        }
        return (result < 0.5f && damage > 0) ? 0.5f : result;
    }

    private static float calculateMagicDamage(LivingEntity target, float damage) {
        float result = damage;
        if (target.isInvertedHealAndHarm()) return 0;

        if (target.getType() == net.minecraft.world.entity.EntityType.WITCH) {
            result *= 0.15f;
        }

        AttributeInstance magicDef = target.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
        if (magicDef != null && magicDef.getValue() > 0) {
            float multiplier = (float) (1.0 - (magicDef.getValue() / (magicDef.getValue() + 30.0)));
            result *= multiplier;
        }
        return Math.max(result, (damage > 0 ? 0.5f : 0));
    }

    private static float calculateFireDamage(LivingEntity target, float damage) {
        if (target.hasEffect(MobEffects.FIRE_RESISTANCE)) return 0;

        int prot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, target);
        if (prot > 0) {
            float reduction = 1.0f - (Math.min(prot, 10) * 0.08f);
            return damage * reduction;
        }
        return damage;
    }

    private static void applyDestructionEffect(LivingEntity entity, float damageAmount) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        double currentModifierValue = 0;
        AttributeModifier existingMod = maxHealthAttr.getModifier(DESTRUCTION_HP_MOD_UUID);
        if (existingMod != null) {
            currentModifierValue = existingMod.getAmount();
            maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        }

        double newReductionValue = currentModifierValue - damageAmount;
        if (maxHealthAttr.getBaseValue() + newReductionValue < 1.0D) newReductionValue = -maxHealthAttr.getBaseValue() + 1.0D;

        maxHealthAttr.addPermanentModifier(new AttributeModifier(DESTRUCTION_HP_MOD_UUID, "Destruction Max HP Reduction", newReductionValue, AttributeModifier.Operation.ADDITION));
        if (entity instanceof ServerPlayer sp) sp.getHealth();
        entity.getPersistentData().putLong("GenesisDestructionEndTick", entity.level().getGameTime() + 144000L);
    }

    public static void removeDestructionEffect(LivingEntity entity) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        entity.getPersistentData().remove("GenesisDestructionEndTick");
    }
}