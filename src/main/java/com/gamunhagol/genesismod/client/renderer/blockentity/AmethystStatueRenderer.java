package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.client.model.block.AmethystStatueModel;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.entity.AmethystStatueBlockEntity;
import com.gamunhagol.genesismod.world.block.nature.AmethystStatueBlock;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class AmethystStatueRenderer implements BlockEntityRenderer<AmethystStatueBlockEntity> {
    private final AmethystStatueModel model;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GenesisMod.MODID, "textures/block/amethyst_statue.png");

    public AmethystStatueRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new AmethystStatueModel(context.bakeLayer(ModModelLayers.AM_STATUE_LAYER));
    }

    @Override
    public void render(AmethystStatueBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        Direction direction = entity.getBlockState().getValue(AmethystStatueBlock.FACING);

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        float rotation = switch (direction) {
            case NORTH -> 0f;
            case SOUTH -> 180f;
            case WEST  -> 270f;
            case EAST  -> 90f;
            default    -> 0f;
        };

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}