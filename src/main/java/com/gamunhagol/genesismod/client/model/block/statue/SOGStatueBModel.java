package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueBModel extends Model {
    private final ModelPart root;

    private final ModelPart crown;
    private final ModelPart broach;
    private final ModelPart bb_main;

    public SOGStatueBModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.crown = root.getChild("crown");
        this.broach = root.getChild("broach");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition crown = partdefinition.addOrReplaceChild("crown", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition crown_6_r_r1 = crown.addOrReplaceChild("crown_6_r_r1", CubeListBuilder.create().texOffs(8, 34).mirror().addBox(-1.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 34).addBox(3.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -13.25F, -1.25F, 0.4363F, 0.0F, 0.0F));

        PartDefinition crown_5_r_r1 = crown.addOrReplaceChild("crown_5_r_r1", CubeListBuilder.create().texOffs(8, 34).mirror().addBox(-1.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 34).addBox(7.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -12.25F, -1.25F, 0.5672F, 0.0F, 0.0F));

        PartDefinition crown_4_r_r1 = crown.addOrReplaceChild("crown_4_r_r1", CubeListBuilder.create().texOffs(8, 34).mirror().addBox(-1.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, -12.2F, -0.65F, 0.1745F, 0.0F, -0.4363F));

        PartDefinition crown_3_r_r1 = crown.addOrReplaceChild("crown_3_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -4.8F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -13.2F, 1.0F, 0.0F, 0.0F, -0.7418F));

        PartDefinition crown_2_r_r1 = crown.addOrReplaceChild("crown_2_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -13.2F, 2.0F, -0.542F, 0.7729F, -1.0349F));

        PartDefinition crown_1_r_r1 = crown.addOrReplaceChild("crown_1_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 34).addBox(5.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -12.0F, 2.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition crown_7_r1 = crown.addOrReplaceChild("crown_7_r1", CubeListBuilder.create().texOffs(8, 41).addBox(-1.0F, -6.8F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.25F, -1.25F, 0.2618F, 0.0F, 0.0F));

        PartDefinition crown_4_l_r1 = crown.addOrReplaceChild("crown_4_l_r1", CubeListBuilder.create().texOffs(8, 34).addBox(-1.0F, -5.8F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -12.2F, -0.65F, 0.1745F, 0.0F, 0.4363F));

        PartDefinition crown_3_l_r1 = crown.addOrReplaceChild("crown_3_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, -4.8F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -13.2F, 1.0F, 0.0F, 0.0F, 0.7418F));

        PartDefinition crown_2_l_r1 = crown.addOrReplaceChild("crown_2_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -13.2F, 2.0F, -0.542F, -0.7729F, 1.0349F));

        PartDefinition broach = partdefinition.addOrReplaceChild("broach", CubeListBuilder.create().texOffs(0, 40).addBox(2.0F, -18.0F, 2.6F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 40).addBox(6.0F, -19.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 45).addBox(4.8F, -20.7F, -2.6F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 40).addBox(3.0F, -19.5F, -5.2F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 40).addBox(1.0F, -21.0F, -4.6F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 45).addBox(-1.0F, -23.4F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 40).mirror().addBox(-4.0F, -18.0F, 2.6F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(0, 40).mirror().addBox(-8.0F, -19.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(0, 45).mirror().addBox(-6.8F, -20.7F, -2.6F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(0, 40).mirror().addBox(-5.0F, -19.5F, -5.2F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(0, 40).mirror().addBox(-3.0F, -21.0F, -4.6F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition broach_2_r_r1 = broach.addOrReplaceChild("broach_2_r_r1", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-5.9F, -15.4F, 3.3F, 0.0F, -0.6109F, 0.0F));

        PartDefinition broach_2_l_r1 = broach.addOrReplaceChild("broach_2_l_r1", CubeListBuilder.create().texOffs(0, 45).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(5.9F, -15.4F, 3.3F, 0.0F, 0.6109F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // 개별 파트를 렌더링하는 대신 root 하나만 렌더링하여 하위 파트가 전부 출력되도록 합니다.
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}