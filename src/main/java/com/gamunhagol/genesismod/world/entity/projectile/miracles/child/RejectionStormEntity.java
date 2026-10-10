package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RejectionStormEntity extends MagicEntity {
    private final double halfSize = 4.0D;
    private final double pushStrength = 0.4D;
    private float dedicationMultiplier = 1.0F;

    public RejectionStormEntity(EntityType<? extends RejectionStormEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 200;
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public RejectionStormEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, float dedicationMultiplier) {
        super(GenesisEntities.REJECTION_STORM.get(), level, owner, snapshot);
        this.maxLifeTicks = 200;
        this.dedicationMultiplier = dedicationMultiplier;
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();

            if (owner == null || !owner.isAlive() || owner.isRemoved()) {
                this.discard();
                return;
            }

            this.setPos(owner.getX(), owner.getY(), owner.getZ());

            applyContinuousPush();

            if (this.tickCount % 2 == 0 && this.level() instanceof ServerLevel serverLevel) {
                spawnFloorParticles(serverLevel);
            }
        }
    }

    private void applyContinuousPush() {
        Vec3 center = this.position();
        double feetY = this.getY();

        AABB floorBox = new AABB(
                center.x - this.halfSize, feetY - 0.5D, center.z - this.halfSize,
                center.x + this.halfSize, feetY + 1.5D, center.z + this.halfSize
        );

        LivingEntity owner = (LivingEntity) this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                floorBox,
                e -> e != owner && e.isAlive()
        );

        for (LivingEntity target : targets) {
            Vec3 targetPos = target.position();
            double dx = targetPos.x - center.x;
            double dz = targetPos.z - center.z;
            double distH = Math.sqrt(dx * dx + dz * dz);

            if (distH <= this.halfSize) {
                double falloff = Math.max(0.2D, 1.0D - (distH / this.halfSize));
                double strength = this.pushStrength * falloff * this.dedicationMultiplier;

                Vec3 pushDir;
                if (distH > 1.0E-4D) {
                    pushDir = new Vec3(dx / distH, 0.0D, dz / distH);
                } else {
                    pushDir = new Vec3(1.0D, 0.0D, 0.0D);
                }

                target.setDeltaMovement(target.getDeltaMovement().add(pushDir.scale(strength).add(0.0D, 0.05D, 0.0D)));
                target.hurtMarked = true;
                target.hasImpulse = true;
            }
        }
    }

    private void spawnFloorParticles(ServerLevel serverLevel) {
        Vec3 center = this.position();
        for (int i = 0; i < 8; i++) {
            double rx = (serverLevel.random.nextDouble() - 0.5D) * (this.halfSize * 2.0D);
            double rz = (serverLevel.random.nextDouble() - 0.5D) * (this.halfSize * 2.0D);

            Vec3 outDir = new Vec3(rx, 0, rz).normalize();
            double speed = 0.2D + serverLevel.random.nextDouble() * 0.15D;

            serverLevel.sendParticles(
                    ParticleTypes.POOF,
                    center.x + rx, center.y + 0.05D, center.z + rz,
                    1,
                    outDir.x * speed, 0.05D, outDir.z * speed,
                    0.05D
            );
        }
    }
}