package com.gamunhagol.genesismod.client.model.block;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class GuideStatueModel extends Model {

    private final ModelPart root;

    public GuideStatueModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        // createBodyLayer에서 만든 "root" 파트를 가져옵니다.
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // 모든 부품을 포함할 최상위 부모 "root"
        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.ZERO);

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -15.0F, 0.0F));

        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                        // Guide 모델만의 고유한 헤드 장식들
                        .texOffs(34, 3).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(40, 7).addBox(-4.0F, -7.0F, -5.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -15.0F, 0.0F));

        root.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)),
                PartPose.offset(0.0F, -15.0F, 0.0F));

        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(56, 0)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -13.0F, 0.0F));

        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 0).mirror()
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(5.0F, -13.0F, 0.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 0)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, -6.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 0).mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(2.0F, -6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}