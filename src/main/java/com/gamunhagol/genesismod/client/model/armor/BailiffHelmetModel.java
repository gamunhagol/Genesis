package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class BailiffHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "bailiff_helmet"), "main");

    public BailiffHelmetModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
                        .texOffs(-14, 50).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 57).addBox(-6.0F, -7.0F, -4.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 57).addBox(-6.0F, -7.0F, -3.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 56).addBox(-6.0F, -8.0F, -2.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 56).addBox(-7.0F, -8.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 56).addBox(-7.0F, -8.0F, 2.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 55).addBox(-7.0F, -9.0F, 4.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(28, 57).mirror().addBox(5.0F, -7.0F, -4.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(28, 57).mirror().addBox(5.0F, -7.0F, -3.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(30, 56).mirror().addBox(5.0F, -8.0F, -2.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(30, 56).mirror().addBox(6.0F, -8.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(30, 56).mirror().addBox(6.0F, -8.0F, 2.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(32, 55).mirror().addBox(6.0F, -9.0F, 4.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("thorn_left_9_r1", CubeListBuilder.create()
                        .texOffs(34, 54).mirror().addBox(-1.0F, -8.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(3.0F, -3.0F, 6.0F, 0.0F, 1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_left_8_r1", CubeListBuilder.create()
                        .texOffs(32, 55).mirror().addBox(-1.0F, -6.0F, -1.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, 0.0F, 1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_left_7_r1", CubeListBuilder.create()
                        .texOffs(32, 55).mirror().addBox(-1.0F, -6.0F, -1.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(7.0F, -3.0F, 5.0F, 0.0F, 1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_10_r1", CubeListBuilder.create()
                        .texOffs(36, 53).addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -3.0F, 6.0F, 0.0F, -1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_right_9_r1", CubeListBuilder.create()
                        .texOffs(34, 54).addBox(0.0F, -8.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.0F, -3.0F, 6.0F, 0.0F, -1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_right_8_r1", CubeListBuilder.create()
                        .texOffs(32, 55).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, -3.0F, 6.0F, 0.0F, -1.5708F, 0.0F));

        head.addOrReplaceChild("thorn_right_7_r1", CubeListBuilder.create()
                        .texOffs(32, 55).addBox(0.0F, -6.0F, -1.0F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.0F, -3.0F, 5.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
