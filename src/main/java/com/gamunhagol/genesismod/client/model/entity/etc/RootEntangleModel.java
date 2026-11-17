package com.gamunhagol.genesismod.client.model.entity.etc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class RootEntangleModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart root_1;
    private final ModelPart root_2;

    public RootEntangleModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.root_1 = root.getChild("root_1");
        this.root_2 = root.getChild("root_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root_1 = partdefinition.addOrReplaceChild("root_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));

        root_1.addOrReplaceChild("root_3_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.0707F, -7.0F, -3.6062F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.4F, -10.0F, 2.5F, 0.0F, 0.7854F, 0.5672F));

        root_1.addOrReplaceChild("root_2_r1", CubeListBuilder.create().texOffs(14, -7)
                        .addBox(-0.0707F, -7.0F, -3.6062F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4F, -6.0F, 2.5F, 0.0F, 0.7854F, -0.48F));

        root_1.addOrReplaceChild("root_1_r1", CubeListBuilder.create().texOffs(0, -7)
                        .addBox(0.0F, -7.0F, 0.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition root_2 = partdefinition.addOrReplaceChild("root_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));

        root_2.addOrReplaceChild("root_3_r2", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.0707F, -7.0F, -3.4648F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.4F, -10.0F, 2.5F, 0.0F, -0.7854F, 0.5672F));

        root_2.addOrReplaceChild("root_2_r2", CubeListBuilder.create().texOffs(14, -7)
                        .addBox(-0.0707F, -7.0F, -3.4648F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.4F, -6.0F, 2.5F, 0.0F, -0.7854F, -0.48F));

        root_2.addOrReplaceChild("root_1_r2", CubeListBuilder.create().texOffs(0, -7)
                        .addBox(0.0F, -7.0F, 0.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        root_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
