package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class GreatHealMiracle extends MiracleSpell {

    public GreatHealMiracle() {
        super("great_heal");
    }

    @Override
    public int getMaxChargeTicks() {
        return 50;
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
        return Map.of(StatType.FAITH, 18);
    }

    @Override
    public float getMentalCost() {
        return 8.4f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
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
        if (level.isClientSide) return;
        applyEffect(level, caster, spellSnapshot.holy());
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        if (level.isClientSide) return;

        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float multiplier = (ratio > 0.70f) ? 1.25f : 1.0f;

        applyEffect(level, caster, spellSnapshot.holy() * multiplier);
    }

    private void applyEffect(Level level, LivingEntity caster, float healAmount) {
        if (caster.isInvertedHealAndHarm()) {
            caster.hurt(GenesisDamageTypes.getSource(level, GenesisDamageTypes.HOLY, caster), healAmount);
        } else {
            caster.heal(healAmount);
        }
    }
}
