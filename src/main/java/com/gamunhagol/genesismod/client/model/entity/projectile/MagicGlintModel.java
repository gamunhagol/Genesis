package com.gamunhagol.genesismod.client.model.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class MagicGlintModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart cross_2;
	private final ModelPart cross_1;

	public MagicGlintModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.cross_2 = root.getChild("cross_2");
		this.cross_1 = root.getChild("cross_1");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild(
				"cross_2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 1.5708F, 0.0F)
		);

		partdefinition.addOrReplaceChild(
				"cross_1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 1.5708F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		cross_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		cross_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
