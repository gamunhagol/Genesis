package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class GrabHolderEntity extends Entity {

    private UUID casterUUID = null;
    private UUID victimUUID = null;

    private int searchTicks;
    private int holdTicks;
    private int currentHoldAge = 0;

    private int escapeWindowTicks = 30;
    private int requiredEscapeCount;
    private int currentEscapeCount = 0;

    private double forwardOffset = 1.2D;
    private double heightOffset = -0.2D;

    private boolean isReleasing = false;

    @Nullable
    private Consumer<LivingEntity> onGrabbedListener;

    public GrabHolderEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public GrabHolderEntity(Level level, LivingEntity caster, int searchTicks, int holdTicks, int requiredEscapeCount) {
        super(GenesisEntities.GRAB_HOLDER.get(), level);
        this.noPhysics = true;
        this.casterUUID = caster.getUUID();
        this.searchTicks = searchTicks;
        this.holdTicks = holdTicks;
        this.requiredEscapeCount = requiredEscapeCount;

        this.updatePositionToCaster(caster);
    }

    public void setOnGrabbedListener(Consumer<LivingEntity> listener) {
        this.onGrabbedListener = listener;
    }

    public static boolean canGrab(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isPassenger()) {
            return false;
        }
        return target.getBbWidth() <= 1.2F && target.getBbHeight() <= 2.5F;
    }

    public boolean isReleasing() {
        return this.isReleasing;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        LivingEntity caster = getCaster();

        if (caster == null || !caster.isAlive() || caster.isRemoved()) {
            release();
            return;
        }

        updatePositionToCaster(caster);

        LivingEntity victim = getVictim();

        if (victim == null) {
            checkGrabCollision(caster);

            this.searchTicks--;
            if (this.searchTicks <= 0 && this.victimUUID == null) {
                this.discard();
            }
            return;
        }

        if (!victim.isAlive() || victim.isRemoved() || !victim.isPassenger()) {
            release();
            return;
        }

        if (this.escapeWindowTicks > 0) {
            this.escapeWindowTicks--;
        }

        this.currentHoldAge++;
        if (this.currentHoldAge >= this.holdTicks) {
            release();
        }
    }

    private void checkGrabCollision(LivingEntity caster) {
        AABB touchBox = this.getBoundingBox().inflate(0.6D);

        List<LivingEntity> touchingEntities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                touchBox,
                entity -> entity != caster && canGrab(entity)
        );

        LivingEntity target = touchingEntities.stream()
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);

        if (target != null) {
            this.victimUUID = target.getUUID();

            target.startRiding(this, true);
            caster.getPersistentData().putInt("GenesisActiveGrabHolderId", this.getId());

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

            if (this.onGrabbedListener != null) {
                this.onGrabbedListener.accept(target);
            }

            if (this.holdTicks <= 0) {
                release();
            }
        }
    }

    private void updatePositionToCaster(LivingEntity caster) {
        Vec3 look = caster.getLookAngle();
        Vec3 targetPos = caster.getEyePosition()
                .add(look.scale(this.forwardOffset))
                .add(0.0D, this.heightOffset, 0.0D);

        this.setPos(targetPos.x, targetPos.y, targetPos.z);
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            moveFunction.accept(passenger, this.getX(), this.getY(), this.getZ());
            passenger.setDeltaMovement(Vec3.ZERO);
        }
    }

    public boolean attemptEscape(LivingEntity victim) {
        if (this.escapeWindowTicks <= 0) {
            return false;
        }

        this.currentEscapeCount++;
        if (this.currentEscapeCount >= this.requiredEscapeCount) {
            release();
            return true;
        }

        return false;
    }

    public void release() {
        if (!this.level().isClientSide && !this.isReleasing) {
            this.isReleasing = true;

            LivingEntity caster = getCaster();
            if (caster != null) {
                caster.getPersistentData().remove("GenesisActiveGrabHolderId");
            }

            this.ejectPassengers();
            this.discard();
        }
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.casterUUID != null && this.level().getServer() != null) {
            Entity entity = this.level().getServer().getLevel(this.level().dimension()).getEntity(this.casterUUID);
            if (entity instanceof LivingEntity living) return living;
        }
        return null;
    }

    @Nullable
    public LivingEntity getVictim() {
        if (this.victimUUID != null && this.level().getServer() != null) {
            Entity entity = this.level().getServer().getLevel(this.level().dimension()).getEntity(this.victimUUID);
            if (entity instanceof LivingEntity living) return living;
        }
        return null;
    }

    @Override protected boolean canRide(Entity entity) { return true; }
    @Override public boolean isAttackable() { return false; }
    @Override public boolean isPickable() { return false; }
    @Override public boolean hurt(DamageSource source, float amount) { return false; }
    @Override protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("CasterUUID")) this.casterUUID = tag.getUUID("CasterUUID");
        if (tag.hasUUID("VictimUUID")) this.victimUUID = tag.getUUID("VictimUUID");
        this.searchTicks = tag.getInt("SearchTicks");
        this.holdTicks = tag.getInt("HoldTicks");
        this.currentHoldAge = tag.getInt("CurrentHoldAge");
        this.escapeWindowTicks = tag.getInt("EscapeWindowTicks");
        this.requiredEscapeCount = tag.getInt("RequiredEscapeCount");
        this.currentEscapeCount = tag.getInt("CurrentEscapeCount");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.casterUUID != null) tag.putUUID("CasterUUID", this.casterUUID);
        if (this.victimUUID != null) tag.putUUID("VictimUUID", this.victimUUID);
        tag.putInt("SearchTicks", this.searchTicks);
        tag.putInt("HoldTicks", this.holdTicks);
        tag.putInt("CurrentHoldAge", this.currentHoldAge);
        tag.putInt("EscapeWindowTicks", this.escapeWindowTicks);
        tag.putInt("RequiredEscapeCount", this.requiredEscapeCount);
        tag.putInt("CurrentEscapeCount", this.currentEscapeCount);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}