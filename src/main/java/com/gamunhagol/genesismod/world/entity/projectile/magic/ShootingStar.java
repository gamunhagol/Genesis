package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ShootingStar extends MagicBullet {
    public ShootingStar(EntityType<? extends ShootingStar> type, Level level) {
        super(type, level);
        this.homingStrength = 0.1f;
        this.homingRadius = 20.0D;
    }

    public ShootingStar(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.SHOOTING_STAR.get(), level, owner, snapshot);
        this.homingStrength = 0.1f;
        this.homingRadius = 20.0D;
    }

    @Override
    protected void spawnFlightParticles() {
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * 0.4D, this.getY(), this.getZ() + (this.random.nextDouble() - 0.5D) * 0.4D, 0, 0, 0);
        }
    }
}