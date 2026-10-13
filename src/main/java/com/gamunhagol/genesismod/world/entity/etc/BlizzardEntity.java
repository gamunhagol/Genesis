package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= this.lifetime) {
            this.discard();
            return;
        }

        double halfWidth = this.zoneSize / 2.0D;
        AABB area = new AABB(
                this.getX() - halfWidth, this.getY(), this.getZ() - halfWidth,
                this.getX() + halfWidth, this.getY() + this.zoneSize, this.getZ() + halfWidth
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
            spawnBlizzardParticles(halfWidth);
        }
    }

    private void spawnBlizzardParticles(double radius) {
        int particleCount = (int) (this.zoneSize * 2);

        for (int i = 0; i < particleCount; i++) {
            double progress = (double) i / particleCount;
            double angle = (this.tickCount * 0.25D) + (progress * Math.PI * 2.0D);
            double currentRadius = (radius * 0.4D) + (this.random.nextDouble() * radius * 0.6D);

            double px = this.getX() + Math.cos(angle) * currentRadius;
            double py = this.getY() + (this.random.nextDouble() * this.zoneSize);
            double pz = this.getZ() + Math.sin(angle) * currentRadius;

            double speed = 0.18D + (this.random.nextDouble() * 0.08D);
            double vx = -Math.sin(angle) * speed;
            double vy = (this.random.nextDouble() - 0.4D) * 0.05D;
            double vz = Math.cos(angle) * speed;

            this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
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

    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }
    @Override protected MovementEmission getMovementEmission() { return MovementEmission.NONE; }
    @Override protected void defineSynchedData() {}
    @Override protected void readAdditionalSaveData(CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}