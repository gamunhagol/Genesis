package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.LargeArrowModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LargeArrowRenderer extends EntityRenderer<LargeArrowEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/projectiles/large_arrow.png");

    private final LargeArrowModel<LargeArrowEntity> model;

    public LargeArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        // 이전에 등록한 레이어에서 모델을 구워옵니다.
        this.model = new LargeArrowModel<>(context.bakeLayer(ModModelLayers.LARGE_ARROW_LAYER));
    }


    @Override
    public void render(LargeArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));


        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));

        // 흔들림 효과
        float shake = (float)entity.shakeTime - partialTicks;
        if (shake > 0.0F) {
            float f = -Mth.sin(shake * 3.0F) * shake;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f));
        }

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LargeArrowEntity entity) {
        return TEXTURE;
    }
}
