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

public class SpellDamageCalculator {
    public static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");

    public static void applyDamage(LivingEntity target, LivingEntity caster, DamageSnapshot snapshot) {
        if (target == null || caster == null || snapshot == null || snapshot.isEmpty()) return;

        int arcaneLevel = WeaponRequirementHelper.getEntityStat(caster, StatType.ARCANE);
        float procChance = 0.10f + (arcaneLevel * 0.005f);

        float magicMultiplier = AbstractMagicDomainEntity.getMultiplierAt(caster);

        float summonMultiplier = 1.0f;
        if (caster instanceof Mob mob && mob instanceof ISummonable) {
            if (mob.getPersistentData().contains("GenesisSummonDamageMultiplier")) {
                summonMultiplier += (float) mob.getPersistentData().getDouble("GenesisSummonDamageMultiplier");
            }
        }

        if (snapshot.magic() > 0) {
            float magicDmg = calculateMagicDamage(target, snapshot.magic() * magicMultiplier * summonMultiplier);
            if (magicDmg > 0) target.hurt(caster.damageSources().indirectMagic(caster, caster), magicDmg);
        }

        if (snapshot.fire() > 0) {
            float fireDmg = calculateFireDamage(target, snapshot.fire() * summonMultiplier);
            if (fireDmg > 0) {
                target.hurt(caster.damageSources().inFire(), fireDmg);
                if (target.level().random.nextFloat() < procChance) {
                    target.setSecondsOnFire(3);
                }
            }
        }

        if (snapshot.lightning() > 0) {
            float lightDmg = calculateLightningDamage(target, snapshot.lightning() * summonMultiplier);
            if (lightDmg > 0) {
                target.hurt(caster.damageSources().lightningBolt(), lightDmg);
                if (target.level().random.nextFloat() < procChance) {
                    target.addEffect(new MobEffectInstance(GenesisEffects.ELECTRIC_SHOCK.get(), 360, 0));
                }
            }
        }

        if (snapshot.frost() > 0) {
            float frostDmg = calculateFrostDamage(target, snapshot.frost() * summonMultiplier);
            if (frostDmg > 0) {
                target.hurt(caster.damageSources().freeze(), frostDmg);
                if (target.level().random.nextFloat() < procChance) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                }
            }
        }

        if (snapshot.holy() > 0) {
            float holyDmg = calculateHolyDamage(target, snapshot.holy() * summonMultiplier);
            if (holyDmg > 0) target.hurt(GenesisDamageTypes.getSource(target.level(), GenesisDamageTypes.HOLY, caster), holyDmg);
        }

        if (snapshot.physical() > 0) {
            target.hurt(caster.damageSources().mobAttack(caster), snapshot.physical() * summonMultiplier);
        }

        if (snapshot.destruction() > 0) {
            float destDmg = snapshot.destruction() * summonMultiplier;
            applyDestructionEffect(target, destDmg);
            target.hurt(GenesisDamageTypes.getSource(target.level(), GenesisDamageTypes.DESTRUCTION, caster), destDmg);
        }
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
}