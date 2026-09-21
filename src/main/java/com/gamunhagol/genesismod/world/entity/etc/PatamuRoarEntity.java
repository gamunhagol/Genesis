package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PatamuRoarEntity extends Entity implements ItemSupplier {
    private static final int LIFETIME_TICKS = 60;
    private final int pulseInterval = 10;
    private final double radius = 8.0D;
    private final double pulseRepulsionStrength = 0.5D;

    private UUID ownerUUID = null;
    private DamageSnapshot snapshot = DamageSnapshot.EMPTY;

    public PatamuRoarEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setup(LivingEntity owner, DamageSnapshot snapshot) {
        this.ownerUUID = owner.getUUID();
        this.snapshot = snapshot;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            if (this.tickCount >= LIFETIME_TICKS) {
                this.discard();
                return;
            }

            if (this.tickCount % this.pulseInterval == 0) {
                applyPulse(serverLevel);
            }
        }
    }

    private void applyPulse(ServerLevel level) {
        LivingEntity owner = getOwner(level);
        if (owner == null) return;

        Vec3 center = this.position();
        AABB box = this.getBoundingBox().inflate(this.radius);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e.isAlive() && e != owner
        );

        float dotDamage = this.snapshot.physical() > 0 ? this.snapshot.physical() * 0.2F : 2.0F;

        for (LivingEntity target : targets) {
            Vec3 toTarget = target.position().subtract(center);
            double dist = toTarget.length();

            if (dist <= this.radius && dist > 0.01D) {
                Vec3 pushDir = toTarget.normalize().scale(this.pulseRepulsionStrength);
                target.setDeltaMovement(target.getDeltaMovement().add(pushDir.x, 0.1D, pushDir.z));
                target.hurtMarked = true;

                target.hurt(level.damageSources().indirectMagic(this, owner), dotDamage);
            }
        }
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