package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.core.particles.ParticleOptions;
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

public abstract class AbstractMagicDomainEntity extends MagicEntity implements ItemSupplier {
    protected double domainRadius = 1.5D;
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

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
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
        AABB domainBox = new AABB(
                this.getX() - this.domainRadius,
                this.getY() - this.heightCheckBelow,
                this.getZ() - this.domainRadius,
                this.getX() + this.domainRadius,
                this.getY() + this.heightCheckAbove,
                this.getZ() + this.domainRadius
        );

        List<LivingEntity> insideEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                domainBox,
                e -> e.isAlive() && (e == this.getOwner() || (this.getOwner() != null && !e.isAlliedTo(this.getOwner())))
        );

        for (LivingEntity entity : insideEntities) {
            applyEntityBuff(entity);
        }
    }

    protected abstract void applyEntityBuff(LivingEntity entity);

    protected void spawnFloorParticles() {
        if (this.random.nextFloat() < 0.25F) {
            double rx = (this.random.nextDouble() - 0.5D) * (this.domainRadius * 2.0D);
            double rz = (this.random.nextDouble() - 0.5D) * (this.domainRadius * 2.0D);
            this.level().addParticle(getFloorParticle(), this.getX() + rx, this.getY() + 0.05D, this.getZ() + rz, 0, 0.01D, 0);
        }
    }

    protected ParticleOptions getFloorParticle() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    public abstract float getMagicDamageMultiplier();

    public static float getMultiplierAt(Entity entity) {
        if (entity == null || entity.level().isClientSide) return 1.0F;

        AABB checkArea = new AABB(
                entity.getX() - 1.5D, entity.getY() - 1.0D, entity.getZ() - 1.5D,
                entity.getX() + 1.5D, entity.getY() + 5.0D, entity.getZ() + 1.5D
        );

        List<AbstractMagicDomainEntity> domains = entity.level().getEntitiesOfClass(
                AbstractMagicDomainEntity.class,
                checkArea,
                d -> d.isAlive() && d.getOwner() == entity
        );

        if (domains.isEmpty()) return 1.0F;

        float max = 1.0F;
        for (AbstractMagicDomainEntity domain : domains) {
            max = Math.max(max, domain.getMagicDamageMultiplier());
        }
        return max;
    }
}