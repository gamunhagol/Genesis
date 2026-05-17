package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueCModel extends Model {
    private final ModelPart root;

    // C 모델에 존재하는 파트들로 변경
    private final ModelPart horn;
    private final ModelPart head;
    private final ModelPart bb_main;

    public SOGStatueCModel(ModelPart root) {
        // B 모델과 동일하게 투명도 처리 렌더 타입 적용
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.horn = root.getChild("horn");
        this.head = root.getChild("head");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // statue_of_god_c의 큐브 빌더 코드 이식
        PartDefinition horn = partdefinition.addOrReplaceChild("horn", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition horn_5_r_r1 = horn.addOrReplaceChild("horn_5_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-4.3F, -14.1F, 2.2F, -1.5708F, 1.0559F, -1.5708F));

        PartDefinition horn_4_r_r1 = horn.addOrReplaceChild("horn_4_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-2.9F, -10.7F, 3.4F, 0.0F, -0.1745F, -0.48F));

        PartDefinition horn_3_r_r1 = horn.addOrReplaceChild("horn_3_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.8F, -14.1F, 3.4F, 0.0F, -0.1309F, 0.5585F));

        PartDefinition horn_2_r_r1 = horn.addOrReplaceChild("horn_2_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-6.6F, -10.8F, 2.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition horn_1_r_r1 = horn.addOrReplaceChild("horn_1_r_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-7.6F, -13.8F, 2.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition horn_5_l_r1 = horn.addOrReplaceChild("horn_5_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(4.3F, -14.1F, 2.2F, -1.5708F, -1.0559F, 1.5708F));

        PartDefinition horn_4_l_r1 = horn.addOrReplaceChild("horn_4_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(2.9F, -10.7F, 3.4F, 0.0F, 0.1745F, 0.48F));

        PartDefinition horn_3_l_r1 = horn.addOrReplaceChild("horn_3_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.8F, -14.1F, 3.4F, 0.0F, 0.1309F, -0.5585F));

        PartDefinition horn_2_l_r1 = horn.addOrReplaceChild("horn_2_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(6.6F, -10.8F, 2.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition horn_1_l_r1 = horn.addOrReplaceChild("horn_1_l_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(7.6F, -13.8F, 2.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition eye_r_r1 = head.addOrReplaceChild("eye_r_r1", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 45).addBox(3.4F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7F, -13.2F, 4.9F, -0.7854F, 0.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 38).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -12.6F, 3.0F, -0.5236F, 0.0F, 0.0F));

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