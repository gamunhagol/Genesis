package com.gamunhagol.genesismod.world.entity.projectile.miracles;

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

import java.util.List;

public abstract class AbstractStormSegment extends MagicEntity {
    private static final EntityDataAccessor<Integer> DATA_PARENT_ID =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EMPOWERED_WATER =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ON_FIRE_SOURCE =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_LIFE_TICKS =
            SynchedEntityData.defineId(AbstractStormSegment.class, EntityDataSerializers.INT);

    protected double basePullRadius = 5.0D;
    protected double basePullStrength = 0.12D;
    protected double liftStrength = 0.08D;

    protected int nextWanderChangeTick = 0;
    protected int movementMode = 0;
    protected float moveAngle = 0.0F;
    protected float angularVelocity = 0.0F;
    protected double moveSpeed = 0.15D;

    protected Vec3 segmentVelocity = Vec3.ZERO;
    protected float lateralBias = 0.0F;

    public AbstractStormSegment(EntityType<? extends AbstractStormSegment> entityType, Level level) {
        super(entityType, level);
        this.maxLifeTicks = 400;
        this.setMaxUpStep(1.1F);
    }

    public AbstractStormSegment(EntityType<? extends AbstractStormSegment> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot, int parentId) {
        super(entityType, level, owner, snapshot);
        this.maxLifeTicks = 400;
        this.setMaxUpStep(1.1F);
        this.setParentId(parentId);
        this.lateralBias = (this.random.nextFloat() - 0.5F) * 2.0F;
        this.moveAngle = this.random.nextFloat() * ((float) Math.PI * 2.0F);

        if (isRoot()) {
            this.setNoGravity(false);
            this.noPhysics = false;
        } else {
            this.setNoGravity(true);
            this.noPhysics = true;
        }
    }

