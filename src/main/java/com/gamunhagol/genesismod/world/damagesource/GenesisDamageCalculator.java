package com.gamunhagol.genesismod.world.damagesource;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractMagicDomainEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.UUID;

public class GenesisDamageCalculator {
    public static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");

    public static float calculateElementalDamageAndApplyEffects(LivingEntity target, LivingEntity caster, DamageSnapshot snapshot, float magicMultiplier, float summonMultiplier) {
        float totalElemental = 0.0f;

        int arcaneLevel = WeaponRequirementHelper.getEntityStat(caster, StatType.ARCANE);
        float procChance = 0.10f + (arcaneLevel * 0.005f);

        if (snapshot.magic() > 0) {
            totalElemental += calculateMagicDamage(target, snapshot.magic() * magicMultiplier * summonMultiplier);
        }
        if (snapshot.fire() > 0) {
            float fireDmg = calculateFireDamage(target, snapshot.fire() * summonMultiplier);
            totalElemental += fireDmg;
            if (fireDmg > 0 && target.level().random.nextFloat() < procChance) {
                target.setSecondsOnFire(3);
            }
        }
        if (snapshot.lightning() > 0) {
            float lightDmg = calculateLightningDamage(target, snapshot.lightning() * summonMultiplier);
            totalElemental += lightDmg;
            if (lightDmg > 0 && target.level().random.nextFloat() < procChance) {
                target.addEffect(new MobEffectInstance(GenesisEffects.ELECTRIC_SHOCK.get(), 360, 0));
            }
        }
        if (snapshot.frost() > 0) {
            float frostDmg = calculateFrostDamage(target, snapshot.frost() * summonMultiplier);
            totalElemental += frostDmg;
            if (frostDmg > 0 && target.level().random.nextFloat() < procChance) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
            }
        }
        if (snapshot.holy() > 0) {
            totalElemental += calculateHolyDamage(target, snapshot.holy() * summonMultiplier);
        }
        if (snapshot.destruction() > 0) {
            float destDmg = snapshot.destruction() * summonMultiplier;
            applyDestructionEffect(target, destDmg);
            totalElemental += destDmg;
        }

