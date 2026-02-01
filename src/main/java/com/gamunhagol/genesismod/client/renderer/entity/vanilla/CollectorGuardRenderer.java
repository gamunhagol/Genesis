package com.gamunhagol.genesismod.client.renderer.entity.vanilla;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CollectorGuardRenderer extends HumanoidMobRenderer<CollectorGuard, HumanoidModel<CollectorGuard>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(GenesisMod.MODID, "textures/entity/collector_guard.png");

    public CollectorGuardRenderer(EntityRendererProvider.Context pContext) {

        super(pContext, new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER)), 0.5f);

        this.addLayer(new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer<>(this,
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(pContext.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                pContext.getModelManager()));

    }

    @Override
    public ResourceLocation getTextureLocation(CollectorGuard pEntity) {
        return TEXTURE;
    }
}
