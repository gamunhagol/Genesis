package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.client.model.block.SentinelStatueModel;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.StatueBlock;
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
            new ResourceLocation(GenesisMod.MODID, "textures/block/statue_of_sentinel_of_oblivion.png");

    public StatueBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SentinelStatueModel(context.bakeLayer(ModModelLayers.SENTINEL_LAYER));
    }

    @Override
    public void render(StatueBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        // 1. 블럭의 현재 방향 가져오기 (StatueBlock.FACING 사용)
        BlockState blockState = entity.getBlockState();
        Direction direction = blockState.getValue(StatueBlock.FACING);

        // 2. 위치 잡기 (중앙)
        poseStack.translate(0.5D, 1.5D, 0.5D);

        // 3. Java 모델의 기본 상하 반전 적용
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        // 4. [수정 포인트] 방향 순서가 꼬일 때 여기서 수동으로 순서를 바꿉니다.
        // 현재 남북이 반대이고 동서가 제각각이라면, 아래 숫자들을 서로 바꿔보며 맞출 수 있습니다.
        float rotation = switch (direction) {
            case NORTH -> 0f; // 만약 뒤통수가 보이면 0f로 수정
            case SOUTH -> 180f;   // 만약 뒤통수가 보이면 180f로 수정
            case WEST  -> 90f;  // 만약 옆면이 보이면 270f 등으로 수정
            case EAST  -> 270f; // 만약 옆면이 보이면 90f 등으로 수정
            default    -> 0f;
        };

        // 마이너스(-) 부호를 붙여 마인크래프트 회전 방향(시계/반시계)을 맞춥니다.
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}