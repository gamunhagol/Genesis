package com.gamunhagol.genesismod.client.renderer.entity.rune;

import com.gamunhagol.genesismod.client.model.entity.rune.FloorRuneAModel;
import com.gamunhagol.genesismod.client.model.entity.rune.FloorRuneBModel;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.gamunhagol.genesismod.world.entity.etc.rune.HealRuneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HealRuneRenderer extends EntityRenderer<HealRuneEntity> {
    public static final ResourceLocation AREA_HEAL_TEXTURE = new ResourceLocation("genesis", "textures/entity/rune/area_heal.png");
    public static final ResourceLocation GREAT_HEAL_TEXTURE = new ResourceLocation("genesis", "textures/entity/rune/great_heal.png");
    public static final ResourceLocation AREA_GREAT_HEAL_TEXTURE = new ResourceLocation("genesis", "textures/entity/rune/area_great_heal.png");

    private final FloorRuneAModel<HealRuneEntity> modelA;
    private final FloorRuneBModel<HealRuneEntity> modelB;

    public HealRuneRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.modelA = new FloorRuneAModel<>(context.bakeLayer(ModModelLayers.FLOOR_RUNE_A_LAYER));
        this.modelB = new FloorRuneBModel<>(context.bakeLayer(ModModelLayers.FLOOR_RUNE_B_LAYER));
    }

    @Override
    public void render(HealRuneEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        int type = entity.getRuneType();
        float targetScale = switch (type) {
            case 1 -> 1.0F;
            case 2 -> 3.0F;
            default -> 2.5F;
        };

        float age = entity.tickCount + partialTicks;
        float growTicks = 5.0F;
        float progress = Mth.clamp(age / growTicks, 0.0F, 1.0F);
        float currentScale = Mth.lerp(progress, 0.1F, targetScale);

        poseStack.scale(currentScale, 1.0F, currentScale);

        float maxLife = entity.getMaxLife();
        float fadeTicks = 10.0F;
        float fadeStart = maxLife - fadeTicks;
        float alpha = 1.0F;
        if (age > fadeStart) {
            alpha = 1.0F - Mth.clamp((age - fadeStart) / fadeTicks, 0.0F, 1.0F);
        }

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentCull(this.getTextureLocation(entity)));
        if (type == 0) {
            this.modelA.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        } else {
            this.modelB.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HealRuneEntity entity) {
        return switch (entity.getRuneType()) {
            case 1 -> GREAT_HEAL_TEXTURE;
            case 2 -> AREA_GREAT_HEAL_TEXTURE;
            default -> AREA_HEAL_TEXTURE;
        };
    }
}
