package com.gamunhagol.genesismod.world.entity.projectile;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class MagicEntity extends Projectile {
    protected DamageSnapshot damageSnapshot = DamageSnapshot.EMPTY;
    protected int lifeTicks = 0;
    protected int maxLifeTicks = 200;

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

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.lifeTicks++;
            if (this.lifeTicks >= this.maxLifeTicks) {
                this.discard();
                return;
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (!super.canHitEntity(entity)) return false;
        if (entity == this.getOwner()) return false;
        return entity instanceof LivingEntity;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("LifeTicks", this.lifeTicks);
        tag.putInt("MaxLifeTicks", this.maxLifeTicks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.lifeTicks = tag.getInt("LifeTicks");
        this.maxLifeTicks = tag.getInt("MaxLifeTicks");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}