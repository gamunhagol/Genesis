package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.client.model.block.SentinelStatueModel;
import com.gamunhagol.genesismod.world.block.StatueBlock; // [중요] StatueBlock import 필요
import com.gamunhagol.genesismod.world.block.entity.StatueBlockEntity;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction; // [중요] Direction import
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState; // [중요] BlockState import

public class StatueBlockRenderer implements BlockEntityRenderer<StatueBlockEntity> {
    private final SentinelStatueModel model;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("genesis", "textures/block/statue_of_sentinel_of_oblivion.png");

    public StatueBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SentinelStatueModel(context.bakeLayer(ModModelLayers.SENTINEL_LAYER));
    }

    @Override
    public void render(StatueBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        // 1. 블럭의 현재 방향 가져오기
        BlockState blockState = entity.getBlockState();
        Direction direction = blockState.getValue(StatueBlock.FACING);

        // 2. 위치 잡기 (중앙)
        poseStack.translate(0.5D, 1.5D, 0.5D);

        // 3. 상하 반전 (Java 모델 특성)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        // 4. [핵심] 방향에 따라 Y축 회전
        // direction.toYRot()은 북쪽 기준 각도를 줍니다. 반대 방향으로 돌려야 딱 맞습니다.
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

        // 5. 그리기
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}