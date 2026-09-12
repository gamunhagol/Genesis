package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public abstract class MagicBullet extends MagicEntity implements ItemSupplier {
    protected float homingStrength = 0.08f;
    protected double homingRadius = 12.0D;
    protected double weakGravity = 0.006D;
    protected double baseSpeed = -1.0D;

    protected LivingEntity lockedTarget = null;

    public MagicBullet(EntityType<? extends MagicBullet> entityType, Level level) {
        super(entityType, level);
    }

    public MagicBullet(EntityType<? extends MagicBullet> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 motion = this.getDeltaMovement();

        if (this.baseSpeed < 0) {
            this.baseSpeed = motion.length();
            if (this.baseSpeed < 0.1D) this.baseSpeed = 1.0D;
        }

        motion = new Vec3(motion.x, motion.y - this.weakGravity, motion.z);

        if (!this.level().isClientSide) {
            motion = applyHoming(motion);
        }

        motion = motion.normalize().scale(this.baseSpeed);
        this.setDeltaMovement(motion);

        HitResult hitresult = net.minecraft.world.entity.projectile.ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();

        if (this.level().isClientSide) {
            spawnFlightParticles();
        }
    }

    protected Vec3 applyHoming(Vec3 currentMotion) {
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

            // 1. 근접 관성 진입 거리 확대 (3.5블록 이내에서는 조향 컷)
            if (distance < 3.5D) {
                return currentMotion;
            }

            Vec3 motionDir = currentMotion.normalize();
            Vec3 targetDir = toTarget.normalize();

            double dot = motionDir.dot(targetDir);

            // 2. 정면 각도를 약 53도(dot > 0.6) 이내로 엄격하게 제한
            if (dot > 0.6D) {
                return currentMotion.normalize().lerp(targetDir, this.homingStrength);
            } else {
                // 3. 시야각을 한 번 벗어난 타겟은 락온을 즉시 풀어 급회전 방지
                this.lockedTarget = null;
            }
        }

        return currentMotion;
    }

    protected void spawnFlightParticles() {
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            Entity owner = this.getOwner();

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                cap.setSnapshot(this.damageSnapshot);
            });

            target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }
}