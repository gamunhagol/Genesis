package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class MagicTrap extends MagicEntity {
    protected double triggerRadius = 1.0D;
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
            this.setDeltaMovement(Vec3.ZERO);
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
        AABB triggerBox = this.getBoundingBox().inflate(this.triggerRadius, 0.5D, this.triggerRadius);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                triggerBox,
                e -> e != this.getOwner() && e.isAlive() && (e instanceof Enemy || e != this.getOwner())
        );

        if (!targets.isEmpty()) {
            triggerTrap(targets.get(0));
        }
    }

    protected void triggerTrap(LivingEntity target) {
        Entity owner = this.getOwner();
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));

        float damage = this.damageSnapshot.magic();
        target.hurt(this.damageSources().indirectMagic(this, owner), damage);

        this.discard();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void spawnIdleParticles() {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        double smokeRadius = 1.0D;

        if (this.random.nextFloat() < 0.065F) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D * smokeRadius;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D * smokeRadius;

            double spawnX = this.getX() + offsetX;
            double spawnY = this.getY() + 0.02D;
            double spawnZ = this.getZ() + offsetZ;

            double speedX = (this.random.nextDouble() - 0.5D) * 0.005D;
            double speedY = 0.001D;
            double speedZ = (this.random.nextDouble() - 0.5D) * 0.005D;

            Particle blueSmoke = engine.createParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    spawnX, spawnY, spawnZ,
                    speedX, speedY, speedZ
            );

            if (blueSmoke != null) {
                float tint = this.random.nextFloat() * 0.05F;
                float r = 0.08F + tint;
                float g = 0.12F + tint;
                float b = 0.35F + tint;
                blueSmoke.setColor(r, g, b);

                blueSmoke.scale(2.0F);
                blueSmoke.setLifetime(80 + this.random.nextInt(41));
            }
        }

        if (this.random.nextFloat() < 0.085F) {
            double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D * smokeRadius;
            double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D * smokeRadius;

            double spawnX = this.getX() + offsetX;
            double spawnY = this.getY() + 0.02D;
            double spawnZ = this.getZ() + offsetZ;

            double speedX = (this.random.nextDouble() - 0.5D) * 0.005D;
            double speedY = 0.001D;
            double speedZ = (this.random.nextDouble() - 0.5D) * 0.005D;

            Particle darkSmoke = engine.createParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    spawnX, spawnY, spawnZ,
                    speedX, speedY, speedZ
            );

            if (darkSmoke != null) {
                float shade = 0.08F + this.random.nextFloat() * 0.06F;
                darkSmoke.setColor(shade, shade, shade);

                darkSmoke.scale(1.5F);
                darkSmoke.setLifetime(80 + this.random.nextInt(31));
            }
        }
    }
}
