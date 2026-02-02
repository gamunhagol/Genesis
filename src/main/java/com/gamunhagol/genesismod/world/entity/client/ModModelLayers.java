package com.gamunhagol.genesismod.world.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation COLLECTOR_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "collector"), "main");
    public static final ModelLayerLocation COLLECTOR_GUARD_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "collector_guard"), "main");

    public static final ModelLayerLocation SENTINEL_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "statue_of_sentinel"), "main");
    public static final ModelLayerLocation AEK_STATUE_LAYER = new ModelLayerLocation(
            new ResourceLocation("genesis", "aek_statue"), "main");
}
