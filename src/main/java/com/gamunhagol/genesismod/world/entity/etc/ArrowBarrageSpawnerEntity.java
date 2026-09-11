package com.gamunhagol.genesismod.world.entity.etc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class ArrowBarrageSpawnerEntity extends Entity {
    public enum Mode {
        RAIN,
        BOMBARDMENT
    }

    private static final int LIFETIME_TICKS = 200;
    private Mode mode = Mode.RAIN;
    private Vec3 shootDirection = new Vec3(0, -1, 0);
    private UUID ownerUUID = null;

    public ArrowBarrageSpawnerEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setup(LivingEntity owner, Mode mode, Vec3 shootDir) {
        this.ownerUUID = owner.getUUID();
        this.mode = mode;
        this.shootDirection = shootDir.normalize();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            if (this.tickCount >= LIFETIME_TICKS) {
                this.discard();
                return;
            }

            int spawnCount = this.random.nextInt(4); // 0, 1, 2, 3발 중 하나
            for (int i = 0; i < spawnCount; i++) {
                spawnSingleArrow(serverLevel);
            }
        }
    }

    private void spawnSingleArrow(ServerLevel level) {
        Vec3 spawnPos;
        Vec3 fireDir;

        double u = (this.random.nextDouble() - 0.5D) * 10.0D;
        double v = (this.random.nextDouble() - 0.5D) * 10.0D;

        if (this.mode == Mode.RAIN) {
            spawnPos = this.position().add(u, 0, v);
            fireDir = new Vec3(0, -1, 0);
        } else {
            Vec3 forward = this.shootDirection;

            Vec3 right = forward.cross(new Vec3(0, 1, 0));
            if (right.lengthSqr() < 1.0E-4D) {
                right = forward.cross(new Vec3(1, 0, 0));
            }
            right = right.normalize();

            Vec3 up = right.cross(forward).normalize();

            spawnPos = this.position().add(right.scale(u)).add(up.scale(v));
            fireDir = forward;
        }

        Arrow arrow = new Arrow(level, spawnPos.x, spawnPos.y, spawnPos.z);

        LivingEntity owner = getOwner(level);
        if (owner != null) {
            arrow.setOwner(owner);
        }

        arrow.setBaseDamage(3.0D);
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;

        arrow.shoot(fireDir.x, fireDir.y, fireDir.z, 3.0F, 1.0F);

        arrow.addTag("GenesisBarrageArrow");

        level.addFreshEntity(arrow);

        if (this.random.nextFloat() < 0.25F) {
            level.playSound(null, spawnPos.x, spawnPos.y, spawnPos.z,
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.8F, 0.9F + (this.random.nextFloat() * 0.2F));
        }
    }

    @Nullable
    private LivingEntity getOwner(ServerLevel level) {
        if (this.ownerUUID != null && level.getEntity(this.ownerUUID) instanceof LivingEntity living) {
            return living;
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
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickCount = tag.getInt("Age");
        if (tag.contains("Mode")) {
            this.mode = Mode.valueOf(tag.getString("Mode"));
        }
        this.shootDirection = new Vec3(tag.getDouble("DirX"), tag.getDouble("DirY"), tag.getDouble("DirZ"));
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.tickCount);
        tag.putString("Mode", this.mode.name());
        tag.putDouble("DirX", this.shootDirection.x);
        tag.putDouble("DirY", this.shootDirection.y);
        tag.putDouble("DirZ", this.shootDirection.z);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

