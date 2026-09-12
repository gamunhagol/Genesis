package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.MagicBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GreatMagicPebble extends MagicBullet {
    public GreatMagicPebble(EntityType<? extends GreatMagicPebble> type, Level level) {
        super(type, level);
        this.homingStrength = 0.14f;
        this.homingRadius = 14.0D;
    }

    public GreatMagicPebble(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.GREAT_MAGIC_PEBBLE.get(), level, owner, snapshot);
        this.homingStrength = 0.14f;
        this.homingRadius = 14.0D;
    }

    @Override
    protected void spawnFlightParticles() {
        super.spawnFlightParticles();
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D, this.getY(), this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D, 0, 0, 0);
    }
}