package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FallingStarBullet extends MagicBullet {

    public FallingStarBullet(EntityType<? extends FallingStarBullet> entityType, Level level) {
        super(entityType, level);
        this.weakGravity = 0.02D;
    }

    public FallingStarBullet(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.FALLING_STAR_BULLET.get(), level, owner, snapshot);
        this.weakGravity = 0.02D;
    }

    @Override
    protected Vec3 applyHoming(Vec3 currentMotion) {
        return currentMotion;
    }

    @Override
    protected void spawnFlightParticles() {
        this.level().addParticle(
                ParticleTypes.SOUL_FIRE_FLAME,
                this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D,
                this.getY(),
                this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D,
                0, 0.01D, 0
        );
    }
}