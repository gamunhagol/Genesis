package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class HomingMeteorBullet extends MagicBullet {
    protected int homingDelayTicks = 10;
    protected double maxSpeed = 1.2D;
    protected double accelerationRate = 1.02D;

    public HomingMeteorBullet(EntityType<? extends HomingMeteorBullet> entityType, Level level) {
        super(entityType, level);
        this.maxTurnRate = 0.12D;
        this.homingRadius = 30.0D;
    }

    public HomingMeteorBullet(EntityType<? extends HomingMeteorBullet> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.maxTurnRate = 0.12D;
        this.homingRadius = 30.0D;
    }

    @Override
    public void tick() {
        if (this.tickCount > this.homingDelayTicks && this.accelerationRate > 1.0D) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.length() < this.maxSpeed) {
                this.setDeltaMovement(motion.scale(this.accelerationRate));
            }
        }
        super.tick();
    }

    @Override
    protected Vec3 applyHoming(Vec3 currentMotion) {
        if (this.tickCount <= this.homingDelayTicks) {
            return currentMotion;
        }
        return super.applyHoming(currentMotion);
    }
}
