package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public abstract class HomingMeteorBullet extends MagicBullet {
    protected double dotLimit = -0.5D;
    protected double closeCutoffDistance = 1.0D;

    protected int homingDelayTicks = 12;
    protected double maxSpeed = 1.15D;

    public HomingMeteorBullet(EntityType<? extends HomingMeteorBullet> entityType, Level level) {
        super(entityType, level);
        this.homingStrength = 0.18f;
        this.homingRadius = 40.0D;
        this.weakGravity = 0.001D;
    }

    public HomingMeteorBullet(EntityType<? extends HomingMeteorBullet> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.homingStrength = 0.18f;
        this.homingRadius = 24.0D;
        this.weakGravity = 0.001D;
    }

    @Override
    public void tick() {
        Vec3 motion = this.getDeltaMovement();
        double currentSpeed = motion.length();

        if (currentSpeed < this.maxSpeed) {
            this.setDeltaMovement(motion.scale(1.03D));
        }

        super.tick();
    }

    @Override
    protected Vec3 applyHoming(Vec3 currentMotion) {
        if (this.tickCount <= this.homingDelayTicks) {
            return currentMotion;
        }

        if (this.lockedTarget == null || !this.lockedTarget.isAlive() || this.lockedTarget.isRemoved()) {
            List<LivingEntity> targets = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox().inflate(this.homingRadius),
                    e -> e != this.getOwner() && e.isAlive() && (e instanceof Enemy || e != this.getOwner())
            );

            this.lockedTarget = targets.stream()
                    .min(Comparator.comparingDouble(this::distanceToSqr))
                    .orElse(null);
        }

        if (this.lockedTarget != null) {
            Vec3 toTarget = this.lockedTarget.getBoundingBox().getCenter().subtract(this.position());
            double distance = toTarget.length();

            if (distance < this.closeCutoffDistance) {
                return currentMotion;
            }

            Vec3 motionDir = currentMotion.normalize();
            Vec3 targetDir = toTarget.normalize();
            double speed = currentMotion.length();

            float progressiveHoming = Math.min(this.homingStrength, (this.tickCount - this.homingDelayTicks) * 0.015f);

            double dot = motionDir.dot(targetDir);
            if (dot > this.dotLimit) {
                Vec3 newDir = motionDir.lerp(targetDir, progressiveHoming).normalize();
                return newDir.scale(speed);
            } else {
                this.lockedTarget = null;
            }
        }

        return currentMotion;
    }
}