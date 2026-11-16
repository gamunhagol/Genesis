package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.StormSegment3Model;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.StormSegment3Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class StormSegment3Renderer extends EntityRenderer<StormSegment3Entity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("genesis", "textures/entity/projectiles/storm_segment_3.png");

    private final StormSegment3Model<StormSegment3Entity> model;

    public StormSegment3Renderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StormSegment3Model<>(context.bakeLayer(ModModelLayers.STORM_SEGMENT_3_LAYER));
    }

    @Override
    public void render(StormSegment3Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float scale = entity.getVisualScale();
        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D * scale, 0.0D);
        poseStack.scale(scale, scale, scale);

        float ageInTicks = (float) entity.tickCount + partialTicks;

        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);

        VertexConsumer baseConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, baseConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(StormSegment3Entity entity) {
        return TEXTURE;
    }
}
