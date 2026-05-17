package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueGModel extends Model {
    private final ModelPart root;

    // G 모델에 존재하는 파트
    private final ModelPart bb_main;

    public SOGStatueGModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head_r1 = bb_main.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.7F, -9.7F, -2.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}