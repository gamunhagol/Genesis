package com.gamunhagol.genesismod.world.entity.base;

import java.util.UUID;
import javax.annotation.Nullable;

public interface ISummonable {
    @Nullable
    UUID getOwnerUUID();
    void setOwnerUUID(@Nullable UUID uuid);

    float getUpkeepCost();
}
