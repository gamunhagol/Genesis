package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.FrozenStarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class FrozenStarModel<T extends FrozenStarEntity> extends EntityModel<T> {
    private final ModelPart bb_main;

    public FrozenStarModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(12.0F, -6.0F, 1.0F, 0.0F, -0.8727F, -0.6981F));

        bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, -8.0F, -13.0F, -0.8588F, 0.4141F, -0.4924F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
