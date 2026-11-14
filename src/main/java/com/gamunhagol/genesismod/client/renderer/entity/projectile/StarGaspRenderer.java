package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.StarGaspModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.StarGaspEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class StarGaspRenderer extends EntityRenderer<StarGaspEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("genesis", "textures/entity/projectiles/star_gasp/star_gasp.png");

    private static final ResourceLocation SWIRL_TEXTURE =
            new ResourceLocation("genesis", "textures/entity/projectiles/star_gasp/event_horizon.png");

    private final StarGaspModel<StarGaspEntity> model;

    public StarGaspRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StarGaspModel<>(context.bakeLayer(ModModelLayers.STAR_GASP_LAYER));
    }

    @Override
    public void render(StarGaspEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);

        float ageInTicks = (float) entity.tickCount + partialTicks;

        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);

        VertexConsumer baseConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, baseConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0625D * 1.2D, 0.0D);
        poseStack.scale(1.15F, 1.15F, 1.15F);

        float uOffset = ageInTicks * 0.01F;
        float vOffset = ageInTicks * 0.01F;
        VertexConsumer swirlConsumer = buffer.getBuffer(RenderType.energySwirl(SWIRL_TEXTURE, uOffset % 1.0F, vOffset % 1.0F));

        this.model.renderCubeSwirl(poseStack, swirlConsumer, 15728640, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(StarGaspEntity entity) {
        return TEXTURE;
    }
}
