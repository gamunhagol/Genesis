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
        initSettings();
    }

    public SmallMagicMeteor(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.SMALL_MAGIC_METEOR.get(), level, owner, snapshot);
        initSettings();
    }

    private void initSettings() {
        this.maxTurnRate = 0.24D;
        this.maxSpeed = 0.33D;
        this.homingRadius = 24.0D;
        this.homingDelayTicks = 4;
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
