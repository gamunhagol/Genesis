package com.gamunhagol.genesismod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class AmHeartModel extends Model {
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bb_main;

	public AmHeartModel(ModelPart root) {
		super(RenderType::entityTranslucentCull);
		this.bone = root.getChild("bone");
		this.bone2 = root.getChild("bone2");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(1.5F, 16.6F, -0.8F));

		bone.addOrReplaceChild("vessel_4_r1", CubeListBuilder.create().texOffs(22, 32).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.8F, -0.5F, -0.2793F, 0.0F, 0.0F));

		bone.addOrReplaceChild("vessel_3_r1", CubeListBuilder.create().texOffs(22, 32).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.8F, -0.4F, 0.4348F, 0.5416F, -0.8374F));

		bone.addOrReplaceChild("vessel_2_r1", CubeListBuilder.create().texOffs(22, 32).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.9F, 0.0F, 0.6807F, 0.0F, 0.0F));

		bone.addOrReplaceChild("vessel_1_r1", CubeListBuilder.create().texOffs(22, 32).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));


		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.9F, 13.8F, -2.8F));

		bone2.addOrReplaceChild("small_vessel_4_r1", CubeListBuilder.create().texOffs(22, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("small_vessel_4_r2", CubeListBuilder.create().texOffs(22, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 3.0F, -0.829F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("small_vessel_3_r1", CubeListBuilder.create().texOffs(22, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.75F, 0.9F, 0.5F, -0.6775F, -0.5214F, -0.5543F));

		bone2.addOrReplaceChild("small_vessel_2_r1", CubeListBuilder.create().texOffs(22, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.2F, -1.2217F, 0.0F, 0.0F));

		bone2.addOrReplaceChild("small_vessel_1_r1", CubeListBuilder.create().texOffs(22, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.829F, 0.0F, 0.0F));


		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		bb_main.addOrReplaceChild("atrium_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -6.0F, -1.0F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.0F, 1.0F, 0.9163F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}