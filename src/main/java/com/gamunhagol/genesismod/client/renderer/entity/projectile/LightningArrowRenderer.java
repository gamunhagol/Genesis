package com.gamunhagol.genesismod.client.renderer.entity.projectile;

import com.gamunhagol.genesismod.client.model.entity.projectile.LightningArrowModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LightningArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LightningArrowRenderer extends EntityRenderer<LightningArrowEntity> {

    private static final ResourceLocation[] SPAWN_TEXTURES = new ResourceLocation[] {
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_0.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_1.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_2.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_3.png")
    };

    private static final ResourceLocation[] IDLE_TEXTURES = new ResourceLocation[] {
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_4.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_5.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_6.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_7.png")
    };

    private static final ResourceLocation[] HIT_TEXTURES = new ResourceLocation[] {
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_8.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_9.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_10.png"),
            new ResourceLocation("genesis", "textures/entity/projectiles/lightning_arrow_11.png")
    };

    private final LightningArrowModel<LightningArrowEntity> model;

    public LightningArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LightningArrowModel<>(context.bakeLayer(ModModelLayers.LIGHTNING_ARROW_LAYER));
    }

    @Override
    public void render(LightningArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);

        float yaw = entity.isHit() ? entity.getHitYaw() : Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = entity.isHit() ? entity.getHitPitch() : Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LightningArrowEntity entity) {
        int ticksPerFrame = 2;

        if (entity.isHit()) {
            int frameIndex = Math.min(entity.getHitTicks() / ticksPerFrame, HIT_TEXTURES.length - 1);
            return HIT_TEXTURES[frameIndex];
        }

        int spawnDuration = SPAWN_TEXTURES.length * ticksPerFrame;
        if (entity.tickCount < spawnDuration) {
            int frameIndex = entity.tickCount / ticksPerFrame;
            return SPAWN_TEXTURES[frameIndex];
        }

        int idleTicks = entity.tickCount - spawnDuration;
        int frameIndex = (idleTicks / ticksPerFrame) % IDLE_TEXTURES.length;
        return IDLE_TEXTURES[frameIndex];
    }
}
