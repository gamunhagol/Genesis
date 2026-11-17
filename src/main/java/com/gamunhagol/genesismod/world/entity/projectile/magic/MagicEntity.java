package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public abstract class MagicEntity extends Projectile {
    private static final EntityDataAccessor<Boolean> DATA_HIT = SynchedEntityData.defineId(MagicEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_HIT_YAW = SynchedEntityData.defineId(MagicEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_HIT_PITCH = SynchedEntityData.defineId(MagicEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_CHARGING = SynchedEntityData.defineId(MagicEntity.class, EntityDataSerializers.BOOLEAN);

    protected DamageSnapshot damageSnapshot = DamageSnapshot.EMPTY;
    protected int lifeTicks = 0;
    protected int maxLifeTicks = 1200;
    protected int hitTicks = 0;
    protected int maxHitTicks = 8;

    public MagicEntity(EntityType<? extends MagicEntity> entityType, Level level) {
        super(entityType, level);
    }

    public MagicEntity(EntityType<? extends MagicEntity> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        this(entityType, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
        this.damageSnapshot = snapshot;
    }

    public void setDamageSnapshot(DamageSnapshot snapshot) {
        this.damageSnapshot = snapshot;
    }

    public DamageSnapshot getDamageSnapshot() {
        return this.damageSnapshot;
    }

    public void setMaxLifeTicks(int ticks) {
        this.maxLifeTicks = ticks;
    }

    public void setMaxHitTicks(int ticks) {
        this.maxHitTicks = ticks;
    }

    public boolean isHit() {
        return this.entityData.get(DATA_HIT);
    }

    public float getHitYaw() {
        return this.entityData.get(DATA_HIT_YAW);
    }

    public float getHitPitch() {
        return this.entityData.get(DATA_HIT_PITCH);
    }

    public int getHitTicks() {
        return this.hitTicks;
    }

    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(DATA_CHARGING, charging);
    }

    protected void triggerHit(Vec3 hitPos) {
        this.updateRotation();
        float yaw = this.getYRot();
        float pitch = this.getXRot();

        this.setPos(hitPos.x, hitPos.y, hitPos.z);
        this.entityData.set(DATA_HIT_YAW, yaw);
        this.entityData.set(DATA_HIT_PITCH, pitch);
        this.entityData.set(DATA_HIT, true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HIT, false);
        this.entityData.define(DATA_HIT_YAW, 0.0F);
        this.entityData.define(DATA_HIT_PITCH, 0.0F);
        this.entityData.define(DATA_CHARGING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isCharging()) {
            if (!this.level().isClientSide) {
                Entity owner = this.getOwner();
                if (!(owner instanceof LivingEntity livingOwner) || !livingOwner.isAlive() || !livingOwner.isUsingItem()) {
                    this.discard();
                }
            }
            return;
        }

        this.lifeTicks++;

        if (this.isHit()) {
            this.setYRot(this.getHitYaw());
            this.setXRot(this.getHitPitch());
            this.yRotO = this.getHitYaw();
            this.xRotO = this.getHitPitch();

            this.hitTicks++;
            if (!this.level().isClientSide && this.hitTicks >= this.maxHitTicks) {
                this.discard();
            }
            return;
        }

        if (!this.level().isClientSide) {
            if (this.lifeTicks >= this.maxLifeTicks) {
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.isCharging()) return false;
        if (!super.canHitEntity(entity)) return false;
        if (entity == this.getOwner()) return false;
        return entity instanceof LivingEntity;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTicks", this.lifeTicks);
        tag.putInt("MaxLifeTicks", this.maxLifeTicks);
        tag.putBoolean("IsHit", this.isHit());
        tag.putFloat("HitYaw", this.getHitYaw());
        tag.putFloat("HitPitch", this.getHitPitch());
        tag.putInt("HitTicks", this.hitTicks);
        tag.putBoolean("IsCharging", this.isCharging());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTicks = tag.getInt("LifeTicks");
        this.maxLifeTicks = tag.getInt("MaxLifeTicks");
        this.entityData.set(DATA_HIT, tag.getBoolean("IsHit"));
        this.entityData.set(DATA_HIT_YAW, tag.getFloat("HitYaw"));
        this.entityData.set(DATA_HIT_PITCH, tag.getFloat("HitPitch"));
        this.hitTicks = tag.getInt("HitTicks");
        this.entityData.set(DATA_CHARGING, tag.getBoolean("IsCharging"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnIdleParticles() {
    }
}
