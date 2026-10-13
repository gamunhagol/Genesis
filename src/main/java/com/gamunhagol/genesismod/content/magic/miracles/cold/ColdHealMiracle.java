package com.gamunhagol.genesismod.content.magic.miracles.cold;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ColdHealMiracle extends MiracleSpell {

    public ColdHealMiracle() {
        super("cold_heal");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 14);
    }

    @Override
    public float getMentalCost() {
        return 5.6f;
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
        float baseHeal = 8.0f;
        float holyScaling = catalyst.holy() * 0.25f;

        int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                .map(StatCapability::getFaith)
                .orElse(10);

        float faithBonusMultiplier = 1.0f + (faith * 0.015f);
        float dedicationMultiplier = getDedicationMultiplier(caster);

        float finalHeal = (baseHeal + holyScaling) * faithBonusMultiplier * dedicationMultiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        caster.heal(spellSnapshot.holy());
        caster.addEffect(new MobEffectInstance(GenesisEffects.COLD_RESISTANCE.get(), 100, 0));
    }
}
