package com.gamunhagol.genesismod.client.renderer.entity.vanilla;

import com.gamunhagol.genesismod.client.model.entity.CollectorModel;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CollectorRenderer extends MobRenderer<Collector, CollectorModel<Collector>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/collector.png");

    public CollectorRenderer(EntityRendererProvider.Context context) {
        super(context, new CollectorModel<>(context.bakeLayer(CollectorModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Collector entity) {
        return TEXTURE;
    }
}
