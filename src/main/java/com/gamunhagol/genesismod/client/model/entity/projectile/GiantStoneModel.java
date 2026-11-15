package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class GiantStoneModel<T extends StoneProjectile> extends EntityModel<T> {
    private final ModelPart under;
    private final ModelPart upper;
    private final ModelPart wide;

    public GiantStoneModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.under = root.getChild("under");
        this.upper = root.getChild("upper");
        this.wide = root.getChild("wide");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition under = partdefinition.addOrReplaceChild("under", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, 20.0F, -7.0F));

        under.addOrReplaceChild("under_rock_9_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, -10.0F, -4.0F, 0.6788F, 0.2261F, -0.623F));

        under.addOrReplaceChild("under_rock_8_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, -10.0F, 18.0F, 0.7676F, 0.0714F, -0.6513F));

        under.addOrReplaceChild("under_rock_7_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(15.0F, -11.0F, 19.0F, 0.9445F, 0.1694F, -0.616F));

        under.addOrReplaceChild("under_rock_6_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-16.0F, -16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(15.0F, -11.0F, -3.0F, 0.7773F, -0.0303F, -0.6311F));

        under.addOrReplaceChild("under_rock_5_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.8485F, -0.7172F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.9599F, 0.0F, 0.0F));

        under.addOrReplaceChild("under_rock_4_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-14.0808F, -16.0707F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        under.addOrReplaceChild("under_rock_3_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -15.9293F, -1.9192F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -12.0F, 8.0F, -0.9599F, 0.0F, 0.0F));

        under.addOrReplaceChild("under_rock_2_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.2828F, -16.7071F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition upper = partdefinition.addOrReplaceChild("upper", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -15.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, -4.0F, -7.0F));

        upper.addOrReplaceChild("upper_rock_9_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-16.0F, 2.0F, 6.0F, -1.2133F, 0.916F, -2.0776F));

        upper.addOrReplaceChild("upper_rock_8_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(6.0F, 4.0F, 8.0F, -0.0028F, 0.961F, -0.9672F));

        upper.addOrReplaceChild("upper_rock_7_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(12.0F, 3.0F, -6.0F, 0.5769F, 0.5789F, -0.6447F));

        upper.addOrReplaceChild("upper_rock_6_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.7854F, 0.0F, -0.7854F));

        upper.addOrReplaceChild("upper_rock_5_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 9.0F, -0.5672F, 0.0F, 0.0F));

        upper.addOrReplaceChild("upper_rock_4_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-18.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.9163F));

        upper.addOrReplaceChild("upper_rock_3_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -4.0F, -14.0F, -0.8727F, 0.0F, 0.0F));

        upper.addOrReplaceChild("upper_rock_2_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(14.0F, -6.0F, 0.0F, 0.0F, 0.0F, -1.0036F));

        PartDefinition wide = partdefinition.addOrReplaceChild("wide", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-28.0F, -16.0F, -17.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-15.0F, -15.0F, -32.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-2.0F, -17.0F, -17.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(7.0F, 8.0F, 9.0F));

        wide.addOrReplaceChild("wide_rock_8_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(11.0F, 0.0F, 3.0F, 0.0F, -0.7854F, 0.0F));

        wide.addOrReplaceChild("wide_rock_7_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(12.0F, 1.0F, -21.0F, 0.0F, -0.7854F, 0.0F));

        wide.addOrReplaceChild("wide_rock_6_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, 0.0F, -20.0F, 0.0F, -0.7854F, 0.0F));

        wide.addOrReplaceChild("wide_rock_5_r1", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, 1.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        under.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        upper.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        wide.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
