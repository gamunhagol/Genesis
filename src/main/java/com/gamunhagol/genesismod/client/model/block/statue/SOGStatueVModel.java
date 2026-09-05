package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueVModel extends Model {
    private final ModelPart root;
    private final ModelPart bb_main;

    public SOGStatueVModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-1.0F, -18.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F))
                .texOffs(0, 25).addBox(-6.4F, -18.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(4.1F, -16.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-10.0F, -23.0F, -1.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(0, 32).addBox(2.0F, -20.0F, -1.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(0, 42).addBox(-5.0F, -26.0F, -2.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition branch_6_r1 = bb_main.addOrReplaceChild("branch_6_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(2.0F, -7.0F, 0.0F, 0.0F, 0.0F, -1.2654F));

        PartDefinition branch_3_r1 = bb_main.addOrReplaceChild("branch_3_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-1.0F, -9.0F, 0.0F, 0.0F, 0.0F, 1.1345F));

        PartDefinition branch_2_r1 = bb_main.addOrReplaceChild("branch_2_r1", CubeListBuilder.create().texOffs(0, 25).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.7F, -11.0F, 0.0F, 0.0F, 0.0F, -1.1345F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
