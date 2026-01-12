package com.gamunhagol.genesismod.world.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation COLLECTOR_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "collector"), "main");
    public static final ModelLayerLocation COLLECTOR_GUARD_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "collector_guard"), "main");
}
