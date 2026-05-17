package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueDModel extends Model {
    private final ModelPart root;

    // D 모델에 존재하는 파트들
    private final ModelPart wing;
    private final ModelPart bb_main;

    public SOGStatueDModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.wing = root.getChild("wing");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wing = partdefinition.addOrReplaceChild("wing", CubeListBuilder.create().texOffs(0, 38).addBox(4.9F, -15.6F, 2.7F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).mirror().addBox(-10.8F, -15.6F, 2.8F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition wing_3_r_r1 = wing.addOrReplaceChild("wing_3_r_r1", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-1.0F, -4.0F, -1.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.3F, -7.1F, 3.9F, 0.0F, 0.0F, -0.5236F));

        PartDefinition wing_1_r_r1 = wing.addOrReplaceChild("wing_1_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -4.0F, -1.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.7F, -15.6F, 3.5F, 0.0F, 0.0F, 0.4363F));

        PartDefinition wing_3_l_r1 = wing.addOrReplaceChild("wing_3_l_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.4F, -7.1F, 3.9F, 0.0F, 0.0F, 0.5236F));

        PartDefinition wing_1_l_r1 = wing.addOrReplaceChild("wing_1_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7F, -15.6F, 3.5F, 0.0F, 0.0F, -0.4363F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}