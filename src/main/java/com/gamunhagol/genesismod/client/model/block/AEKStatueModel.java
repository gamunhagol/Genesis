package com.gamunhagol.genesismod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class AEKStatueModel extends Model {
    private final ModelPart right_inerarm;
    private final ModelPart right_arm;
    private final ModelPart left_inerarm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart right_shoe;
    private final ModelPart left_leg;
    private final ModelPart left_shoe;
    private final ModelPart waist;
    private final ModelPart body;
    private final ModelPart ear;
    private final ModelPart head;
    private final ModelPart foundation;
    private final ModelPart stone_tray;

    public AEKStatueModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.right_inerarm = root.getChild("right_inerarm");
        this.right_arm = root.getChild("right_arm");
        this.left_inerarm = root.getChild("left_inerarm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.right_shoe = root.getChild("right_shoe");
        this.left_leg = root.getChild("left_leg");
        this.left_shoe = root.getChild("left_shoe");
        this.waist = root.getChild("waist");
        this.body = root.getChild("body");
        this.ear = root.getChild("ear");
        this.head = root.getChild("head");
        this.foundation = root.getChild("foundation");
        this.stone_tray = root.getChild("stone_tray");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_inerarm = partdefinition.addOrReplaceChild("right_inerarm",
                CubeListBuilder.create().texOffs(56, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)),
                PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)),
                PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition left_inerarm = partdefinition.addOrReplaceChild("left_inerarm",
                CubeListBuilder.create().texOffs(56, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false),
                PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false),
                PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)),
                PartPose.offset(-1.9F, 10.0F, 0.0F));

        PartDefinition right_shoe = partdefinition.addOrReplaceChild("right_shoe",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)),
                PartPose.offset(-1.9F, 10.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false),
                PartPose.offset(1.9F, 10.0F, 0.0F));

        PartDefinition left_shoe = partdefinition.addOrReplaceChild("left_shoe",
                CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)).mirror(false),
                PartPose.offset(1.9F, 10.0F, 0.0F));

        PartDefinition waist = partdefinition.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition ear = partdefinition.addOrReplaceChild("ear",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        ear.addOrReplaceChild("right_ear_r1",
                CubeListBuilder.create().texOffs(72, 27).mirror().addBox(-1.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(5.0F, -25.0F, -1.0F, 0.0F, -0.1309F, 0.0F));

        ear.addOrReplaceChild("left_ear_r1",
                CubeListBuilder.create().texOffs(73, 27).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, -25.0F, -1.0F, 0.0F, 0.1309F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        head.addOrReplaceChild("head_2_r1",
                CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, -31.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.8F)),
                PartPose.offsetAndRotation(0.0F, 23.0F, 4.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition foundation = partdefinition.addOrReplaceChild("foundation",
                CubeListBuilder.create().texOffs(0, 48).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stone_tray = partdefinition.addOrReplaceChild("stone_tray",
                CubeListBuilder.create().texOffs(42, 39).addBox(-5.0F, -23.0F, -15.0F, 12.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 34).addBox(-1.0F, -24.0F, -13.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.0F, 28.0F, 4.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.right_inerarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_inerarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.right_shoe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.left_shoe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.ear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.foundation.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.stone_tray.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
