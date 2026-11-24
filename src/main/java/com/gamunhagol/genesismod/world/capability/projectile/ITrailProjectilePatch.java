package com.gamunhagol.genesismod.world.capability.projectile;

import javax.annotation.Nullable;
import yesman.epicfight.api.client.animation.property.TrailInfo;

public interface ITrailProjectilePatch {
    @Nullable
    TrailInfo getTrailInfo();
}
