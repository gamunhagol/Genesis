package com.gamunhagol.genesismod.world.entity.projectile.miracles;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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

public abstract class AbstractStormSegment extends MagicEntity implements ItemSupplier {
    private static final EntityDataAccessor<Integer> DATA_PARENT_ID =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EMPOWERED_WATER =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ON_FIRE_SOURCE =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.BOOLEAN);

    protected double basePullRadius = 5.0D;
    protected double basePullStrength = 0.12D;
    protected double liftStrength = 0.08D;
    protected Vec3 wanderVelocity = Vec3.ZERO;
    protected int wanderInterval = 40;

    public AbstractStormSegment(EntityType<? extends AbstractStormSegment> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 400;
    }

    public AbstractStormSegment(EntityType<? extends AbstractStormSegment> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot, int parentId) {
        super(entityType, level, owner, snapshot);
        this.maxLifeTicks = 400;
        this.setParentId(parentId);

        if (isRoot()) {
            this.setNoGravity(false);
            this.noPhysics = false;
        } else {
            this.setNoGravity(true);
            this.noPhysics = true;
        }
    }

    public abstract boolean isRoot();
    public abstract double getVerticalSpacing();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PARENT_ID, -1);
        this.entityData.define(DATA_EMPOWERED_WATER, false);
        this.entityData.define(DATA_ON_FIRE_SOURCE, false);
    }

    public int getParentId() { return this.entityData.get(DATA_PARENT_ID); }
    public void setParentId(int id) { this.entityData.set(DATA_PARENT_ID, id); }

    public boolean isEmpoweredByWater() { return this.entityData.get(DATA_EMPOWERED_WATER); }
    public void setEmpoweredByWater(boolean val) { this.entityData.set(DATA_EMPOWERED_WATER, val); }

    public boolean isOnFireSource() { return this.entityData.get(DATA_ON_FIRE_SOURCE); }
    public void setOnFireSource(boolean val) { this.entityData.set(DATA_ON_FIRE_SOURCE, val); }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        if (!isRoot()) {
            this.lifeTicks = 0;
            AbstractStormSegment root = findRootSegment();
            if (root == null || !root.isAlive()) {
                this.discard();
                return;
            }
            this.setEmpoweredByWater(root.isEmpoweredByWater());
            this.setOnFireSource(root.isOnFireSource());
        }

        super.tick();

        if (!this.level().isClientSide) {
            if (isRoot()) {
                handleRootMovementAndFluids();
                applyVortexPhysics();
                applyDamage();
            } else {
                handleFollowParent();
            }
        }
    }

    private AbstractStormSegment findRootSegment() {
        Entity curr = this;
        while (curr instanceof AbstractStormSegment seg) {
            if (seg.isRoot()) {
                return seg;
            }
            curr = seg.level().getEntity(seg.getParentId());
        }
        return null;
    }

    private void handleRootMovementAndFluids() {
        BlockPos currentPos = this.blockPosition();
        FluidState fluid = this.level().getFluidState(currentPos);
        BlockState blockState = this.level().getBlockState(currentPos);

        boolean inWater = fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER || this.isInWater();
        boolean inLava = fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA || blockState.is(Blocks.LAVA);
        boolean inFire = blockState.is(Blocks.FIRE) || blockState.is(Blocks.SOUL_FIRE);

        this.setEmpoweredByWater(inWater);
        this.setOnFireSource(inLava || inFire);

        if (inWater && this.tickCount % 2 == 0 && this.lifeTicks > 0) {
            this.lifeTicks--;
        }

        if (this.tickCount % this.wanderInterval == 0 || this.wanderVelocity.lengthSqr() < 1.0E-4D) {
            float angle = this.random.nextFloat() * ((float) Math.PI * 2.0F);
            double speed = 0.06D + (this.random.nextDouble() * 0.04D);
            this.wanderVelocity = new Vec3(Math.cos(angle) * speed, 0.0D, Math.sin(angle) * speed);
        }

        Vec3 motion = this.getDeltaMovement();
        double nextYMotion;

        if (inWater || inLava) {
            double surfaceY = currentPos.getY() + fluid.getHeight(this.level(), currentPos) - 0.1D;
            if (this.getY() < surfaceY) {
                nextYMotion = 0.06D;
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

    private void handleFollowParent() {
        Entity parent = this.level().getEntity(getParentId());
        if (parent != null && parent.isAlive()) {
            AbstractStormSegment root = findRootSegment();
            float lifeRatio = (root != null)
                    ? Math.max(0.2F, 1.0F - ((float) root.lifeTicks / (float) root.maxLifeTicks))
                    : 1.0F;

            double targetY = parent.getY() + (getVerticalSpacing() * lifeRatio);
            Vec3 targetPos = new Vec3(parent.getX(), targetY, parent.getZ());
            Vec3 nextPos = this.position().lerp(targetPos, 0.4D);
            this.setPos(nextPos.x, nextPos.y, nextPos.z);
            this.setYRot(parent.getYRot() + 5.0F);
        }
    }

    private void applyVortexPhysics() {
        float lifeRatio = Math.max(0.2F, 1.0F - ((float) this.lifeTicks / (float) this.maxLifeTicks));
        float waterMultiplier = this.isEmpoweredByWater() ? 1.6F : 1.0F;
        double currentRadius = this.basePullRadius * lifeRatio * waterMultiplier;

        AABB vortexArea = this.getBoundingBox().inflate(currentRadius, 6.0D * lifeRatio, currentRadius);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                vortexArea,
                e -> e.isAlive() && e != owner
        );

        Vec3 center = this.position();

        for (LivingEntity target : targets) {
            Vec3 toCenter = center.subtract(target.position());
            double distH = Math.sqrt(toCenter.x * toCenter.x + toCenter.z * toCenter.z);

            if (distH <= currentRadius && distH > 0.1D) {
                double falloff = 1.0D - (distH / currentRadius);

                Vec3 pull = new Vec3(toCenter.x, 0, toCenter.z).normalize().scale(this.basePullStrength * falloff);
                Vec3 tangent = new Vec3(-toCenter.z, 0, toCenter.x).normalize().scale(0.08D * falloff);
                Vec3 lift = new Vec3(0, this.liftStrength * falloff, 0);

                Vec3 combined = pull.add(tangent).add(lift);
                target.setDeltaMovement(target.getDeltaMovement().add(combined));
                target.hurtMarked = true;
            }
        }
    }

    private void applyDamage() {
        AABB damageArea = this.getBoundingBox().inflate(this.basePullRadius * 0.7D, 6.0D, this.basePullRadius * 0.7D);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                damageArea,
                e -> e.isAlive() && e != owner
        );

        boolean fireState = this.isOnFireSource();

        for (LivingEntity target : targets) {
            if (fireState) {
                target.setSecondsOnFire(2);
                target.hurt(this.damageSources().inFire(), 1.0F);
            }

            if (this.tickCount % 10 == 0) {
                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));
                float damage = this.damageSnapshot.physical() > 0 ? this.damageSnapshot.physical() : 2.5F;
                if (this.isEmpoweredByWater()) {
                    damage *= 1.5F;
                }
                target.hurt(this.damageSources().indirectMagic(this, owner), damage);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ParentId", getParentId());
        tag.putBoolean("EmpoweredWater", isEmpoweredByWater());
        tag.putBoolean("OnFireSource", isOnFireSource());
        tag.putDouble("WanderX", this.wanderVelocity.x);
        tag.putDouble("WanderZ", this.wanderVelocity.z);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setParentId(tag.getInt("ParentId"));
        setEmpoweredByWater(tag.getBoolean("EmpoweredWater"));
        setOnFireSource(tag.getBoolean("OnFireSource"));
        this.wanderVelocity = new Vec3(tag.getDouble("WanderX"), 0.0D, tag.getDouble("WanderZ"));
    }
}