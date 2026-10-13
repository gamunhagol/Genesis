package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class AreaGreatHealMiracle extends MiracleSpell {

    public AreaGreatHealMiracle() {
        super("area_great_heal");
    }

    @Override
    public int getMaxChargeTicks() {
        return 80;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return true;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 25);
    }

    @Override
    public float getMentalCost() {
        return 14.9f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float healAmount = 14.0f + catalyst.holy();
        float efficiency = 0.6f;

        float baseHeal = healAmount + (catalyst.holy() * efficiency);
        float multiplier = getDedicationMultiplier(caster);
        float finalHeal = baseHeal * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        applyAreaInstantHeal(level, caster, spellSnapshot.holy());
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float multiplier = (ratio > 0.70f) ? 1.25f : 1.0f;

        applyAreaInstantHeal(level, caster, spellSnapshot.holy() * multiplier);
    }

    private void applyAreaInstantHeal(Level level, LivingEntity caster, float finalHealAmount) {
        if (level.isClientSide) return;

        double radius = 6.0D;
        AABB area = caster.getBoundingBox().inflate(radius);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, Entity::isAlive);

        int amplifier = Math.max(0, Math.round(finalHealAmount / 4.0f) - 1);
        for (LivingEntity target : targets) {
            target.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, amplifier, false, false, true));
        }
    }
}
