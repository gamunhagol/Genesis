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

public class ShootingStar extends MagicBullet {
    private static final EntityDataAccessor<Float> DATA_SCALE =
            SynchedEntityData.defineId(ShootingStar.class, EntityDataSerializers.FLOAT);

    public ShootingStar(EntityType<? extends ShootingStar> type, Level level) {
        super(type, level);
        this.maxTurnRate = 0.03D;
        this.homingRadius = 32.0D;
    }

    public ShootingStar(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.SHOOTING_STAR.get(), level, owner, snapshot);
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

}
