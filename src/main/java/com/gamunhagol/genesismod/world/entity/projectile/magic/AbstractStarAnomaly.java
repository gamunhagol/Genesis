package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class AbstractStarAnomaly extends MagicEntity implements ItemSupplier {
    protected int maxLifespanTicks = 60;
    protected double pullRadius = 3.0D;
    protected double maxPullStrength = 0.08D;
    protected float explosionPower = 4.0F;

    public AbstractStarAnomaly(EntityType<? extends AbstractStarAnomaly> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public AbstractStarAnomaly(EntityType<? extends AbstractStarAnomaly> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.setNoGravity(true);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);

        if (this.level().isClientSide) {
            spawnIdleParticles();
        } else {
            applyGravitationalPull();

            if (this.tickCount >= this.maxLifespanTicks) {
                this.detonate();
                this.discard();
            }
        }
    }

    protected void spawnIdleParticles() {
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
    }

    protected void applyGravitationalPull() {
        AABB box = this.getBoundingBox().inflate(this.pullRadius);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, box, Entity::isAlive);

        Vec3 center = this.position();

        for (LivingEntity target : targets) {
            Vec3 toCenter = center.subtract(target.position());
            double dist = toCenter.length();

            if (dist > 0.1D && dist <= this.pullRadius) {
                double falloff = 1.0D - (dist / this.pullRadius);
                double currentPull = this.maxPullStrength * falloff;

                Vec3 pullVector = toCenter.normalize().scale(currentPull);

                target.setDeltaMovement(target.getDeltaMovement().add(pullVector));
                target.hurtMarked = true;
            }
        }
    }

    protected void detonate() {
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.MOB
        );

        applyMagicDamageToArea(this.explosionPower * 1.5D);
    }

    protected void applyMagicDamageToArea(double range) {
        AABB damageArea = this.getBoundingBox().inflate(range);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, damageArea, Entity::isAlive);
        Entity owner = this.getOwner();
        double rSqr = range * range;

        for (LivingEntity target : targets) {
            if (target.distanceToSqr(this) <= rSqr) {
                target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
            }
        }
    }
}