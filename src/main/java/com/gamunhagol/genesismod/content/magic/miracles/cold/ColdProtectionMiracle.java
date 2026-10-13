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

public class ColdProtectionMiracle extends MiracleSpell {

    public ColdProtectionMiracle() {
        super("cold_protection");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 13);
    }

    @Override
    public float getMentalCost() {
        return 5.2f;
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

        int baseDurationTicks = 1800;

        int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                .map(StatCapability::getFaith)
                .orElse(10);

        float faithBonusMultiplier = 1.0f + (faith * 0.01f);
        float dedicationMultiplier = getDedicationMultiplier(caster);

        int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

        caster.addEffect(new MobEffectInstance(GenesisEffects.COLD_RESISTANCE.get(), finalDuration, 0));
    }
}
