package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class EmbroideredVeilModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "embroidered_veil"), "main");

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(GenesisMod.MODID, "textures/models/armor/embroidered_layer_1.png");

    public EmbroideredVeilModel(ModelPart root) {
        super(root, RenderType::entityTranslucent);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("helmet_main", CubeListBuilder.create()
                        .texOffs(0, 90).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 90).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        VertexConsumer translucentBuffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.itemEntityTranslucentCull(TEXTURE_LOCATION));

        super.renderToBuffer(poseStack, translucentBuffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}