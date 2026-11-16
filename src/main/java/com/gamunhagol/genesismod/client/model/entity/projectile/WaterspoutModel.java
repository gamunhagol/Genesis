package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.WaterspoutEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class WaterspoutModel<T extends WaterspoutEntity> extends EntityModel<T> {
    private final ModelPart inner;
    private final ModelPart outer;

    public WaterspoutModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.inner = root.getChild("inner");
        this.outer = root.getChild("outer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("inner", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 26.0F, 10.0F, new CubeDeformation(1.0F)),
                PartPose.ZERO);

        partdefinition.addOrReplaceChild("outer", CubeListBuilder.create()
                        .texOffs(40, 0).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 26.0F, 10.0F, new CubeDeformation(2.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.inner.yRot = ageInTicks * 10.0F * ((float) Math.PI / 180F);
        this.outer.yRot = -ageInTicks * 20.0F * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.inner.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.outer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
