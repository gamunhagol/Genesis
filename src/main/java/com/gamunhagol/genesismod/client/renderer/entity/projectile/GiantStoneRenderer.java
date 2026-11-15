package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.GiantStoneModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LargeRockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GiantStoneRenderer extends EntityRenderer<LargeRockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/projectiles/giant_stone.png");

    private final GiantStoneModel<LargeRockEntity> model;

    public GiantStoneRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GiantStoneModel<>(context.bakeLayer(ModModelLayers.GIANT_STONE_LAYER));
    }

    @Override
    public void render(LargeRockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LargeRockEntity entity) {
        return TEXTURE;
    }
}
