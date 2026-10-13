package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class HuntersInstinctMiracle extends MiracleSpell {
    private static final double DETECT_RADIUS = 16.0D;

    public HuntersInstinctMiracle() {
        super("hunters_instinct");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 13);
    }

    @Override
    public float getMentalCost() {
        return 5.4f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.LIGHTNING;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int baseDurationTicks = 1200;

        int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                .map(StatCapability::getFaith)
                .orElse(0);

        float faithBonusMultiplier = 1.0f + (faith * 0.01f);
        float dedicationMultiplier = getDedicationMultiplier(caster);
        int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

        AABB area = caster.getBoundingBox().inflate(DETECT_RADIUS);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive() && e != caster && !e.isSpectator()
        );

        for (LivingEntity target : targets) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, finalDuration, 0));
        }
    }
}