        return totalElemental;
    }

    public static void applySnapshotDamage(LivingEntity target, LivingEntity caster, DamageSnapshot snapshot) {
        if (target == null || caster == null || snapshot == null || snapshot.isEmpty()) return;

        target.getPersistentData().putBoolean("GenesisSkipSnapshot", true);

        float magicMultiplier = AbstractMagicDomainEntity.getMultiplierAt(caster);
        if (caster.getPersistentData().contains("GenesisStarSeaBuffEndTick")) {
            if (target.level().getGameTime() <= caster.getPersistentData().getLong("GenesisStarSeaBuffEndTick")) {
                magicMultiplier *= 1.5F;
            }
        }

        float summonMultiplier = 1.0f;
        if (caster instanceof Mob mob && mob instanceof ISummonable) {
            if (mob.getPersistentData().contains("GenesisSummonDamageMultiplier")) {
                summonMultiplier += (float) mob.getPersistentData().getDouble("GenesisSummonDamageMultiplier");
            }
        }

        float elementalTotal = calculateElementalDamageAndApplyEffects(target, caster, snapshot, magicMultiplier, summonMultiplier);
        float physicalTotal = snapshot.physical() * summonMultiplier;
        float grandTotal = physicalTotal + elementalTotal;

        if (grandTotal > 0) {
            target.hurt(caster.damageSources().indirectMagic(caster, caster), grandTotal);
        }

        target.getPersistentData().remove("GenesisSkipSnapshot");
    }

    private static float calculateMagicDamage(LivingEntity target, float damage) {
        float result = damage;
        if (target.isInvertedHealAndHarm()) return 0;
        if (target.getType() == net.minecraft.world.entity.EntityType.WITCH) result *= 0.15f;

        int prot = EnchantmentHelper.getEnchantmentLevel(com.gamunhagol.genesismod.world.enchantment.GenesisEnchantments.MAGIC_PROTECTION.get(), target);
        if (prot > 0) result *= 1.0f - (Math.min(prot, 10) * 0.08f);

        AttributeInstance magicDef = target.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
        if (magicDef != null && magicDef.getValue() > 0) {
            result *= (float) (1.0 - (magicDef.getValue() / (magicDef.getValue() + 30.0)));
        }
        return Math.max(result, (damage > 0 ? 0.5f : 0));
    }

    private static float calculateFireDamage(LivingEntity target, float damage) {
        if (target.hasEffect(MobEffects.FIRE_RESISTANCE)) return 0;
        float result = damage;
        int prot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, target);
        if (prot > 0) result *= 1.0f - (Math.min(prot, 10) * 0.08f);

        AttributeInstance fireDef = target.getAttribute(GenesisAttributes.FIRE_DEFENSE.get());
        if (fireDef != null && fireDef.getValue() > 0) {
            result *= (float) (1.0 - (fireDef.getValue() / (fireDef.getValue() + 30.0)));
        }
        return Math.max(result, (damage > 0 ? 0.5f : 0));
    }

    private static float calculateFrostDamage(LivingEntity target, float damage) {
        if (target.hasEffect(GenesisEffects.COLD_RESISTANCE.get())) return 0;
        float result = damage;
        if (target.getType().is(net.minecraft.tags.EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) result *= 0.5f;

        int prot = EnchantmentHelper.getEnchantmentLevel(com.gamunhagol.genesismod.world.enchantment.GenesisEnchantments.FROSTBITE_PROTECTION.get(), target);
        if (prot > 0) result *= 1.0f - (Math.min(prot, 10) * 0.08f);

        AttributeInstance frostDef = target.getAttribute(GenesisAttributes.FROST_DEFENSE.get());
        if (frostDef != null && frostDef.getValue() > 0) {
            result *= (float) (1.0 - (frostDef.getValue() / (frostDef.getValue() + 30.0)));
        }
        return Math.max(result, (damage > 0 ? 0.5f : 0));
    }

    private static float calculateLightningDamage(LivingEntity target, float damage) {
        if (target.hasEffect(GenesisEffects.LIGHTNING_RESISTANCE.get())) return 0;
        float result = damage;
        if (target.isInWaterOrRain()) result *= 1.5f;

        int prot = EnchantmentHelper.getEnchantmentLevel(com.gamunhagol.genesismod.world.enchantment.GenesisEnchantments.ELECTRIC_PROTECTION.get(), target);
        if (prot > 0) result *= 1.0f - (Math.min(prot, 10) * 0.08f);

        AttributeInstance lightDef = target.getAttribute(GenesisAttributes.LIGHTNING_DEFENSE.get());
        if (lightDef != null && lightDef.getValue() > 0) {
            result *= (float) (1.0 - (lightDef.getValue() / (lightDef.getValue() + 30.0)));
        }
        return Math.max(result, (damage > 0 ? 0.5f : 0));
    }

    private static float calculateHolyDamage(LivingEntity target, float damage) {
        float result = damage;
        int prot = EnchantmentHelper.getEnchantmentLevel(com.gamunhagol.genesismod.world.enchantment.GenesisEnchantments.HOLY_PROTECTION.get(), target);
        if (prot > 0) result *= 1.0f - (Math.min(prot, 10) * 0.08f);

        AttributeInstance holyDef = target.getAttribute(GenesisAttributes.HOLY_DEFENSE.get());
        if (holyDef != null && holyDef.getValue() > 0) {
            result *= (float) (1.0 - (holyDef.getValue() / (holyDef.getValue() + 30.0)));
        }
        return (result < 0.5f && damage > 0) ? 0.5f : result;
    }

    public static void applyDestructionEffect(LivingEntity entity, float damageAmount) {
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