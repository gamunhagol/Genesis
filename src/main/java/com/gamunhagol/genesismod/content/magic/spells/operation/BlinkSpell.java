package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class BlinkSpell extends MagicSpell {

    public BlinkSpell() {
        super("blink");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 7);
    }

    @Override
    public float getMentalCost() {
        return 3.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalystSnapshot) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        Vec3 startPos = caster.position();
        Vec3 lookVec = caster.getLookAngle();

        double distance = 4.0D;
        Vec3 targetPos = startPos.add(lookVec.scale(distance));

        level.playSound(
                null,
                startPos.x, startPos.y, startPos.z,
                SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS,
                1.0F,
                1.5F
        );

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.PORTAL,
                    startPos.x, startPos.y + (caster.getBbHeight() / 2.0D), startPos.z,
                    20, 0.2D, 0.4D, 0.2D, 0.1D
            );
        }

        caster.teleportTo(targetPos.x, targetPos.y, targetPos.z);
        caster.resetFallDistance();

        level.playSound(
                null,
                targetPos.x, targetPos.y, targetPos.z,
                SoundEvents.CHORUS_FRUIT_TELEPORT,
                SoundSource.PLAYERS,
                0.8F,
                1.2F
        );
    }
}