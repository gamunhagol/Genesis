package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import yesman.epicfight.world.damagesource.StunType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FrostFogEntity extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FrostFogEntity.class, EntityDataSerializers.FLOAT);

    private int durationTicks = 400;
    private int damageInterval = 10;
    private float radius = 3.0F;
    private float height = 1.5F;

    private DamageSnapshot fogSnapshot = DamageSnapshot.EMPTY;

    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public FrostFogEntity(EntityType<? extends FrostFogEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public FrostFogEntity(Level level, @Nullable LivingEntity owner, DamageSnapshot fogSnapshot) {
        this(GenesisEntities.FROST_FOG.get(), level);
        this.setOwner(owner);
        this.fogSnapshot = fogSnapshot;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner != null ? owner.getUUID() : null;
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity living) {
                this.owner = living;
            }
        }
        return this.owner;
    }

    public void setFogSnapshot(DamageSnapshot fogSnapshot) {
        this.fogSnapshot = fogSnapshot;
    }

    public void setDurationTicks(int durationTicks) {
        this.durationTicks = durationTicks;
    }

    public void setDamageInterval(int damageInterval) {
        this.damageInterval = Math.max(1, damageInterval);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        if (!this.level().isClientSide) {
            this.entityData.set(DATA_RADIUS, radius);
        }
    }

    public float getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_RADIUS, 3.0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnFogParticles();
            return;
        }

        if (this.tickCount >= this.durationTicks) {
            this.discard();
            return;
        }

        if (this.tickCount % this.damageInterval == 0) {
            applyFogDamage();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnFogParticles() {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        float currentRadius = this.getRadius();
        double centerX = this.getX();
        double centerY = this.getY();
        double centerZ = this.getZ();

        int snowCount = 4;
        for (int i = 0; i < snowCount; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            double dist = Math.sqrt(this.random.nextDouble()) * currentRadius;
            double px = centerX + Math.cos(angle) * dist;
            double py = centerY + 0.2D + this.random.nextDouble() * this.height;
            double pz = centerZ + Math.sin(angle) * dist;

            double vx = (this.random.nextDouble() - 0.5D) * 0.04D;
            double vy = -0.02D - this.random.nextDouble() * 0.02D;
            double vz = (this.random.nextDouble() - 0.5D) * 0.04D;

            this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
        }

        if (this.tickCount % 2 == 0) {
            int smokeCount = 2;
            for (int i = 0; i < smokeCount; i++) {
                double angle = this.random.nextDouble() * Math.PI * 2.0D;
                double dist = Math.sqrt(this.random.nextDouble()) * currentRadius;
                double px = centerX + Math.cos(angle) * dist;
                double py = centerY + 0.4D + this.random.nextDouble() * (this.height * 0.8D);
                double pz = centerZ + Math.sin(angle) * dist;

                double vx = (this.random.nextDouble() - 0.5D) * 0.015D;
                double vy = -0.05D;
                double vz = (this.random.nextDouble() - 0.5D) * 0.015D;

                Particle smoke = engine.createParticle(
                        ParticleTypes.CLOUD,
                        px, py, pz,
                        vx, vy, vz
                );

                if (smoke != null) {
                    smoke.setColor(0.95F, 0.98F, 1.0F);
                    smoke.scale(1.6F + this.random.nextFloat() * 0.6F);
                    smoke.setLifetime(35 + this.random.nextInt(20));
                }
            }
        }
    }

    private void applyFogDamage() {
        LivingEntity caster = this.getOwner();
        float r = this.radius;

        AABB box = new AABB(
                this.getX() - r, this.getY() - 0.5D, this.getZ() - r,
                this.getX() + r, this.getY() + this.height, this.getZ() + r
        );

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class, box, e -> e.isAlive() && e != caster
        );

        double radiusSq = r * r;
        for (LivingEntity target : targets) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            if ((dx * dx + dz * dz) > radiusSq) continue;

            if (caster != null) {
                GenesisDamageCalculator.applySnapshotDamage(target, caster, this.fogSnapshot, 0.0F, StunType.NONE);
            } else {
                target.hurt(this.damageSources().freeze(), this.fogSnapshot.frost());
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.durationTicks = tag.getInt("DurationTicks");
        this.damageInterval = Math.max(1, tag.getInt("DamageInterval"));
        if (tag.contains("Radius")) {
            this.setRadius(tag.getFloat("Radius"));
        }
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("DurationTicks", this.durationTicks);
        tag.putInt("DamageInterval", this.damageInterval);
        tag.putFloat("Radius", this.radius);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
