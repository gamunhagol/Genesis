package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class MagicTrap extends MagicEntity {
    protected double triggerRadius = 0.8D;
    protected int armingDelayTicks = 5;

    public MagicTrap(EntityType<? extends MagicTrap> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false);
        this.maxLifeTicks = 600;
    }

    public MagicTrap(EntityType<? extends MagicTrap> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.setNoGravity(false);
        this.maxLifeTicks = 600;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.onGround()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x * 0.9D, motion.y - 0.04D, motion.z * 0.9D);
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(0, 0, 0);
        }

        if (this.level().isClientSide) {
            spawnIdleParticles();
        } else {
            if (this.lifeTicks >= this.armingDelayTicks) {
                checkTrigger();
            }
        }
    }

    protected void checkTrigger() {
        AABB triggerBox = this.getBoundingBox().inflate(this.triggerRadius, 0.2D, this.triggerRadius);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                triggerBox,
                e -> e != this.getOwner() && e.isAlive() && (e instanceof Enemy || e != this.getOwner())
        );

        if (!targets.isEmpty()) {
            LivingEntity victim = targets.get(0);
            triggerTrap(victim);
        }
    }

    protected void triggerTrap(LivingEntity target) {
        Entity owner = this.getOwner();

        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
        this.discard();
    }

    protected void spawnIdleParticles() {
        if (this.random.nextFloat() < 0.3F) {
            this.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() + (this.random.nextDouble() - 0.5D) * this.triggerRadius,
                    this.getY() + 0.05D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.triggerRadius,
                    0, 0.01D, 0
            );
        }
    }
}