package com.gamunhagol.genesismod.client.renderer.entity.etc;

import com.gamunhagol.genesismod.client.model.entity.etc.RootSpearModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.etc.child.RootSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RootSpearRenderer extends EntityRenderer<RootSpearEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/etc/root_spear.png");

    private final RootSpearModel<RootSpearEntity> model;

    public RootSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RootSpearModel<>(context.bakeLayer(ModModelLayers.ROOT_SPEAR_LAYER));
    }

    @Override
    public void render(RootSpearEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float age = entity.tickCount + partialTicks;
        float progress = Mth.clamp(age / (float) entity.getRiseDuration(), 0.0F, 1.0F);
        float exposure = entity.getMaxExposureRatio() * progress;
        float localOffset = -entity.getModelHeight() * (1.0F - exposure);

        float shakeX = 0.0F;
        float shakeY = 0.0F;
        float shakeZ = 0.0F;
        if (progress < 1.0F) {
            shakeX = Mth.sin(age * 3.5F) * 0.03F;
            shakeY = Mth.sin(age * 5.0F) * 0.045F;
            shakeZ = Mth.cos(age * 3.2F) * 0.03F;
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getRootYaw()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getRootTilt()));
        poseStack.translate(shakeX, localOffset + shakeY, shakeZ);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RootSpearEntity entity) {
        return TEXTURE;
    }
}
