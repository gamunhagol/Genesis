package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.AbstractStormSegment;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class StormSegment2Entity extends AbstractStormSegment {

    public StormSegment2Entity(EntityType<? extends StormSegment2Entity> type, Level level) {
        super(type, level);
    }

    public StormSegment2Entity(Level level, LivingEntity owner, DamageSnapshot snapshot, int parentId) {
        super(GenesisEntities.STORM_SEGMENT_2.get(), level, owner, snapshot, parentId);
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public double getVerticalSpacing() {
        return 1.5D;
    }
}