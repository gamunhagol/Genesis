package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.HomingMeteorBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SmallMagicMeteor extends HomingMeteorBullet {
    public SmallMagicMeteor(EntityType<? extends SmallMagicMeteor> entityType, Level level) {
        super(entityType, level);
        this.homingStrength = 0.24f;
        this.dotLimit = -0.1D;
        this.closeCutoffDistance = 0.2D;
        this.weakGravity = 0.0D;
        this.maxSpeed = 0.33D;
    }

    public SmallMagicMeteor(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.SMALL_MAGIC_METEOR.get(), level, owner, snapshot);
        this.homingStrength = 0.24f;
        this.dotLimit = -0.1D;
        this.closeCutoffDistance = 0.2D;
        this.weakGravity = 0.0D;
        this.maxSpeed = 0.33D;
    }

    @Override
    protected void spawnFlightParticles() {
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                this.getX(),
                this.getY() + 0.05D,
                this.getZ(),
                0, 0.01D, 0);
    }
}