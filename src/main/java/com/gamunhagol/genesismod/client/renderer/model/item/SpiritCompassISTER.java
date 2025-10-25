package com.gamunhagol.genesismod.client.renderer.model.item;

import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.OverlayTexture;

import javax.annotation.Nullable;

/**
 * 🎯 Spirit Compass 커스텀 ISTER
 * - Epic Fight 호환
 * - 나침반 각도 렌더링
 */
public class SpiritCompassISTER extends BlockEntityWithoutLevelRenderer {

    private final Minecraft minecraft = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;

    public SpiritCompassISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        this.itemRenderer = minecraft.getItemRenderer();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context,
                             PoseStack poseStack, MultiBufferSource buffer,
                             int light, int overlay) {

        ClientLevel level = minecraft.level;
        LivingEntity entity = minecraft.player;

        if (level == null || entity == null) return;

        // ✅ Forge 1.20.1에서는 이렇게 모델을 가져와야 함
        BakedModel model = itemRenderer.getModel(stack, level, entity, 0);

        // 🔹 나침반 회전 계산
        float angle = SpiritCompassItem.calculateCompassAngle(stack, level, entity);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        // 🔹 렌더링
        itemRenderer.render(stack, context, false, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, model);
        poseStack.popPose();
    }
}
