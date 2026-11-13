package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GrandFallingStarBullet extends MagicBullet {

    public GrandFallingStarBullet(EntityType<? extends GrandFallingStarBullet> entityType, Level level) {
        super(entityType, level);
    }

    public GrandFallingStarBullet(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.GRAND_FALLING_STAR_BULLET.get(), level, owner, snapshot);
    }

    @Override
    protected Vec3 applyHoming(Vec3 currentMotion) {
        return currentMotion;
    }

    @Override
    protected void spawnFlightParticles() {
    }
}
