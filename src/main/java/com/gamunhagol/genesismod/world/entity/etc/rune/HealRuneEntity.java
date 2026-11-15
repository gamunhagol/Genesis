package com.gamunhagol.genesismod.world.entity.etc.rune;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class HealRuneEntity extends Entity {
    private static final EntityDataAccessor<Integer> RUNE_TYPE = SynchedEntityData.defineId(HealRuneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_LIFE = SynchedEntityData.defineId(HealRuneEntity.class, EntityDataSerializers.INT);

    public HealRuneEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RUNE_TYPE, 0);
        this.entityData.define(MAX_LIFE, 40);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount >= getMaxLife()) {
            this.discard();
        }
    }

    public int getRuneType() {
        return this.entityData.get(RUNE_TYPE);
    }

    public void setRuneType(int runeType) {
        this.entityData.set(RUNE_TYPE, runeType);
    }

    public int getMaxLife() {
        return this.entityData.get(MAX_LIFE);
    }

    public void setMaxLife(int maxLife) {
        this.entityData.set(MAX_LIFE, maxLife);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("RuneType")) setRuneType(compound.getInt("RuneType"));
        if (compound.contains("MaxLife")) setMaxLife(compound.getInt("MaxLife"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("RuneType", getRuneType());
        compound.putInt("MaxLife", getMaxLife());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
