package com.gamunhagol.genesismod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueAModel extends Model {
    private final ModelPart root;

    private final ModelPart horn;
    private final ModelPart mask;
    private final ModelPart bb_main;

    public SOGStatueAModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.horn = root.getChild("horn");
        this.mask = root.getChild("mask");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition horn = partdefinition.addOrReplaceChild("horn", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition horn_4_r_r1 = horn.addOrReplaceChild("horn_4_r_r1", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-3.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-1.8F, -17.2F, 2.0F, 0.0411F, -0.3027F, 1.4336F));

        PartDefinition horn_3_r_r1 = horn.addOrReplaceChild("horn_3_r_r1", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-3.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -18.5F, 2.0F, 0.1298F, -0.1895F, 1.1278F));

        PartDefinition horn_2_r_r1 = horn.addOrReplaceChild("horn_2_r_r1", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-3.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-6.5F, -17.1F, 2.0F, 0.2258F, -0.1256F, 0.7639F));

        PartDefinition horn_3_l_r1 = horn.addOrReplaceChild("horn_3_l_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.0F, -18.5F, 2.0F, 0.1298F, 0.1895F, -1.1278F));

        PartDefinition horn_4_l_r1 = horn.addOrReplaceChild("horn_4_l_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.8F, -17.2F, 2.0F, 0.0411F, 0.3027F, -1.4336F));

        PartDefinition horn_2_l_r1 = horn.addOrReplaceChild("horn_2_l_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(6.5F, -17.1F, 2.0F, 0.2258F, 0.1256F, -0.7639F));

        PartDefinition horn_1_r_r1 = horn.addOrReplaceChild("horn_1_r_r1", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-3.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -14.5F, 2.0F, 0.2921F, -0.0905F, 0.2921F));

        PartDefinition horn_1_l_r1 = horn.addOrReplaceChild("horn_1_l_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -14.5F, 2.0F, 0.2921F, 0.0905F, -0.2921F));

        PartDefinition mask = partdefinition.addOrReplaceChild("mask", CubeListBuilder.create(), PartPose.offset(2.0F, 11.5F, 3.0F));

        PartDefinition mask_3_r1 = mask.addOrReplaceChild("mask_3_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition mask_2_r_r1 = mask.addOrReplaceChild("mask_2_r_r1", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-1.0F, -4.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.12F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.098F, -0.0131F, -0.3052F));

        PartDefinition mask_1_r_r1 = mask.addOrReplaceChild("mask_1_r_r1", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-1.0F, -4.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.1388F, -0.025F, -0.6104F));

        PartDefinition mask_2_l_r1 = mask.addOrReplaceChild("mask_2_l_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -4.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.12F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.098F, 0.0131F, 0.3052F));

        PartDefinition mask_1_l_r1 = mask.addOrReplaceChild("mask_1_l_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -4.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1388F, 0.025F, 0.6104F));

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