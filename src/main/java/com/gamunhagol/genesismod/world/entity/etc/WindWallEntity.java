package com.gamunhagol.genesismod.world.entity.etc;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class WindWallEntity extends Entity {
    private static final EntityDataAccessor<Float> WIND_DIR_X = SynchedEntityData.defineId(WindWallEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> WIND_DIR_Z = SynchedEntityData.defineId(WindWallEntity.class, EntityDataSerializers.FLOAT);

    private int maxLifeTicks = 200;
    private double wallWidth = 6.0D;
    private double wallHeight = 4.0D;
    private double windDepth = 13.89D;
    private double windStrength = 0.52D;

    public WindWallEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setup(Vec3 horizontalDirection, int durationTicks) {
        Vec3 dir = new Vec3(horizontalDirection.x, 0, horizontalDirection.z).normalize();
        this.entityData.set(WIND_DIR_X, (float) dir.x);
        this.entityData.set(WIND_DIR_Z, (float) dir.z);
        this.maxLifeTicks = durationTicks;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(WIND_DIR_X, 0.0F);
        this.entityData.define(WIND_DIR_Z, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= this.maxLifeTicks) {
            this.discard();
            return;
        }

        Vec3 windDir = new Vec3(this.entityData.get(WIND_DIR_X), 0, this.entityData.get(WIND_DIR_Z));
        if (windDir.lengthSqr() < 1.0E-4D) return;

        if (this.level().isClientSide) {
            spawnWindParticles(windDir);
        } else {
            applyWindCurrent(windDir);
        }
    }

    private void applyWindCurrent(Vec3 windDir) {
        Vec3 right = new Vec3(-windDir.z, 0, windDir.x).normalize();

        double halfWidth = this.wallWidth / 2.0D;
        Vec3 center = this.position();

        AABB zone = new AABB(
                center.x - halfWidth, center.y, center.z - halfWidth,
                center.x + halfWidth, center.y + this.wallHeight, center.z + halfWidth
        ).inflate(this.windDepth);

        List<Entity> affectedEntities = this.level().getEntitiesOfClass(
                Entity.class,
                zone,
                e -> e != this && e.isAlive()
        );

        for (Entity target : affectedEntities) {
            Vec3 toTarget = target.position().subtract(center);

            double forwardDist = toTarget.dot(windDir);
            double lateralDist = Math.abs(toTarget.dot(right));

            if (forwardDist >= 0.0D && forwardDist <= this.windDepth && lateralDist <= halfWidth) {
                double falloff = 1.0D - (forwardDist / this.windDepth);
                double currentStrength = this.windStrength * falloff;

                Vec3 windVelocity = windDir.scale(currentStrength);

                target.setDeltaMovement(target.getDeltaMovement().add(windVelocity));
                target.hasImpulse = true;

                if (!(target instanceof Projectile)) {
                    target.hurtMarked = true;
                }
            }
        }
    }

    private void spawnWindParticles(Vec3 windDir) {
        Vec3 right = new Vec3(-windDir.z, 0, windDir.x).normalize();

        int count = 2;
        for (int i = 0; i < count; i++) {
            double offsetLateral = (this.random.nextDouble() - 0.5D) * this.wallWidth;
            double offsetHeight = this.random.nextDouble() * this.wallHeight;

            Vec3 spawnPos = this.position()
                    .add(right.scale(offsetLateral))
                    .add(0, offsetHeight, 0);

            double speed = 0.15D + this.random.nextDouble() * 0.1D;
            Vec3 pSpeed = windDir.scale(speed);

            this.level().addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    spawnPos.x, spawnPos.y, spawnPos.z,
                    pSpeed.x, 0.01D, pSpeed.z
            );

            if (this.random.nextFloat() < 0.4F) {
                this.level().addParticle(
                        ParticleTypes.POOF,
                        spawnPos.x, spawnPos.y, spawnPos.z,
                        pSpeed.x * 0.5D, 0.0D, pSpeed.z * 0.5D
                );
            }
        }
    }

    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        this.entityData.set(WIND_DIR_X, tag.getFloat("DirX"));
        this.entityData.set(WIND_DIR_Z, tag.getFloat("DirZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putFloat("DirX", this.entityData.get(WIND_DIR_X));
        tag.putFloat("DirZ", this.entityData.get(WIND_DIR_Z));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}