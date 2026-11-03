package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GreatMagicPebble extends MagicBullet {
    private static final EntityDataAccessor<Float> DATA_SCALE =
            SynchedEntityData.defineId(GreatMagicPebble.class, EntityDataSerializers.FLOAT);

    public GreatMagicPebble(EntityType<? extends GreatMagicPebble> type, Level level) {
        super(type, level);
        this.maxTurnRate = 0.09D;
        this.homingRadius = 32.0D;
    }

    public GreatMagicPebble(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.GREAT_MAGIC_PEBBLE.get(), level, owner, snapshot);
        this.maxTurnRate = 0.09D;
        this.homingRadius = 32.0D;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SCALE, 1.0F);
    }

    public void setScale(float scale) {
        this.entityData.set(DATA_SCALE, scale);
    }

    public float getScale() {
        return this.entityData.get(DATA_SCALE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("ProjectileScale", this.getScale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ProjectileScale")) {
            this.setScale(tag.getFloat("ProjectileScale"));
        }
    }

    @Override
    protected void spawnFlightParticles() {
        super.spawnFlightParticles();
        this.level().addParticle(
                ParticleTypes.SOUL_FIRE_FLAME,
                this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D,
                this.getY(),
                this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D,
                0, 0, 0
        );
    }
}
