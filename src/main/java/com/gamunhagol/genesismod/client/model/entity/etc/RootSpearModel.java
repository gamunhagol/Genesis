package com.gamunhagol.genesismod.client.model.entity.etc;

import com.gamunhagol.genesismod.world.entity.etc.child.RootSpearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class RootSpearModel<T extends RootSpearEntity> extends EntityModel<T> {
    private final ModelPart root_1;
    private final ModelPart root_2;
    private final ModelPart root_3;
    private final ModelPart root_4;

    public RootSpearModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root_1 = root.getChild("root_1");
        this.root_2 = root.getChild("root_2");
        this.root_3 = root.getChild("root_3");
        this.root_4 = root.getChild("root_4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("root_1", CubeListBuilder.create().texOffs(0, -16)
                        .addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        partdefinition.addOrReplaceChild("root_2", CubeListBuilder.create().texOffs(0, -16)
                        .addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        partdefinition.addOrReplaceChild("root_3", CubeListBuilder.create().texOffs(0, -16)
                        .addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("root_4", CubeListBuilder.create().texOffs(0, -16)
                        .addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        root_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        root_3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        root_4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
