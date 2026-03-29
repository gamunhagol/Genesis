package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class LargeArrowModel<T extends LargeArrowEntity> extends EntityModel<T> {
    private final ModelPart back;
    private final ModelPart cross_1;
    private final ModelPart cross_2;

    public LargeArrowModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.back = root.getChild("back");
        this.cross_1 = root.getChild("cross_1");
        this.cross_2 = root.getChild("cross_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -4.0F, -3.0F, 0.0F, 7.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 11.0F, -2.3562F, 1.5708F, 0.0F));

        partdefinition.addOrReplaceChild("cross_1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -4.0F, 0.5F, 24.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 1.5708F, 0.0F));

        partdefinition.addOrReplaceChild("cross_2", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -3.0F, 0.5F, 24.0F, 7.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        back.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        cross_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        cross_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}