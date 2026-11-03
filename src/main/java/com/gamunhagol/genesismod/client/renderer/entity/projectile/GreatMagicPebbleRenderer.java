package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.GreatMagicPebbleModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.GreatMagicPebble;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GreatMagicPebbleRenderer extends EntityRenderer<GreatMagicPebble> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation("genesis", "textures/entity/projectiles/great_magic_pebble_0.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/great_magic_pebble_1.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/great_magic_pebble_2.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/great_magic_pebble_3.png")
    };

    private final GreatMagicPebbleModel<GreatMagicPebble> model;

    public GreatMagicPebbleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new GreatMagicPebbleModel<>(context.bakeLayer(ModModelLayers.GREAT_MAGIC_PEBBLE_LAYER));
    }

    @Override
    public void render(GreatMagicPebble entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float scale = entity.getScale();
        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
        poseStack.scale(scale, scale, scale);

        float yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GreatMagicPebble entity) {
        int ticksPerFrame = 3;
        int frameIndex = (entity.tickCount / ticksPerFrame) % TEXTURES.length;
        return TEXTURES[frameIndex];
    }
}
