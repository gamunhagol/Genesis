package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.AbstractStarAnomaly;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FaintStarEntity extends AbstractStarAnomaly {
    public FaintStarEntity(EntityType<? extends FaintStarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifespanTicks = 60;
        this.pullRadius = 3.0D;
        this.maxPullStrength = 0.02D;
        this.explosionPower = 4.0F;
    }

    public FaintStarEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.FAINT_STAR.get(), level, owner, snapshot);
        this.maxLifespanTicks = 60;
        this.pullRadius = 3.0D;
        this.maxPullStrength = 0.02D;
        this.explosionPower = 4.0F;
    }
}