package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.magic.child.StarGaspEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class StarGaspModel<T extends StarGaspEntity> extends EntityModel<T> {
    private final ModelPart cube;
    private final ModelPart ring;

    public StarGaspModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.cube = root.getChild("cube");
        this.ring = root.getChild("ring");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("cube", CubeListBuilder.create()
                        .texOffs(0, 48).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("ring", CubeListBuilder.create()
                        .texOffs(-48, 0).addBox(-24.0F, -8.0F, -24.0F, 48.0F, 0.0F, 48.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.ring.yRot = ageInTicks * 3.0F * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.cube.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.ring.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderCubeSwirl(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.cube.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
