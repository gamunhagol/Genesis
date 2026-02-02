package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.client.model.block.AEKStatueModel;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.AEKStatueBlock;
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
import net.minecraft.world.level.block.state.BlockState;

public class AEKStatueRenderer implements BlockEntityRenderer<AEKStatueBlockEntity> {
    private final AEKStatueModel model;
    // [중요] 텍스처 경로를 정확히 입력하세요!
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GenesisMod.MODID, "textures/block/ancient_elf_knight_statue.png");

    public AEKStatueRenderer(BlockEntityRendererProvider.Context context) {
        // Layer 등록 필요 (4단계 참조)
        this.model = new AEKStatueModel(context.bakeLayer(ModModelLayers.AEK_STATUE_LAYER));
    }

    @Override
    public void render(AEKStatueBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        Direction direction = entity.getBlockState().getValue(AEKStatueBlock.FACING);

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        // [수정] 각 방향에 따른 회전 각도를 수동으로 지정합니다.
        // 만약 시선이 여전히 반대라면 각 숫자에 180을 더하거나 빼보세요.
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