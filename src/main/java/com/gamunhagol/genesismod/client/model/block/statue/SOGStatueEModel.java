package com.gamunhagol.genesismod.client.model.block.statue;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class SOGStatueEModel extends Model {
    private final ModelPart root;

    // E 모델에 존재하는 파트들
    private final ModelPart crown;
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart side;
    private final ModelPart bb_main;

    public SOGStatueEModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);

        this.root = root;
        this.crown = root.getChild("crown");
        // front, back, side는 crown의 하위 파트이므로 crown.getChild로 가져옵니다.
        this.front = this.crown.getChild("front");
        this.back = this.crown.getChild("back");
        this.side = this.crown.getChild("side");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition crown = partdefinition.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(-8, 56).addBox(-6.0F, -14.0F, -4.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition front = crown.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 52).addBox(5.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(3.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(1.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).mirror().addBox(-5.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).mirror().addBox(-3.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).mirror().addBox(-1.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition back = crown.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 52).addBox(5.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(3.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(1.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).mirror().addBox(-5.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).mirror().addBox(-1.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 52).mirror().addBox(-3.0F, -16.0F, 3.2F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -7.4F));

        PartDefinition side = crown.addOrReplaceChild("side", CubeListBuilder.create().texOffs(0, 53).addBox(5.3F, -16.0F, 3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(5.3F, -16.0F, 1.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(5.3F, -16.0F, -1.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(5.3F, -16.0F, -3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).mirror().addBox(-6.3F, -16.0F, 3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 53).mirror().addBox(-6.3F, -16.0F, 1.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 53).mirror().addBox(-6.3F, -16.0F, -1.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 53).mirror().addBox(-6.3F, -16.0F, -3.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -4.0F, 14.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}