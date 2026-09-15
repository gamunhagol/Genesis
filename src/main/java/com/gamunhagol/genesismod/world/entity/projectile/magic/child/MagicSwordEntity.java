package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicBullet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class MagicSwordEntity extends MagicBullet implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK =
            SynchedEntityData.defineId(MagicSwordEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final int MAX_IDLE_LIFESPAN = 900;
    private static final int MAX_HOMING_LIFESPAN = 300;

    private boolean isLaunched = false;
    private int launchDelayTicks = 0;
    private int homingTicks = 0;

    private double sideOffset = 0.0D;
    private double heightOffset = 0.8D;

    protected double maxSpeed = 1.3D;
    protected double dotLimit = -0.3D;
    protected double closeCutoffDistance = 0.8D;

    public MagicSwordEntity(EntityType<? extends MagicSwordEntity> entityType, Level level) {
        super(entityType, level);
        this.homingStrength = 0.22f;
        this.homingRadius = 24.0D;
        this.weakGravity = 0.0D;
        this.setNoGravity(true);
    }

    public MagicSwordEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_SWORD.get(), level, owner, snapshot);
        this.homingStrength = 0.22f;
        this.homingRadius = 24.0D;
        this.weakGravity = 0.0D;
        this.setNoGravity(true);
        this.launchDelayTicks = 5 + this.random.nextInt(21);
    }

    public MagicSwordEntity setOrbit(double sideOffset, double heightOffset) {
        this.sideOffset = sideOffset;
        this.heightOffset = heightOffset;
        return this;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM_STACK, new ItemStack(Items.DIAMOND_SWORD));
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM_STACK, stack.copy());
    }

    @Override
    public ItemStack getItem() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (!this.isLaunched) {
                handleIdleState();
            } else {
                handleLaunchedState();
            }
        }

        super.tick();
    }

    private void handleIdleState() {
        if (this.tickCount >= MAX_IDLE_LIFESPAN) {
            this.discard();
            return;
        }

        LivingEntity owner = (LivingEntity) this.getOwner();
        if (owner == null || !owner.isAlive() || owner.isRemoved()) {
            this.discard();
            return;
        }

        float currentYaw = owner.getYRot();
        float yawRad = currentYaw * Mth.DEG_TO_RAD;

        // 시전자 시선의 좌우 직교 벡터 계산
        double rightX = -Math.cos(yawRad);
        double rightZ = -Math.sin(yawRad);

        // 시전자 기준 상대 좌표 갱신 (좌우 분산 + 포물선 높이)
        double targetX = owner.getX() + (rightX * this.sideOffset);
        double targetY = owner.getY() + owner.getBbHeight() + this.heightOffset;
        double targetZ = owner.getZ() + (rightZ * this.sideOffset);

        this.setPos(targetX, targetY, targetZ);
        this.xo = targetX;
        this.yo = targetY;
        this.zo = targetZ;

        // 대기 중 시전자와 동일한 시선(각도) 유지
        this.setYRot(currentYaw);
        this.setXRot(owner.getXRot());
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.setDeltaMovement(Vec3.ZERO);

        // 타겟 탐색
        if (this.lockedTarget == null || !this.lockedTarget.isAlive() || this.lockedTarget.isRemoved()) {
            List<LivingEntity> targets = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox().inflate(this.homingRadius),
                    e -> e != owner && e.isAlive() && (e instanceof Enemy || e != owner)
            );

            this.lockedTarget = targets.stream()
                    .min(Comparator.comparingDouble(this::distanceToSqr))
                    .orElse(null);
        }

        // 타겟 포착 시 엇박자 발사
        if (this.lockedTarget != null) {
            if (this.launchDelayTicks > 0) {
                this.launchDelayTicks--;
            } else {
                this.isLaunched = true;
                Vec3 initialDir = this.lockedTarget.getBoundingBox().getCenter().subtract(this.position()).normalize();
                this.setDeltaMovement(initialDir.scale(0.3D));
            }
        }
    }

    private void handleLaunchedState() {
        this.homingTicks++;
        if (this.homingTicks >= MAX_HOMING_LIFESPAN) {
            this.discard();
            return;
        }

        Vec3 motion = this.getDeltaMovement();
        double currentSpeed = motion.length();
        if (currentSpeed < this.maxSpeed) {
            this.setDeltaMovement(motion.scale(1.05D));
        }
    }

    @Override
    protected Vec3 applyHoming(Vec3 currentMotion) {
        if (!this.isLaunched || this.lockedTarget == null || !this.lockedTarget.isAlive() || this.lockedTarget.isRemoved()) {
            return currentMotion;
        }

        Vec3 toTarget = this.lockedTarget.getBoundingBox().getCenter().subtract(this.position());
        double distance = toTarget.length();

        if (distance < this.closeCutoffDistance) {
            return currentMotion;
        }

        Vec3 motionDir = currentMotion.normalize();
        Vec3 targetDir = toTarget.normalize();
        double speed = currentMotion.length();

        double dot = motionDir.dot(targetDir);
        if (dot > this.dotLimit) {
            Vec3 newDir = motionDir.lerp(targetDir, this.homingStrength).normalize();
            return newDir.scale(speed);
        } else {
            this.lockedTarget = null;
        }

        return currentMotion;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    protected void spawnFlightParticles() {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsLaunched", this.isLaunched);
        tag.putInt("LaunchDelayTicks", this.launchDelayTicks);
        tag.putInt("HomingTicks", this.homingTicks);
        tag.putDouble("SideOffset", this.sideOffset);
        tag.putDouble("HeightOffset", this.heightOffset);
        tag.put("Item", this.getItem().save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.isLaunched = tag.getBoolean("IsLaunched");
        this.launchDelayTicks = tag.getInt("LaunchDelayTicks");
        this.homingTicks = tag.getInt("HomingTicks");
        this.sideOffset = tag.getDouble("SideOffset");
        this.heightOffset = tag.getDouble("HeightOffset");
        if (tag.contains("Item", 10)) {
            this.setItem(ItemStack.of(tag.getCompound("Item")));
        }
    }
}