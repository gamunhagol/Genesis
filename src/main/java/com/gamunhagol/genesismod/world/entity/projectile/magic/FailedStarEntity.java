package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.AbstractStarAnomaly;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FailedStarEntity extends AbstractStarAnomaly {
    public FailedStarEntity(EntityType<? extends FailedStarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifespanTicks = 110;
        this.pullRadius = 5.0D;
        this.maxPullStrength = 0.06D;
        this.explosionPower = 6.0F;
    }

    public FailedStarEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.FAILED_STAR.get(), level, owner, snapshot);
        this.maxLifespanTicks = 110;
        this.pullRadius = 5.0D;
        this.maxPullStrength = 0.06D;
        this.explosionPower = 6.0F;
    }
}