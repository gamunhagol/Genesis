package com.gamunhagol.genesismod.client.renderer.blockentity;

import com.gamunhagol.genesismod.world.block.entity.MistVaultBlockEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MistVaultRenderer implements BlockEntityRenderer<MistVaultBlockEntity> {
    public MistVaultRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(MistVaultBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // 내부에 보여줄 아이템 (사바나의 안개 코어)
        ItemStack stack = new ItemStack(GenesisItems.MISY_CORE_1.get());

        poseStack.pushPose();
        // 1. 블록 정중앙으로 이동 (Y값은 취향껏 조절)
        poseStack.translate(0.5, 0.4, 0.5);
        // 2. 아이템 크기 조절 (블록보다 작게)
        poseStack.scale(0.5f, 0.5f, 0.5f);

        // 3. 아이템 렌더링 (회전 없이 정적 렌더링)
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                packedOverlay,
                poseStack,
                buffer,
                be.getLevel(),
                0
        );
        poseStack.popPose();
    }
}