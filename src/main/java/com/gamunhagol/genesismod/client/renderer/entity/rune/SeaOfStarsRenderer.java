package com.gamunhagol.genesismod.client.renderer.entity.rune;

import com.gamunhagol.genesismod.client.model.entity.rune.FloorRuneBModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.SeaOfStarsEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SeaOfStarsRenderer extends EntityRenderer<SeaOfStarsEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("genesis", "textures/entity/rune/sea_of_stars.png");

    private final FloorRuneBModel<SeaOfStarsEntity> model;

    public SeaOfStarsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FloorRuneBModel<>(context.bakeLayer(ModModelLayers.FLOOR_RUNE_B_LAYER));
    }

    @Override
    public void render(SeaOfStarsEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float rotation = (entity.tickCount + partialTicks) * 0.8F;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaOfStarsEntity entity) {
        return TEXTURE;
    }
}