package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.FrozenStarModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.FrozenStarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FrozenStarRenderer extends EntityRenderer<FrozenStarEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/projectiles/frozen_star.png");

    private final FrozenStarModel<FrozenStarEntity> model;

    public FrozenStarRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FrozenStarModel<>(context.bakeLayer(ModModelLayers.FROZEN_STAR_LAYER));
    }

    @Override
    public void render(FrozenStarEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);

        float age = entity.tickCount + partialTicks;
        poseStack.mulPose(Axis.XP.rotationDegrees(age * 12.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(age * 16.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(age * 10.0F));

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FrozenStarEntity entity) {
        return TEXTURE;
    }
}
