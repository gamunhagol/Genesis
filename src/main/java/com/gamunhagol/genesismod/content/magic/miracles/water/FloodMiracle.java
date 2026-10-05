package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FloodMiracle extends MiracleSpell {

    public FloodMiracle() {
        super("flood");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 13);
    }

    @Override
    public float getMentalCost() {
        return 7.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WATER;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 eyePos = caster.getEyePosition();
        int streamCount = 7;

        for (int i = 0; i < streamCount; i++) {
            double angle = (2 * Math.PI / streamCount) * i + (serverLevel.random.nextDouble() * 0.3D - 0.15D);
            double horizontalSpeed = 0.35D + serverLevel.random.nextDouble() * 0.25D;
            double verticalSpeed = 0.4D + serverLevel.random.nextDouble() * 0.25D;

            Vec3 motion = new Vec3(
                    Math.cos(angle) * horizontalSpeed,
                    verticalSpeed,
                    Math.sin(angle) * horizontalSpeed
            );

            simulateAndPlaceWater(serverLevel, caster, eyePos, motion);
        }

        serverLevel.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void simulateAndPlaceWater(ServerLevel level, LivingEntity caster, Vec3 startPos, Vec3 initialMotion) {
        Vec3 currentPos = startPos;
        Vec3 currentMotion = initialMotion;
        double gravity = 0.04D;
        int maxSteps = 18;

        for (int step = 0; step < maxSteps; step++) {
            Vec3 nextPos = currentPos.add(currentMotion);

            BlockHitResult hitResult = level.clip(new ClipContext(
                    currentPos, nextPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    caster
            ));

            level.sendParticles(ParticleTypes.SPLASH,
                    currentPos.x, currentPos.y, currentPos.z,
                    3, 0.05D, 0.05D, 0.05D, 0.02D);

            if (hitResult.getType() != HitResult.Type.MISS) {
                BlockPos placePos = hitResult.getBlockPos().relative(hitResult.getDirection());
                level.setBlock(placePos, Blocks.WATER.defaultBlockState(), 3);
                return;
            }

            currentPos = nextPos;
            currentMotion = currentMotion.scale(0.96D).subtract(0, gravity, 0);
        }

        BlockPos finalPos = BlockPos.containing(currentPos);
        level.setBlock(finalPos, Blocks.WATER.defaultBlockState(), 3);
    }
}