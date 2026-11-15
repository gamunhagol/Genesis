package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.BigStoneModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.RockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BigStoneRenderer extends EntityRenderer<RockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/projectiles/big_stone.png");

    private final BigStoneModel<RockEntity> model;

    public BigStoneRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BigStoneModel<>(context.bakeLayer(ModModelLayers.BIG_STONE_LAYER));
    }

    @Override
    public void render(RockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RockEntity entity) {
        return TEXTURE;
    }
}
