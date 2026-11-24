package com.gamunhagol.genesismod.world.capability.projectile;

import com.gamunhagol.genesismod.init.GenesisParticles;
import javax.annotation.Nullable;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;

public class TrailArrowPatch extends ArrowPatch implements ITrailProjectilePatch {

    @Nullable
    protected TrailInfo trailInfo = null;

    public TrailArrowPatch setTrail(TrailInfo trailInfo) {
        this.trailInfo = trailInfo;
        return this;
    }

    @Nullable
    @Override
    public TrailInfo getTrailInfo() {
        return this.trailInfo;
    }

    @Override
    public void onAddedToWorld() {
        if (this.trailInfo != null && this.original.level().isClientSide()) {
            double entityId = Double.longBitsToDouble((long) this.original.getId());
            this.original.level().addParticle(
                    GenesisParticles.PROJECTILE_TRAIL.get(), entityId, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D
            );
        }
    }
}