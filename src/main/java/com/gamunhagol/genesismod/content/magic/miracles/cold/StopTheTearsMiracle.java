package com.gamunhagol.genesismod.content.magic.miracles.cold;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class StopTheTearsMiracle extends MiracleSpell {

    public StopTheTearsMiracle() {
        super("stop_the_tears");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 26);
    }

    @Override
    public float getMentalCost() {
        return 6.2f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int faith = WeaponRequirementHelper.getEntityStat(caster, StatType.FAITH);
        float faithBonusMultiplier = 1.0f + (faith * 0.01f);
        float dedicationMultiplier = getDedicationMultiplier(caster);
        float totalMultiplier = faithBonusMultiplier * dedicationMultiplier;

        int resistanceDuration = Math.round(1800 * totalMultiplier);
        int strengthDuration = Math.round(1800 * totalMultiplier);
        int regenDuration = Math.round(600 * totalMultiplier);
        int slownessDuration = Math.round(1200 * totalMultiplier);

        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, resistanceDuration, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, strengthDuration, 2));
        caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenDuration, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slownessDuration, 0));
    }
}