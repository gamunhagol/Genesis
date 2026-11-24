package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.List;

public class LightningStakeEntity extends MagicEntity implements ItemSupplier {
    private static final double STAKE_SPEED = 2.5D;
    private static final double AOE_RADIUS = 3.5D;
    private static final EntityDataAccessor<Integer> DATA_HOLD_TICKS = SynchedEntityData.defineId(LightningStakeEntity.class, EntityDataSerializers.INT);

    private Vec3 pendingDir = new Vec3(0, -1, 0);

    public LightningStakeEntity(EntityType<? extends LightningStakeEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 400;
    }

    public LightningStakeEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 shootDir, int holdTicks) {
        super(GenesisEntities.LIGHTNING_STAKE.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.maxLifeTicks = 400;
        this.pendingDir = shootDir.lengthSqr() > 1.0E-7D ? shootDir.normalize() : new Vec3(0, -1, 0);
        this.setHoldTicks(holdTicks);

        if (holdTicks <= 0) {
            this.setDeltaMovement(this.pendingDir.scale(STAKE_SPEED));
            this.hasImpulse = true;
            this.updateRotation();
        } else {
            this.setDeltaMovement(Vec3.ZERO);
            this.setXRot(90.0F);
            this.setYRot(owner.getYRot());
            this.xRotO = this.getXRot();
            this.yRotO = this.getYRot();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HOLD_TICKS, 0);
    }

    public int getHoldTicks() {
        return this.entityData.get(DATA_HOLD_TICKS);
    }

    public void setHoldTicks(int ticks) {
        this.entityData.set(DATA_HOLD_TICKS, ticks);
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.getHoldTicks() > 0) return false;
        return super.canHitEntity(entity);
    }

    @Override
    public void tick() {
        int hold = this.getHoldTicks();
        if (hold > 0) {
            if (this.getOwner() instanceof LivingEntity owner) {
                if (!owner.isAlive()) {
                    if (!this.level().isClientSide) this.discard();
                    return;
                }
                Vec3 handPos = calculateHandHoldPos(owner);
                this.setPos(handPos.x, handPos.y, handPos.z);
                this.setYRot(owner.getYRot());
                this.setXRot(90.0F);
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }

            if (!this.level().isClientSide) {
                int nextHold = hold - 1;
                this.setHoldTicks(nextHold);
                if (nextHold <= 0) {
                    this.setDeltaMovement(this.pendingDir.scale(STAKE_SPEED));
                    this.hasImpulse = true;
                    this.updateRotation();
                }
            }
            return;
        }

        super.tick();
        if (this.isHit()) return;

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        if (this.isRemoved() || this.isHit()) return;

        Vec3 motion = this.getDeltaMovement();
        this.updateRotation();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    private Vec3 calculateHandHoldPos(LivingEntity caster) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, caster.getYRot());
        Vec3 right = Vec3.directionFromRotation(0.0F, caster.getYRot() + 90.0F);

        boolean isRightHand = true;
        if (caster instanceof Player player) {
            boolean isMainHand = caster.getUsedItemHand() == InteractionHand.MAIN_HAND;
            isRightHand = (player.getMainArm() == HumanoidArm.RIGHT) == isMainHand;
        }
        double sideMult = isRightHand ? 1.0D : -1.0D;

        return new Vec3(
                caster.getX() + forward.x * 0.6D + right.x * 0.35D * sideMult,
                caster.getY() + 1.4D,
                caster.getZ() + forward.z * 0.6D + right.z * 0.35D * sideMult
        );
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.isHit() || this.getHoldTicks() > 0) return;
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            Entity owner = this.getOwner();

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
            DamageSource source = this.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY)
                    .filter(patch -> patch instanceof ProjectilePatch<?>)
                    .map(patch -> (DamageSource) ((ProjectilePatch<?>) patch).createEpicFightDamageSource())
                    .orElseGet(() -> this.damageSources().indirectMagic(this, owner));
            target.hurt(source, 1.0F);

            applyAreaDamage(result.getLocation(), target);
            this.triggerHit(result.getLocation());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.isHit() || this.getHoldTicks() > 0) return;
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            applyAreaDamage(result.getLocation(), null);
            this.triggerHit(result.getLocation());
        }
    }

    private void applyAreaDamage(Vec3 center, Entity directHitTarget) {
        Entity owner = this.getOwner();
        AABB area = new AABB(
                center.x - AOE_RADIUS, center.y - 1.0D, center.z - AOE_RADIUS,
                center.x + AOE_RADIUS, center.y + 2.0D, center.z + AOE_RADIUS
        );

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive() && e != owner && e != directHitTarget
        );

        double radiusSqr = AOE_RADIUS * AOE_RADIUS;
        for (LivingEntity target : targets) {
            if (target.distanceToSqr(center) <= radiusSqr) {
                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));

                DamageSource aoeSource = this.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY)
                        .filter(patch -> patch instanceof ProjectilePatch<?>)
                        .map(patch -> {
                            EpicFightDamageSource ef = ((ProjectilePatch<?>) patch).createEpicFightDamageSource();
                            ef.setBaseImpact(ef.getBaseImpact() * 0.5F);
                            return (DamageSource) ef;
                        })
                        .orElseGet(() -> this.damageSources().indirectMagic(this, owner));

                target.hurt(aoeSource, 1.0F);
            }
        }
    }
}
