package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.FallingStarBullet;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.GrandFallingStarBullet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class StarSeaSpawnerEntity extends Entity {
    private static final int LIFETIME_TICKS = 200;
    private UUID ownerUUID = null;

    private DamageSnapshot smallSnapshot = DamageSnapshot.EMPTY;
    private DamageSnapshot grandSnapshot = DamageSnapshot.EMPTY;

    public StarSeaSpawnerEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setup(LivingEntity owner, DamageSnapshot smallSnapshot, DamageSnapshot grandSnapshot) {
        this.ownerUUID = owner.getUUID();
        this.smallSnapshot = smallSnapshot;
        this.grandSnapshot = grandSnapshot;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            if (this.tickCount >= LIFETIME_TICKS) {
                this.discard();
                return;
            }

            LivingEntity owner = getOwner(serverLevel);

            if (owner == null || !owner.isAlive()) {
                this.discard();
                return;
            }

            if (this.random.nextFloat() < 0.75F) {
                spawnSmallStar(serverLevel, owner);
            }

            if (this.random.nextFloat() < 0.018F) {
                spawnGrandStar(serverLevel, owner);
            }
        }
    }

    private void spawnSmallStar(ServerLevel level, LivingEntity owner) {
        // 원래 크기인 12x12 범위로 복구
        double offsetX = (this.random.nextDouble() - 0.5D) * 12.0D;
        double offsetZ = (this.random.nextDouble() - 0.5D) * 12.0D;
        Vec3 spawnPos = this.position().add(offsetX, 0, offsetZ);

        double spreadX = (this.random.nextDouble() - 0.5D) * 0.7D;
        double spreadZ = (this.random.nextDouble() - 0.5D) * 0.7D;
        Vec3 shootDir = new Vec3(spreadX, -1.0D, spreadZ).normalize();

        FallingStarBullet bullet = new FallingStarBullet(level, owner, this.smallSnapshot);
        bullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        bullet.setDeltaMovement(shootDir.scale(0.75D));
        bullet.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.smallSnapshot));
        level.addFreshEntity(bullet);
    }

    private void spawnGrandStar(ServerLevel level, LivingEntity owner) {
        // 대형탄도 12x12 범위 유지
        double offsetX = (this.random.nextDouble() - 0.5D) * 12.0D;
        double offsetZ = (this.random.nextDouble() - 0.5D) * 12.0D;
        Vec3 spawnPos = this.position().add(offsetX, 0, offsetZ);

        double spreadX = (this.random.nextDouble() - 0.5D) * 0.4D;
        double spreadZ = (this.random.nextDouble() - 0.5D) * 0.4D;
        Vec3 shootDir = new Vec3(spreadX, -1.0D, spreadZ).normalize();

        GrandFallingStarBullet grandBullet = new GrandFallingStarBullet(level, owner, this.grandSnapshot);
        grandBullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        grandBullet.setDeltaMovement(shootDir.scale(0.45D));
        grandBullet.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.grandSnapshot));
        level.addFreshEntity(grandBullet);
    }

    @Nullable
    private LivingEntity getOwner(ServerLevel level) {
        if (this.ownerUUID != null && level.getEntity(this.ownerUUID) instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }
    @Override protected MovementEmission getMovementEmission() { return MovementEmission.NONE; }
    @Override protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}