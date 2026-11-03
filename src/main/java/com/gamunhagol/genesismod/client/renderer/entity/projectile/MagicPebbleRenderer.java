package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.MagicPebbleModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicPebble;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MagicPebbleRenderer extends EntityRenderer<MagicPebble> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/projectiles/magic_pebble.png");

    private final MagicPebbleModel<MagicPebble> model;

    public MagicPebbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new MagicPebbleModel<>(context.bakeLayer(ModModelLayers.MAGIC_PEBBLE_LAYER));
    }

    @Override
    public void render(MagicPebble entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);

        float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(180 + yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicPebble entity) {
        return TEXTURE;
    }
}
