package com.gamunhagol.genesismod.world.entity.etc;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class AbstractRootEntity extends LivingEntity {
    protected int lifeTicks = 0;
    protected int maxLifeTicks = 400;
    protected UUID ownerUUID = null;
    protected DamageSnapshot damageSnapshot = DamageSnapshot.EMPTY;

    public AbstractRootEntity(EntityType<? extends AbstractRootEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractRootEntity(EntityType<? extends AbstractRootEntity> entityType, Level level, @Nullable LivingEntity owner, DamageSnapshot snapshot) {
        this(entityType, level);
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
        }
        this.damageSnapshot = snapshot;
    }

    public static AttributeSupplier.Builder createRootAttributes(float maxHp) {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, maxHp)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.ownerUUID = owner != null ? owner.getUUID() : null;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Nullable
    public LivingEntity getOwnerEntity() {
        if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    public void setDamageSnapshot(DamageSnapshot snapshot) {
        this.damageSnapshot = snapshot;
    }

    public void setMaxLifeTicks(int ticks) {
        this.maxLifeTicks = ticks;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(0.0D, motion.y - 0.08D, 0.0D);
        }

        if (!this.level().isClientSide) {
            AABB triggerBox = this.getBoundingBox().inflate(0.15D, 0.3D, 0.15D);
            List<LivingEntity> insideEntities = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    triggerBox,
                    this::isValidTarget
            );

            for (LivingEntity target : insideEntities) {
                onEntityContact(target);
            }

            this.lifeTicks++;
            if (this.lifeTicks >= this.maxLifeTicks) {
                this.discard();
            }
        }
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {}

    @Override
    public void push(double x, double y, double z) {}

    @Override
    public void knockback(double pStrength, double pX, double pZ) {}

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        this.setDeltaMovement(Vec3.ZERO);
        this.hurtMarked = false;
        return result;
    }

    protected boolean isValidTarget(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isSpectator()) {
            return false;
        }
        if (this.ownerUUID != null && target.getUUID().equals(this.ownerUUID)) {
            return false;
        }
        if (target instanceof AbstractRootEntity) {
            return false;
        }
        return true;
    }

    protected abstract void onEntityContact(LivingEntity target);

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTicks", this.lifeTicks);
        tag.putInt("MaxLifeTicks", this.maxLifeTicks);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTicks = tag.getInt("LifeTicks");
        this.maxLifeTicks = tag.getInt("MaxLifeTicks");
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}