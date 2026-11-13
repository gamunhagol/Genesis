package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.magic.child.DividedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class DividedRenderer extends EntityRenderer<DividedEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("genesis", "textures/entity/projectiles/divided.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    private static final float SIN_45 = (float) Math.sin(Math.PI / 4D);

    private final ModelPart cube;
    private final ModelPart glass;

    public DividedRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(ModelLayers.END_CRYSTAL);
        this.glass = modelPart.getChild("glass");
        this.cube = modelPart.getChild("cube");
    }

    @Override
    public void render(DividedEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
        poseStack.scale(2.0F, 2.0F, 2.0F);

        float rotationAngle = ((float) entity.tickCount + partialTicks) * 3.0F;
        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE);
        int overlay = OverlayTexture.NO_OVERLAY;

        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
        poseStack.mulPose((new Quaternionf()).setAngleAxis((float) (Math.PI / 3D), SIN_45, 0.0F, SIN_45));
        this.glass.render(poseStack, vertexconsumer, packedLight, overlay);

        poseStack.scale(0.875F, 0.875F, 0.875F);
        poseStack.mulPose((new Quaternionf()).setAngleAxis((float) (Math.PI / 3D), SIN_45, 0.0F, SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
        this.glass.render(poseStack, vertexconsumer, packedLight, overlay);

        poseStack.scale(0.875F, 0.875F, 0.875F);
        poseStack.mulPose((new Quaternionf()).setAngleAxis((float) (Math.PI / 3D), SIN_45, 0.0F, SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
        this.cube.render(poseStack, vertexconsumer, packedLight, overlay);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DividedEntity entity) {
        return TEXTURE;
    }
}
