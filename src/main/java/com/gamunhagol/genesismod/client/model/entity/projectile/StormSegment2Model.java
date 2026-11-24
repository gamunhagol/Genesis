package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class StormSegment2Model<T extends Entity> extends EntityModel<T> {
    private final ModelPart inner;
    private final ModelPart middle;
    private final ModelPart outer;

    public StormSegment2Model(ModelPart root) {
        super(RenderType::entityCutout);
        this.inner = root.getChild("inner");
        this.middle = root.getChild("middle");
        this.outer = root.getChild("outer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("inner", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-11.0F, -12.0F, -11.0F, 22.0F, 24.0F, 22.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        partdefinition.addOrReplaceChild("middle", CubeListBuilder.create()
                        .texOffs(0, 46).addBox(-11.0F, -12.0F, -11.0F, 22.0F, 24.0F, 22.0F, new CubeDeformation(1.0F)),
                PartPose.ZERO);

        partdefinition.addOrReplaceChild("outer", CubeListBuilder.create()
                        .texOffs(0, 92).addBox(-11.0F, -12.0F, -11.0F, 22.0F, 24.0F, 22.0F, new CubeDeformation(1.2F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 138, 138);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.inner.yRot = ageInTicks * 10.0F * ((float) Math.PI / 180F);
        this.middle.yRot = ageInTicks * 15.0F * ((float) Math.PI / 180F);
        this.outer.yRot = -ageInTicks * 20.0F * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.inner.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.middle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.outer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
