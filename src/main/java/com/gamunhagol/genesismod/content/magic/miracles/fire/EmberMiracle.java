package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Map;

public class EmberMiracle extends MiracleSpell {
    public EmberMiracle() {
        super("ember");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override
    public float getMentalCost() {
        return 2.0f;
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
        float baseFire = 6.0f;
        float finalFire = (baseFire + (catalyst.fire() * 0.5f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();

        double reach = 1.5D;
        Vec3 centerPos = eyePos.add(look.scale(reach)).subtract(0, 0.3D, 0);

        double radius = 1.2D;
        AABB hitBox = new AABB(
                centerPos.x - radius, centerPos.y - radius, centerPos.z - radius,
                centerPos.x + radius, centerPos.y + radius, centerPos.z + radius
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                hitBox,
                target -> target.isAlive() && target != caster && !caster.isAlliedTo(target)
        );

        for (LivingEntity target : targets) {
            if (!hasLineOfSight(level, eyePos, target.getBoundingBox().getCenter())) {
                continue;
            }

            GenesisDamageCalculator.applySnapshotDamage(target, caster, spellSnapshot, 1.5f, StunType.SHORT);
        }

        if (level instanceof ServerLevel serverLevel) {
            spawnCombustionParticles(serverLevel, centerPos, look);
        }
    }

    private void spawnCombustionParticles(ServerLevel level, Vec3 center, Vec3 look) {
        level.sendParticles(
                ParticleTypes.FLAME,
                center.x, center.y, center.z,
                20,
                0.25D, 0.2D, 0.25D,
                0.01D
        );

        level.sendParticles(
                ParticleTypes.LAVA,
                center.x, center.y, center.z,
                6,
                0.25D, 0.15D, 0.25D,
                0.01D
        );

        for (int i = 0; i < 15; i++) {
            double spreadX = (level.random.nextDouble() - 0.5D) * 0.15D;
            double spreadY = 0.08D + (level.random.nextDouble() * 0.12D);
            double spreadZ = (level.random.nextDouble() - 0.5D) * 0.15D;
            Vec3 burstDir = look.scale(0.08D).add(spreadX, spreadY, spreadZ);

            level.sendParticles(
                    ParticleTypes.FLAME,
                    center.x, center.y, center.z,
                    0,
                    burstDir.x, burstDir.y, burstDir.z,
                    0.5D
            );
        }
    }

    private boolean hasLineOfSight(Level level, Vec3 start, Vec3 end) {
        HitResult result = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                null
        ));
        return result.getType() == HitResult.Type.MISS;
    }
}
