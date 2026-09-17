package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SupernovaEntity extends MagicEntity implements ItemSupplier {
    protected int flightTicks = 140;
    protected int decelerateTicks = 200;
    protected int maxLifespanTicks = 300;

    protected float contactExplosionPower = 3.0F;
    protected float apexExplosionPower = 10.0F;

    private double baseSpeed = 0.19D;

    public SupernovaEntity(EntityType<? extends SupernovaEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
    }

    public SupernovaEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 direction) {
        super(GenesisEntities.SUPERNOVA.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
        this.setDeltaMovement(direction.normalize().scale(this.baseSpeed));
        this.hasImpulse = true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        handleMovementPhases();

        if (!this.level().isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }

            checkProximityContact();

            if (this.isRemoved()) return;

            if (this.tickCount >= this.maxLifespanTicks) {
                detonate(this.apexExplosionPower, 35.0F);
                this.discard();
                return;
            }
        }
        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    private void handleMovementPhases() {
        Vec3 currentMotion = this.getDeltaMovement();

        if (this.tickCount <= this.flightTicks) {
            if (currentMotion.lengthSqr() > 1.0E-6D) {
                this.setDeltaMovement(currentMotion.normalize().scale(this.baseSpeed));
            }
        } else if (this.tickCount <= this.decelerateTicks) {
            float progress = (float) (this.tickCount - this.flightTicks) / (this.decelerateTicks - this.flightTicks);
            double currentSpeed = this.baseSpeed * Math.max(0.0D, 1.0D - progress);

            if (currentMotion.lengthSqr() > 1.0E-6D) {
                this.setDeltaMovement(currentMotion.normalize().scale(currentSpeed));
            } else {
                this.setDeltaMovement(Vec3.ZERO);
            }
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    private void checkProximityContact() {
        AABB touchBox = this.getBoundingBox().inflate(0.3D);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                touchBox,
                e -> e.isAlive() && e != this.getOwner()
        );

        if (!targets.isEmpty()) {
            handleCollision(targets.get(0));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            handleCollision(result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            handleCollision(null);
        }
    }

    private void handleCollision(Entity hitEntity) {
        if (hitEntity instanceof LivingEntity target && hitEntity != this.getOwner()) {
            applyDirectElementalDamage(target);
        }

        detonate(this.contactExplosionPower, 10.0F);
        this.discard();
    }

    private void applyDirectElementalDamage(LivingEntity target) {
        Entity owner = this.getOwner();

        if (this.damageSnapshot.physical() > 0) target.hurt(this.damageSources().indirectMagic(this, owner), this.damageSnapshot.physical());
        if (this.damageSnapshot.magic() > 0) target.hurt(this.damageSources().indirectMagic(this, owner), this.damageSnapshot.magic());
        if (this.damageSnapshot.fire() > 0) target.hurt(this.damageSources().inFire(), this.damageSnapshot.fire());
        if (this.damageSnapshot.lightning() > 0) target.hurt(this.damageSources().lightningBolt(), this.damageSnapshot.lightning());
        if (this.damageSnapshot.frost() > 0) target.hurt(this.damageSources().freeze(), this.damageSnapshot.frost());
        if (this.damageSnapshot.holy() > 0) target.hurt(this.damageSources().indirectMagic(this, owner), this.damageSnapshot.holy());
        if (this.damageSnapshot.destruction() > 0) target.hurt(this.damageSources().indirectMagic(this, owner), this.damageSnapshot.destruction());
    }

    protected void detonate(float explosionPower, float baseVoidDamage) {
        Entity owner = this.getOwner();

        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                explosionPower,
                Level.ExplosionInteraction.MOB
        );

        double range = explosionPower;
        AABB voidArea = this.getBoundingBox().inflate(range);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                voidArea,
                e -> e.isAlive() && e != owner
        );

        double rSqr = range * range;
        for (LivingEntity target : targets) {
            double distSqr = target.distanceToSqr(this);
            if (distSqr <= rSqr) {
                double dist = Math.sqrt(distSqr);
                float falloff = (float) Math.max(0.0D, 1.0D - (dist / range));
                float finalVoidDamage = baseVoidDamage * falloff;

                if (finalVoidDamage > 0.0F) {
                    target.hurt(this.damageSources().fellOutOfWorld(), finalVoidDamage);
                }
            }
        }
    }
}