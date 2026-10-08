package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WaterspoutEntity extends MagicEntity implements ItemSupplier {

    private static final EntityDataAccessor<Integer> DATA_SEGMENT_INDEX =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PARENT_ID =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EMPOWERED_WATER =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.BOOLEAN);

    protected double basePullRadius = 6.5D;
    protected double basePullStrength = 0.15D;
    protected int wanderChangeInterval = 40;
    protected Vec3 wanderVelocity = Vec3.ZERO;

    public WaterspoutEntity(EntityType<? extends WaterspoutEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 600;
    }

    public WaterspoutEntity(EntityType<? extends WaterspoutEntity> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot, int segmentIndex, int parentId) {
        super(entityType, level, owner, snapshot);
        this.maxLifeTicks = 600;
        this.setSegmentIndex(segmentIndex);
        this.setParentId(parentId);

        if (segmentIndex == 0) {
            this.setNoGravity(false);
            this.noPhysics = false;
        } else {
            this.setNoGravity(true);
            this.noPhysics = true;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SEGMENT_INDEX, 0);
        this.entityData.define(DATA_PARENT_ID, -1);
        this.entityData.define(DATA_EMPOWERED_WATER, false);
    }

    public int getSegmentIndex() {
        return this.entityData.get(DATA_SEGMENT_INDEX);
    }

    public void setSegmentIndex(int index) {
        this.entityData.set(DATA_SEGMENT_INDEX, index);
        if (index == 0) {
            this.setNoGravity(false);
            this.noPhysics = false;
        } else {
            this.setNoGravity(true);
            this.noPhysics = true;
        }
    }

    public int getParentId() {
        return this.entityData.get(DATA_PARENT_ID);
    }

    public void setParentId(int id) {
        this.entityData.set(DATA_PARENT_ID, id);
    }

    public boolean isEmpoweredByWater() {
        return this.entityData.get(DATA_EMPOWERED_WATER);
    }

    public void setEmpoweredByWater(boolean empowered) {
        this.entityData.set(DATA_EMPOWERED_WATER, empowered);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        int index = getSegmentIndex();

        if (index != 0) {
            this.lifeTicks = 0;

            WaterspoutEntity root = findRootSpout();
            if (root == null || !root.isAlive()) {
                this.discard();
                return;
            }
        }

        super.tick();

        if (!this.level().isClientSide) {
            if (index == 0) {
                handleRootMovementAndFluids();
            } else {
                handleFollowParent(index);
            }

            applyVortexPull();
            applyContactEffects();
        }
    }

    private void handleRootMovementAndFluids() {
        BlockPos currentPos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(currentPos);
        BlockState blockState = this.level().getBlockState(currentPos);

        if (fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA || blockState.is(Blocks.LAVA)) {
            boolean isSource = (fluid.getType() == Fluids.LAVA && fluid.isSource()) || blockState.is(Blocks.LAVA);
            this.level().setBlock(currentPos, isSource ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState(), 3);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.2F);

            this.lifeTicks += 4;
            this.setEmpoweredByWater(false);
        }
        else if (fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER || this.isInWater()) {
            this.setEmpoweredByWater(true);
            if (this.tickCount % 2 == 0 && this.lifeTicks > 0) {
                this.lifeTicks--;
            }
        } else {
            this.setEmpoweredByWater(false);
        }

        if (this.tickCount % this.wanderChangeInterval == 0 || this.wanderVelocity.lengthSqr() < 1.0E-4D) {
            float angle = this.random.nextFloat() * ((float) Math.PI * 2F);
            double speed = 0.08D + (this.random.nextDouble() * 0.04D);
            this.wanderVelocity = new Vec3(Math.cos(angle) * speed, 0.0D, Math.sin(angle) * speed);
        }

        Vec3 motion = this.getDeltaMovement();
        double nextYMotion;

        if (fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER) {
            double surfaceY = currentPos.getY() + fluid.getHeight(this.level(), currentPos) - 0.1D;
            if (this.getY() < surfaceY) {
                nextYMotion = 0.05D;
            } else {
                nextYMotion = 0.0D;
            }
        } else if (!this.onGround()) {
            nextYMotion = Math.max(-0.4D, motion.y - 0.04D);
        } else {
            nextYMotion = 0.0D;
        }

        this.setDeltaMovement(this.wanderVelocity.x, nextYMotion, this.wanderVelocity.z);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void handleFollowParent(int index) {
        Entity parent = this.level().getEntity(getParentId());

        if (parent == null || !parent.isAlive()) {
            this.discard();
            return;
        }

        WaterspoutEntity rootSpout = findRootSpout();
        if (rootSpout == null || !rootSpout.isAlive()) {
            this.discard();
            return;
        }

        BlockPos myPos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(myPos);
        BlockState blockState = this.level().getBlockState(myPos);

        if (fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA || blockState.is(Blocks.LAVA)) {
            boolean isSource = (fluid.getType() == Fluids.LAVA && fluid.isSource()) || blockState.is(Blocks.LAVA);
            this.level().setBlock(myPos, isSource ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState(), 3);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.2F);
            rootSpout.lifeTicks += 4;
            rootSpout.setEmpoweredByWater(false);
        } else if (fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER || this.isInWater()) {
            rootSpout.setEmpoweredByWater(true);
            if (this.tickCount % 2 == 0 && rootSpout.lifeTicks > 0) {
                rootSpout.lifeTicks--;
            }
        }

        this.setEmpoweredByWater(rootSpout.isEmpoweredByWater());

        float lifeRatio = Math.max(0.2F, 1.0F - ((float) rootSpout.lifeTicks / (float) rootSpout.maxLifeTicks));
        float waterMultiplier = this.isEmpoweredByWater() ? 2.0F : 1.0F;
        float finalScale = lifeRatio * waterMultiplier;

        double heightSpacing = 2.2D * Math.min(finalScale, 1.6D);
        double targetY = parent.getY() + heightSpacing;

        double swayRadius = (0.45D * index) * finalScale;
        float angle = (this.tickCount * 0.2F) + (index * 1.5F);
        double offsetX = Math.cos(angle) * swayRadius;
        double offsetZ = Math.sin(angle) * swayRadius;

        Vec3 targetPos = new Vec3(parent.getX() + offsetX, targetY, parent.getZ() + offsetZ);
        Vec3 nextPos = this.position().lerp(targetPos, 0.35D);

        this.setPos(nextPos.x, nextPos.y, nextPos.z);
    }

    private WaterspoutEntity findRootSpout() {
        Entity curr = this;
        while (curr instanceof WaterspoutEntity spout) {
            if (spout.getSegmentIndex() == 0) {
                return spout;
            }
            curr = spout.level().getEntity(spout.getParentId());
        }
        return null;
    }

    protected void applyVortexPull() {
        WaterspoutEntity root = (getSegmentIndex() == 0) ? this : findRootSpout();
        float lifeRatio = (root != null)
                ? Math.max(0.2F, 1.0F - ((float) root.lifeTicks / (float) root.maxLifeTicks))
                : 1.0F;

        float waterMultiplier = this.isEmpoweredByWater() ? 2.0F : 1.0F;
        double currentPullRadius = this.basePullRadius * lifeRatio * waterMultiplier;

        AABB pullBox = this.getBoundingBox().inflate(currentPullRadius, 1.5D, currentPullRadius);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                pullBox,
                e -> e.isAlive() && e != owner
        );

        Vec3 center = this.position();

        for (LivingEntity target : targets) {
            Vec3 toCenter = center.subtract(target.position());
            double distH = Math.sqrt(toCenter.x * toCenter.x + toCenter.z * toCenter.z);

            if (distH > 0.1D && distH <= currentPullRadius) {
                double falloff = 1.0D - (distH / currentPullRadius);
                double currentPull = this.basePullStrength * falloff;

                Vec3 horizontalPull = new Vec3(toCenter.x, 0.0D, toCenter.z).normalize().scale(currentPull);
                Vec3 pullVector = horizontalPull.add(0.0D, 0.04D * falloff, 0.0D);

                target.setDeltaMovement(target.getDeltaMovement().add(pullVector));
                target.hurtMarked = true;
            }
        }
    }

    protected void applyContactEffects() {
        AABB hitBox = this.getBoundingBox().inflate(0.6D);
        Entity owner = this.getOwner();

        List<LivingEntity> touchingEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                hitBox,
                e -> e.isAlive() && e != owner
        );

        if (touchingEntities.isEmpty()) return;

        for (LivingEntity target : touchingEntities) {
            int currentAir = target.getAirSupply();
            if (currentAir > 0) {
                target.setAirSupply(Math.max(0, currentAir - 20));
            }

            if (this.tickCount % 10 == 0) {
                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                    cap.setSnapshot(this.damageSnapshot);
                });

                float baseDamage = this.damageSnapshot.physical() > 0 ? this.damageSnapshot.physical() : 3.0F;
                if (this.isEmpoweredByWater()) {
                    baseDamage *= 2.0F;
                }

                target.hurt(this.damageSources().indirectMagic(this, owner), baseDamage);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SegmentIndex", getSegmentIndex());
        tag.putInt("ParentId", getParentId());
        tag.putBoolean("EmpoweredWater", isEmpoweredByWater());
        tag.putDouble("WanderX", this.wanderVelocity.x);
        tag.putDouble("WanderZ", this.wanderVelocity.z);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSegmentIndex(tag.getInt("SegmentIndex"));
        setParentId(tag.getInt("ParentId"));
        setEmpoweredByWater(tag.getBoolean("EmpoweredWater"));
        this.wanderVelocity = new Vec3(tag.getDouble("WanderX"), 0.0D, tag.getDouble("WanderZ"));
    }
}