    public abstract boolean isRoot();
    public abstract int getSegmentIndex();
    public abstract double getVerticalSpacing();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PARENT_ID, -1);
        this.entityData.define(DATA_EMPOWERED_WATER, false);
        this.entityData.define(DATA_ON_FIRE_SOURCE, false);
        this.entityData.define(DATA_LIFE_TICKS, 0);
    }

    public int getParentId() { return this.entityData.get(DATA_PARENT_ID); }
    public void setParentId(int id) { this.entityData.set(DATA_PARENT_ID, id); }

    public boolean isEmpoweredByWater() { return this.entityData.get(DATA_EMPOWERED_WATER); }
    public void setEmpoweredByWater(boolean val) { this.entityData.set(DATA_EMPOWERED_WATER, val); }

    public boolean isOnFireSource() { return this.entityData.get(DATA_ON_FIRE_SOURCE); }
    public void setOnFireSource(boolean val) { this.entityData.set(DATA_ON_FIRE_SOURCE, val); }

    public float getVisualScale() {
        AbstractStormSegment root = isRoot() ? this : findRootSegment();
        int currentLife = (root != null) ? root.entityData.get(DATA_LIFE_TICKS) : this.entityData.get(DATA_LIFE_TICKS);
        int maxLife = (root != null && root.maxLifeTicks > 0) ? root.maxLifeTicks : 400;

        float lifeRatio = Math.max(0.2F, 1.0F - ((float) currentLife / (float) maxLife));
        float waterMultiplier = this.isEmpoweredByWater() ? 1.6F : 1.0F;
        return lifeRatio * waterMultiplier;
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

        if (this.level().isClientSide) {
            spawnSpiralParticles(getSegmentIndex());
        } else {
            if (isRoot()) {
                handleRootMovementAndFluids();
                this.entityData.set(DATA_LIFE_TICKS, this.lifeTicks);
                applyVortexPhysics();
                applyDamage();
            } else {
                handleFollowParent();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnSpiralParticles(int index) {
        AbstractStormSegment root = isRoot() ? this : findRootSegment();
        if (root == null) return;

        float scale = this.getVisualScale();

        double[] segmentRadii = {0.55D, 0.85D, 1.10D, 1.40D};
        double baseRadius = (index >= 0 && index < segmentRadii.length)
                ? segmentRadii[index]
                : (0.55D + index * 0.28D);

        double segmentHeight = Math.max(this.getBbHeight(), 1.5D) * scale;

        Vec3 tiltOffset = Vec3.ZERO;
        if (!isRoot()) {
            Entity parent = this.level().getEntity(getParentId());
            if (parent != null) {
                tiltOffset = new Vec3(
                        (this.getX() - parent.getX()) * 0.08D,
                        0.0D,
                        (this.getZ() - parent.getZ()) * 0.08D
                );
            }
        }

        boolean isFireOrLava = root.isOnFireSource();
        boolean isWater = !isFireOrLava && root.isEmpoweredByWater();

        ParticleOptions particleType = isFireOrLava ? ParticleTypes.SMALL_FLAME : ParticleTypes.SMOKE;

        double baseAngle = (this.tickCount * 0.26D) + (index * 1.2D);
        double verticalStep = (this.tickCount % 4) * 0.25D;

        for (int arm = 0; arm < 4; arm++) {
            double armAngle = baseAngle + (arm * (Math.PI * 0.5D)) + (verticalStep * 0.9D);
            double spawnYOffset = verticalStep * segmentHeight;

            double currentRadius = (baseRadius + (verticalStep * 0.28D)) * scale;

            double rx = Math.cos(armAngle) * currentRadius;
            double rz = Math.sin(armAngle) * currentRadius;

            double px = this.getX() + rx;
            double py = this.getY() + spawnYOffset;
            double pz = this.getZ() + rz;

            double radialOutSpeed = 0.015D * scale;
            double tangentSpeed = 0.07D * scale;

            double vx = (-Math.sin(armAngle) * tangentSpeed) + (Math.cos(armAngle) * radialOutSpeed) + tiltOffset.x;
            double vy = (0.10D + (this.random.nextDouble() * 0.03D)) * scale;
            double vz = (Math.cos(armAngle) * tangentSpeed) + (Math.sin(armAngle) * radialOutSpeed) + tiltOffset.z;

            Particle particle = Minecraft.getInstance().particleEngine.createParticle(
                    particleType,
                    px, py, pz,
                    vx, vy, vz
            );

            if (particle != null) {
                int life = Math.max(5, (int) (13 * Math.min(scale, 1.5F)));
                particle.setLifetime(life);
                particle.scale(Math.max(0.5F, 1.0F * scale));

                if (!isFireOrLava) {
                    if (isWater) {
                        particle.setColor(0.22F, 0.62F, 0.96F);
                    } else {
                        particle.setColor(0.88F, 0.90F, 0.93F);
                    }
                }
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

        if (this.tickCount >= this.nextWanderChangeTick) {
            this.movementMode = this.random.nextInt(4);
            this.nextWanderChangeTick = this.tickCount + 10 + this.random.nextInt(25);

            switch (this.movementMode) {
                case 0 -> {
                    this.moveAngle = this.random.nextFloat() * ((float) Math.PI * 2.0F);
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

        this.setDeltaMovement(vx, nextYMotion, vz);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.horizontalCollision) {
            this.moveAngle += (float) Math.PI * (0.5F + this.random.nextFloat());
        }
    }

    private void handleFollowParent() {
        Entity parent = this.level().getEntity(getParentId());
        if (parent == null || !parent.isAlive()) {
            this.discard();
            return;
        }

        AbstractStormSegment root = findRootSegment();
        if (root == null || !root.isAlive()) {
            this.discard();
            return;
        }

        int index = getSegmentIndex();
        float finalScale = this.getVisualScale();

        double heightSpacing = getVerticalSpacing() * Math.min(finalScale, 1.6D);
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
        this.setYRot(parent.getYRot() + 5.0F);
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
        tag.putInt("MovementMode", this.movementMode);
        tag.putFloat("MoveAngle", this.moveAngle);
        tag.putFloat("AngularVelocity", this.angularVelocity);
        tag.putDouble("MoveSpeed", this.moveSpeed);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setParentId(tag.getInt("ParentId"));
        setEmpoweredByWater(tag.getBoolean("EmpoweredWater"));
        setOnFireSource(tag.getBoolean("OnFireSource"));
        this.movementMode = tag.getInt("MovementMode");
        this.moveAngle = tag.getFloat("MoveAngle");
        this.angularVelocity = tag.getFloat("AngularVelocity");
        this.moveSpeed = tag.getDouble("MoveSpeed");
    }
}
