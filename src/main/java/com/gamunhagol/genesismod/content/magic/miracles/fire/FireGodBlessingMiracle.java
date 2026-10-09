package com.gamunhagol.genesismod.content.magic.miracles.fire;

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

public class FireGodBlessingMiracle extends MiracleSpell {

    public FireGodBlessingMiracle() {
        super("fire_god_blessing");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 15
        );
    }

    @Override
    public float getMentalCost() {
        return 7.3f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
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
                .orElse(0);

        float faithBonusMultiplier = 1.0f + (faith * 0.01f);
        float dedicationMultiplier = getDedicationMultiplier(caster);

        int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

        caster.addEffect(new MobEffectInstance(GenesisEffects.FIRE_REFLECTION.get(), finalDuration, 0));

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    caster.getX(), caster.getY() + (caster.getBbHeight() * 0.5D), caster.getZ(),
                    25, 0.4D, 0.5D, 0.4D, 0.05D
            );
            serverLevel.sendParticles(
                    ParticleTypes.LAVA,
                    caster.getX(), caster.getY() + (caster.getBbHeight() * 0.5D), caster.getZ(),
                    5, 0.2D, 0.2D, 0.2D, 0.0D
            );
        }

        level.playSound(
                null,
                caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.PLAYERS,
                1.0F,
                0.8F
        );
    }
}