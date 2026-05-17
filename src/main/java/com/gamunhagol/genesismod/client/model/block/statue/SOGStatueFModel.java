package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueFModel extends Model {
    private final ModelPart root;

    // F 모델에 존재하는 파트들
    private final ModelPart horn;
    private final ModelPart bb_main;

    public SOGStatueFModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.horn = root.getChild("horn");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition horn = partdefinition.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 38).addBox(7.4F, -20.6F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.05F))
                .texOffs(0, 38).mirror().addBox(-9.5F, -20.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition horn_5_r_r1 = horn.addOrReplaceChild("horn_5_r_r1", CubeListBuilder.create().texOffs(8, 42).mirror().addBox(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-9.4F, -21.6F, 0.0F, 0.0F, 0.0F, 1.0297F));

        PartDefinition horn_4_r_r1 = horn.addOrReplaceChild("horn_4_r_r1", CubeListBuilder.create().texOffs(0, 46).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.7F, -21.1F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition horn_3_r_r1 = horn.addOrReplaceChild("horn_3_r_r1", CubeListBuilder.create().texOffs(0, 46).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.9F, -18.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition horn_1_r_r1 = horn.addOrReplaceChild("horn_1_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.15F)).mirror(false), PartPose.offsetAndRotation(-8.5F, -14.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition horn_5_l_r1 = horn.addOrReplaceChild("horn_5_l_r1", CubeListBuilder.create().texOffs(8, 42).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(9.3F, -21.7F, 0.0F, 0.0F, 0.0F, -1.0297F));

        PartDefinition horn_4_l_r1 = horn.addOrReplaceChild("horn_4_l_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8F, -21.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition horn_3_l_r1 = horn.addOrReplaceChild("horn_3_l_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.8F, -18.1F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition horn_1_l_r1 = horn.addOrReplaceChild("horn_1_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(8.4F, -14.1F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}