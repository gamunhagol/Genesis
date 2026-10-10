package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.AbstractStormSegment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class StormSegment1Entity extends AbstractStormSegment {

    public StormSegment1Entity(EntityType<? extends StormSegment1Entity> type, Level level) {
        super(type, level);
    }

    public StormSegment1Entity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.STORM_SEGMENT_1.get(), level, owner, snapshot, -1);
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public double getVerticalSpacing() {
        return 0.0D;
    }
}