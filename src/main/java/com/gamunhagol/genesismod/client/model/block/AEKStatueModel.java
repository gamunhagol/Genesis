package com.gamunhagol.genesismod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class AEKStatueModel extends Model {
    private final ModelPart root;

    // 나중에 애니메이션을 넣을 수도 있으니 개별 파트 변수는 남겨둡니다.
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public AEKStatueModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F))
                .texOffs(20, 34).addBox(-4.0F, 20.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(56, 26).addBox(-8.0F, 4.0F, -1.0F, 16.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 18.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition headwear = partdefinition.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
                .texOffs(58, 46).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition left_eararmor_r1 = headwear.addOrReplaceChild("left_eararmor_r1", CubeListBuilder.create().texOffs(48, 56).mirror().addBox(-1.0F, -38.0F, -3.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 39.0F, 0.0F, 0.0F, -0.3054F, 0.0F));

        PartDefinition right_eararmor_r1 = headwear.addOrReplaceChild("right_eararmor_r1", CubeListBuilder.create().texOffs(48, 56).addBox(-4.0F, -38.0F, -3.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 39.0F, 0.0F, 0.0F, 0.3054F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, 7.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 42).addBox(-3.0F, 7.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(-5.0F, -13.0F, 0.0F));

        PartDefinition right_arm_armor_r1 = right_arm.addOrReplaceChild("right_arm_armor_r1", CubeListBuilder.create().texOffs(16, 51).addBox(-8.0F, -1.0F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.75F))
                .texOffs(0, 51).addBox(-8.0F, -1.0F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 19.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 51).mirror().addBox(-1.0F, 7.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 42).mirror().addBox(-1.0F, 7.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.offset(5.0F, -13.0F, 0.0F));

        PartDefinition left_arm_armor_r1 = left_arm.addOrReplaceChild("left_arm_armor_r1", CubeListBuilder.create().texOffs(16, 51).mirror().addBox(4.0F, -1.0F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                .texOffs(0, 51).mirror().addBox(4.0F, -1.0F, -3.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 19.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 42).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.0F, -6.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-2.0F, 12.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 42).mirror().addBox(-2.0F, 12.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(2.0F, -6.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);}
}
