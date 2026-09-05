package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueTModel extends Model {
    private final ModelPart root;
    private final ModelPart bb_main;

    public SOGStatueTModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 23).addBox(-3.0F, -9.9F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition moon_7_r1 = bb_main.addOrReplaceChild("moon_7_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, 5.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

        PartDefinition moon_6_r1 = bb_main.addOrReplaceChild("moon_6_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -14.1F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition moon_5_r1 = bb_main.addOrReplaceChild("moon_5_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition moon_4_r1 = bb_main.addOrReplaceChild("moon_4_r1", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, 3.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.1F, -14.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition moon_3_r1 = bb_main.addOrReplaceChild("moon_3_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, 4.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition moon_2_r1 = bb_main.addOrReplaceChild("moon_2_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, 5.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition pedestal_r1 = bb_main.addOrReplaceChild("pedestal_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7F, 2.0F, -1.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}