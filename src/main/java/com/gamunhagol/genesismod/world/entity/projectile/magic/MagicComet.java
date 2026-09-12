package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MagicComet extends MagicBullet {
    public MagicComet(EntityType<? extends MagicComet> type, Level level) {
        super(type, level);
        this.homingStrength = 0.1f;
        this.homingRadius = 16.0D;
    }

    public MagicComet(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_COMET.get(), level, owner, snapshot);
        this.homingStrength = 0.1f;
        this.homingRadius = 16.0D;
    }

    @Override
    protected void spawnFlightParticles() {
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * 0.3D, this.getY(), this.getZ() + (this.random.nextDouble() - 0.5D) * 0.3D, 0, 0, 0);
        }
    }
}