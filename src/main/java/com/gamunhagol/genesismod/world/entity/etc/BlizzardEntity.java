package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BlizzardEntity extends Entity {
    private static final int DEFAULT_LIFETIME = 600;
    private static final EntityDataAccessor<Float> DATA_ZONE_SIZE = SynchedEntityData.defineId(BlizzardEntity.class, EntityDataSerializers.FLOAT);

    private float zoneSize = 6.0F;
    private int lifetime = DEFAULT_LIFETIME;
    private UUID ownerUUID = null;
    private DamageSnapshot frostSnapshot = DamageSnapshot.EMPTY;

    public BlizzardEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public BlizzardEntity(Level level, double x, double y, double z) {
        this(GenesisEntities.BLIZZARD.get(), level);
        this.setPos(x, y, z);
    }

    public void setup(LivingEntity owner, DamageSnapshot snapshot, int lifetimeTicks, float size) {
        this.ownerUUID = owner.getUUID();
        this.frostSnapshot = snapshot;
        this.lifetime = lifetimeTicks;
        this.zoneSize = size;
        this.entityData.set(DATA_ZONE_SIZE, size);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= this.lifetime) {
            this.discard();
            return;
        }

        float currentSize = this.level().isClientSide ? this.entityData.get(DATA_ZONE_SIZE) : this.zoneSize;
        double halfWidth = currentSize / 2.0D;

        AABB area = new AABB(
                this.getX() - halfWidth, this.getY(), this.getZ() - halfWidth,
                this.getX() + halfWidth, this.getY() + currentSize, this.getZ() + halfWidth
        );

        LivingEntity owner = getOwner();

        List<LivingEntity> insideEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive() && e != owner
        );

        for (LivingEntity target : insideEntities) {
            target.setIsInPowderSnow(true);

            if (!this.level().isClientSide) {
                if (target.canFreeze()) {
                    int currentFrozen = target.getTicksFrozen();
                    target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze(), currentFrozen + 2));
                }

                if (this.tickCount % 20 == 0 && owner != null && !this.frostSnapshot.isEmpty()) {
                    GenesisDamageCalculator.applySnapshotDamage(target, owner, this.frostSnapshot);
                }
            }
        }

        if (this.level().isClientSide) {
            spawnBlizzardParticles(halfWidth, currentSize);
        }
    }

    private void spawnBlizzardParticles(double maxRadius, float currentSize) {
        int outerCount = (int) (currentSize * 10);
        for (int i = 0; i < outerCount; i++) {
            double h = this.random.nextDouble();
            double distFromCenterY = Math.abs(h - 0.5D) * 2.0D;
            double curveFactor = 0.45D + (0.55D * Math.pow(distFromCenterY, 1.8D));
            double currentRadius = maxRadius * curveFactor * (0.85D + this.random.nextDouble() * 0.2D);
            double angle = (this.random.nextDouble() * Math.PI * 2.0D) + (this.tickCount * 0.65D);

            double px = this.getX() + Math.cos(angle) * currentRadius;
            double py = this.getY() + (h * currentSize);
            double pz = this.getZ() + Math.sin(angle) * currentRadius;

            double speed = 0.35D + (this.random.nextDouble() * 0.15D);
            double vx = -Math.sin(angle) * speed;
            double vy = 0.05D + (this.random.nextDouble() * 0.08D);
            double vz = Math.cos(angle) * speed;

            if (this.random.nextFloat() < 0.7F) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
            } else {
                this.level().addParticle(ParticleTypes.CLOUD, px, py, pz, vx * 0.3D, vy * 0.5D, vz * 0.3D);
            }
        }

        int coreCount = (int) (currentSize * 3);
        for (int i = 0; i < coreCount; i++) {
            double h = this.random.nextDouble();
            double coreRadius = maxRadius * (0.05D + this.random.nextDouble() * 0.17D);
            double angle = (this.random.nextDouble() * Math.PI * 2.0D) + (this.tickCount * 0.9D);

            double px = this.getX() + Math.cos(angle) * coreRadius;
            double py = this.getY() + (h * currentSize);
            double pz = this.getZ() + Math.sin(angle) * coreRadius;

            double speed = 0.25D + (this.random.nextDouble() * 0.1D);
            double vx = -Math.sin(angle) * speed;
            double vy = 0.12D + (this.random.nextDouble() * 0.1D);
            double vz = Math.cos(angle) * speed;

            if (this.random.nextBoolean()) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
            } else {
                this.level().addParticle(ParticleTypes.WHITE_ASH, px, py, pz, vx, vy, vz);
            }
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ZONE_SIZE, 6.0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
