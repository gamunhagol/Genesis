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

public class MagicComet extends MagicBullet {
    private static final EntityDataAccessor<Float> DATA_SCALE =
            SynchedEntityData.defineId(MagicComet.class, EntityDataSerializers.FLOAT);

    public MagicComet(EntityType<? extends MagicComet> type, Level level) {
        super(type, level);
        this.maxTurnRate = 0.03D;
        this.homingRadius = 32.0D;
    }

    public MagicComet(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_COMET.get(), level, owner, snapshot);
        this.maxTurnRate = 0.03D;
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
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.3D,
                    this.getY(),
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.3D,
                    0, 0, 0
            );
        }
    }
}
