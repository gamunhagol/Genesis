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
    private final ModelPart bb_main;

    public StormSegment2Model(ModelPart root) {
        super(RenderType::entityCutout);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-11.0F, -12.0F, -11.0F, 22.0F, 24.0F, 22.0F, new CubeDeformation(0.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 88, 88);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bb_main.yRot = ageInTicks * 20.0F * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
