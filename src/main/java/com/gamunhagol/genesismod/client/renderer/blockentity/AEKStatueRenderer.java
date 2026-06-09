package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.client.model.block.AEKStatueModel;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.custom.AEKStatueBlock;
import com.gamunhagol.genesismod.world.block.entity.AEKStatueBlockEntity;
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

public class AEKStatueRenderer implements BlockEntityRenderer<AEKStatueBlockEntity> {
    private final AEKStatueModel model;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GenesisMod.MODID, "textures/block/ancient_elf_knight_statue.png");

    public AEKStatueRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new AEKStatueModel(context.bakeLayer(ModModelLayers.AEK_STATUE_LAYER));
    }

    @Override
    public void render(AEKStatueBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        Direction direction = entity.getBlockState().getValue(AEKStatueBlock.FACING);

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