package com.gamunhagol.genesismod.client.renderer.blockentity.statue;

import com.gamunhagol.genesismod.client.model.block.statue.SOGStatueCModel;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.custom.statue.StatueBaseBlock;
import com.gamunhagol.genesismod.world.block.entity.statue.GodStatueGenericBlockEntity;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class SOGStatueCRenderer implements BlockEntityRenderer<GodStatueGenericBlockEntity> {
    private final SOGStatueCModel model;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GenesisMod.MODID, "textures/block/statue_of_god_c.png");

    public SOGStatueCRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SOGStatueCModel(context.bakeLayer(ModModelLayers.STATUE_GOD_C_LAYER));
    }

    @Override
    public void render(GodStatueGenericBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        Direction direction = entity.getBlockState().getValue(StatueBaseBlock.FACING);

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        float rotation = switch (direction) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST  -> 90f;
            case EAST  -> 270f;
            default    -> 0f;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}