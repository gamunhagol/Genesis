package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueQModel extends Model {
    private final ModelPart root;
    private final ModelPart bb_main;

    public SOGStatueQModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition frame_4_r1 = bb_main.addOrReplaceChild("frame_4_r1", CubeListBuilder.create().texOffs(4, 35).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -15.2F, 1.1F, -0.3142F, 0.0F, 0.0F));

        PartDefinition frame_3_r1 = bb_main.addOrReplaceChild("frame_3_r1", CubeListBuilder.create().texOffs(4, 35).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-6.0F, -14.0F, -1.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(1.0F, -14.0F, -1.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.8F, -2.9F, -0.3142F, 0.0F, 0.0F));

        PartDefinition mirror_r1 = bb_main.addOrReplaceChild("mirror_r1", CubeListBuilder.create().texOffs(0, 19).addBox(-7.0F, -14.0F, -1.0F, 8.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.5F, -2.0F, -0.3142F, 0.0F, 0.0F));

        PartDefinition support_r1 = bb_main.addOrReplaceChild("support_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-9.0F, -4.0F, -1.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, -2.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}