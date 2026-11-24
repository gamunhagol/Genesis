package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;

import java.util.List;

public class WaterspoutEntity extends MagicEntity {

    private static final EntityDataAccessor<Integer> DATA_SEGMENT_INDEX =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PARENT_ID =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EMPOWERED_WATER =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_LIFE_TICKS =
            SynchedEntityData.defineId(WaterspoutEntity.class, EntityDataSerializers.INT);

    protected double basePullRadius = 6.5D;
    protected double basePullStrength = 0.15D;

    protected int nextWanderChangeTick = 0;
    protected int movementMode = 0;
    protected float moveAngle = 0.0F;
    protected float angularVelocity = 0.0F;
    protected double moveSpeed = 0.15D;

    protected Vec3 segmentVelocity = Vec3.ZERO;
    protected float lateralBias = 0.0F;

    public WaterspoutEntity(EntityType<? extends WaterspoutEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 600;
        this.setMaxUpStep(1.1F);
    }

    public WaterspoutEntity(EntityType<? extends WaterspoutEntity> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot, int segmentIndex, int parentId) {
        super(entityType, level, owner, snapshot);
        this.maxLifeTicks = 600;
        this.setMaxUpStep(1.1F);
        this.setSegmentIndex(segmentIndex);
        this.setParentId(parentId);
        this.lateralBias = (this.random.nextFloat() - 0.5F) * 2.0F;
        this.moveAngle = this.random.nextFloat() * ((float) Math.PI * 2F);

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
        this.entityData.define(DATA_LIFE_TICKS, 0);
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

    public float getVisualScale() {
        WaterspoutEntity root = (getSegmentIndex() == 0) ? this : findRootSpout();
        int currentLife = (root != null) ? root.entityData.get(DATA_LIFE_TICKS) : this.entityData.get(DATA_LIFE_TICKS);
        int maxLife = (root != null && root.maxLifeTicks > 0) ? root.maxLifeTicks : 600;

        float lifeRatio = Math.max(0.2F, 1.0F - ((float) currentLife / (float) maxLife));
        float waterMultiplier = this.isEmpoweredByWater() ? 2.0F : 1.0F;
        return lifeRatio * waterMultiplier;
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

        if (this.level().isClientSide) {
            spawnSpiralParticles(index);
        } else {
            if (index == 0) {
                handleRootMovementAndFluids();
                this.entityData.set(DATA_LIFE_TICKS, this.lifeTicks);
            } else {
                handleFollowParent(index);
            }

            applyVortexPull();
            applyContactEffects();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnSpiralParticles(int index) {
        WaterspoutEntity root = (index == 0) ? this : findRootSpout();
        if (root == null) return;

        float scale = this.getVisualScale();
        double baseRadius = (0.65D + (index * 0.22D)) * scale;
        double segmentHeight = (this.getBbHeight() * 0.85D) * scale;

        Vec3 tiltOffset = Vec3.ZERO;
        if (index > 0) {
            Entity parent = this.level().getEntity(getParentId());
            if (parent != null) {
                tiltOffset = new Vec3(
                        (this.getX() - parent.getX()) * 0.08D,
                        0.0D,
                        (this.getZ() - parent.getZ()) * 0.08D
                );
            }
        }

        BlockPos rootPos = root.blockPosition();
        BlockPos belowPos = rootPos.below();
        BlockState stateAt = this.level().getBlockState(rootPos);
        FluidState fluidAt = this.level().getFluidState(rootPos);
        BlockState stateBelow = this.level().getBlockState(belowPos);
        FluidState fluidBelow = this.level().getFluidState(belowPos);

        boolean isFireOrLava = fluidAt.is(FluidTags.LAVA) || stateAt.is(Blocks.LAVA) || stateAt.is(BlockTags.FIRE) || stateAt.is(Blocks.CAMPFIRE)
                || fluidBelow.is(FluidTags.LAVA) || stateBelow.is(Blocks.LAVA) || stateBelow.is(BlockTags.FIRE) || stateBelow.is(Blocks.CAMPFIRE);

        boolean isWater = !isFireOrLava && (root.isEmpoweredByWater()
                || fluidAt.is(FluidTags.WATER) || stateAt.is(Blocks.WATER)
                || fluidBelow.is(FluidTags.WATER) || stateBelow.is(Blocks.WATER));

        ParticleOptions particleType = isFireOrLava ? ParticleTypes.SMALL_FLAME : ParticleTypes.SMOKE;

        double baseAngle = (this.tickCount * 0.24D) + (index * 1.15D);
        double verticalStep = (this.tickCount % 4) * 0.25D;

        for (int arm = 0; arm < 4; arm++) {
            double armAngle = baseAngle + (arm * (Math.PI * 0.5D)) + (verticalStep * 0.9D);
            double spawnYOffset = verticalStep * segmentHeight;

            double rx = Math.cos(armAngle) * baseRadius;
            double rz = Math.sin(armAngle) * baseRadius;

            double px = this.getX() + rx;
            double py = this.getY() + spawnYOffset;
            double pz = this.getZ() + rz;

            double tangentSpeed = 0.06D * scale;
            double vx = (-Math.sin(armAngle) * tangentSpeed) + tiltOffset.x;
            double vy = (0.09D + (this.random.nextDouble() * 0.03D)) * scale;
            double vz = (Math.cos(armAngle) * tangentSpeed) + tiltOffset.z;

            Particle particle = Minecraft.getInstance().particleEngine.createParticle(
                    particleType,
                    px, py, pz,
                    vx, vy, vz
            );

            if (particle != null) {
                int life = Math.max(4, (int) (12 * Math.min(scale, 1.5F)));
                particle.setLifetime(life);
                particle.scale(Math.max(0.45F, 0.95F * scale));

                if (!isFireOrLava) {
                    if (isWater) {
                        particle.setColor(0.22F, 0.62F, 0.96F);
                    } else {
                        particle.setColor(0.92F, 0.94F, 0.96F);
                    }
                }
            }
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
        } else if (fluid.getType() == Fluids.WATER || fluid.getType() == Fluids.FLOWING_WATER || this.isInWater()) {
            this.setEmpoweredByWater(true);
            if (this.tickCount % 2 == 0 && this.lifeTicks > 0) {
                this.lifeTicks--;
            }
        } else {
            this.setEmpoweredByWater(false);
        }

        if (this.tickCount >= this.nextWanderChangeTick) {
            this.movementMode = this.random.nextInt(4);
            this.nextWanderChangeTick = this.tickCount + 10 + this.random.nextInt(25);

            switch (this.movementMode) {
                case 0 -> {
                    this.moveAngle = this.random.nextFloat() * ((float) Math.PI * 2F);
                    this.angularVelocity = 0.0F;
                    this.moveSpeed = 0.22D + (this.random.nextDouble() * 0.14D);
                }
                case 1 -> {
                    float dir = this.random.nextBoolean() ? 1.0F : -1.0F;
                    this.angularVelocity = dir * (0.14F + this.random.nextFloat() * 0.14F);
                    this.moveSpeed = 0.18D + (this.random.nextDouble() * 0.10D);
                }
                case 2 -> {
                    this.moveAngle += ((this.random.nextFloat() - 0.5F) * 2.5F);
                    this.angularVelocity = 0.0F;
                    this.moveSpeed = 0.20D + (this.random.nextDouble() * 0.12D);
                }
                case 3 -> {
                    this.moveAngle += (float) Math.PI * (0.6F + this.random.nextFloat() * 0.8F);
                    this.angularVelocity = (this.random.nextFloat() - 0.5F) * 0.1F;
                    this.moveSpeed = 0.03D;
                }
            }
        }

        if (this.movementMode == 2) {
            this.moveAngle += (float) Math.sin(this.tickCount * 0.45D) * 0.28F;
        } else if (this.movementMode == 3) {
            this.moveSpeed = Math.min(0.28D, this.moveSpeed + 0.025D);
            this.moveAngle += this.angularVelocity;
        } else {
            this.moveAngle += this.angularVelocity;
        }

        double vx = Math.cos(this.moveAngle) * this.moveSpeed;
        double vz = Math.sin(this.moveAngle) * this.moveSpeed;

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

        this.setDeltaMovement(vx, nextYMotion, vz);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.horizontalCollision) {
            this.moveAngle += (float) Math.PI * (0.5F + this.random.nextFloat());
        }
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
        Vec3 targetPos = new Vec3(parent.getX(), parent.getY() + heightSpacing, parent.getZ());
        Vec3 displacement = targetPos.subtract(this.position());

        double stiffness = Math.max(0.11D, 0.22D - (index * 0.018D));
        double damping = Math.min(0.76D, 0.64D + (index * 0.022D));

        double twistAngle = Math.sin((this.tickCount * 0.10D) + (index * 1.8D) + this.lateralBias) * (0.14D * index);
        double cosT = Math.cos(twistAngle);
        double sinT = Math.sin(twistAngle);

        double pulledX = displacement.x * cosT - displacement.z * sinT;
        double pulledZ = displacement.x * sinT + displacement.z * cosT;

        Vec3 springForce = new Vec3(
                pulledX * stiffness,
                displacement.y * 0.32D,
                pulledZ * stiffness
        );

        this.segmentVelocity = this.segmentVelocity.add(springForce);
        this.segmentVelocity = new Vec3(
                this.segmentVelocity.x * damping,
                this.segmentVelocity.y * 0.65D,
                this.segmentVelocity.z * damping
        );

        this.setDeltaMovement(this.segmentVelocity);
        this.move(MoverType.SELF, this.getDeltaMovement());
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

                DamageSource source = this.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY)
                        .filter(patch -> patch instanceof ProjectilePatch<?>)
                        .map(patch -> (DamageSource) ((ProjectilePatch<?>) patch).createEpicFightDamageSource())
                        .orElseGet(() -> this.damageSources().indirectMagic(this, owner));

                target.hurt(source, baseDamage);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SegmentIndex", getSegmentIndex());
        tag.putInt("ParentId", getParentId());
        tag.putBoolean("EmpoweredWater", isEmpoweredByWater());
        tag.putInt("MovementMode", this.movementMode);
        tag.putFloat("MoveAngle", this.moveAngle);
        tag.putFloat("AngularVelocity", this.angularVelocity);
        tag.putDouble("MoveSpeed", this.moveSpeed);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSegmentIndex(tag.getInt("SegmentIndex"));
        setParentId(tag.getInt("ParentId"));
        setEmpoweredByWater(tag.getBoolean("EmpoweredWater"));
        this.movementMode = tag.getInt("MovementMode");
        this.moveAngle = tag.getFloat("MoveAngle");
        this.angularVelocity = tag.getFloat("AngularVelocity");
        this.moveSpeed = tag.getDouble("MoveSpeed");
    }
}
