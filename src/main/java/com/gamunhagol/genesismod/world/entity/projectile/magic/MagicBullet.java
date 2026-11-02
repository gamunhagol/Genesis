package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
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
    protected double maxTurnRate = 0.08D;
    protected double homingRadius = 32.0D;
    protected double weakGravity = 0.0D;

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
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        super.tick();

        Vec3 motion = this.getDeltaMovement();

        if (!this.isNoGravity() && this.weakGravity > 0.0D) {
            motion = motion.subtract(0.0D, this.weakGravity, 0.0D);
        }

        motion = applyHoming(motion);
        this.setDeltaMovement(motion);

        if (motion.lengthSqr() > 1.0E-7D) {
            double hDist = motion.horizontalDistance();

            float targetYRot = (float)(Mth.atan2(motion.x, motion.z) * (180.0F / (float)Math.PI));
            float targetXRot = (float)(-(Mth.atan2(motion.y, hDist) * (180.0F / (float)Math.PI)));

            this.setYRot(targetYRot);
            this.setXRot(targetXRot);

            if (this.lifeTicks <= 1) {
                this.yRotO = targetYRot;
                this.xRotO = targetXRot;
            }
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        if (this.isRemoved()) return;

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
            double distSqr = toTarget.lengthSqr();

            if (distSqr < 0.01D) {
                return currentMotion;
            }

            Vec3 motionDir = currentMotion.normalize();
            Vec3 targetDir = toTarget.normalize();
            double speed = currentMotion.length();

            if (motionDir.dot(targetDir) < -0.2D) {
                this.lockedTarget = null;
                return currentMotion;
            }

            Vec3 newDir = rotateTowards(motionDir, targetDir, this.maxTurnRate);
            return newDir.scale(speed);
        }

        return currentMotion;
    }

    protected Vec3 rotateTowards(Vec3 from, Vec3 to, double maxAngleRadians) {
        double dot = Mth.clamp(from.dot(to), -1.0D, 1.0D);
        double angle = Math.acos(dot);

        if (angle <= maxAngleRadians) {
            return to;
        }

        return from.lerp(to, maxAngleRadians / angle).normalize();
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

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
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
