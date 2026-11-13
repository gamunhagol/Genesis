package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class AbstractMagicDomainEntity extends MagicEntity {
    protected double heightCheckAbove = 5.0D;
    protected double heightCheckBelow = 1.0D;

    public AbstractMagicDomainEntity(EntityType<? extends AbstractMagicDomainEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.maxLifeTicks = 1800;
    }

    public AbstractMagicDomainEntity(EntityType<? extends AbstractMagicDomainEntity> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.setNoGravity(true);
        this.noPhysics = true;
        this.maxLifeTicks = 1800;
    }

    public double getDomainRadius() {
        return this.getBbWidth() * 0.5D;
    }

    public AABB getDomainBox() {
        double r = getDomainRadius();
        return new AABB(
                this.getX() - r,
                this.getY() - this.heightCheckBelow,
                this.getZ() - r,
                this.getX() + r,
                this.getY() + this.heightCheckAbove,
                this.getZ() + r
        );
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);

        if (this.level().isClientSide) {
            spawnFloorParticles();
        } else {
            applyDomainBuff();
        }
    }

    protected void applyDomainBuff() {
        List<LivingEntity> insideEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                getDomainBox(),
                e -> e.isAlive() && (e == this.getOwner() || (this.getOwner() != null && !e.isAlliedTo(this.getOwner())))
        );

        for (LivingEntity entity : insideEntities) {
            applyEntityBuff(entity);
        }
    }

    protected abstract void applyEntityBuff(LivingEntity entity);

    protected void spawnFloorParticles() {
    }

    public abstract float getMagicDamageMultiplier();

    public static float getMultiplierAt(Entity entity) {
        if (entity == null || entity.level().isClientSide) return 1.0F;

        AABB searchArea = entity.getBoundingBox().inflate(8.0D);

        List<AbstractMagicDomainEntity> domains = entity.level().getEntitiesOfClass(
                AbstractMagicDomainEntity.class,
                searchArea,
                d -> d.isAlive() && d.getOwner() == entity && d.getDomainBox().intersects(entity.getBoundingBox())
        );

        if (domains.isEmpty()) return 1.0F;

        float max = 1.0F;
        for (AbstractMagicDomainEntity domain : domains) {
            max = Math.max(max, domain.getMagicDamageMultiplier());
        }
        return max;
    }
}
