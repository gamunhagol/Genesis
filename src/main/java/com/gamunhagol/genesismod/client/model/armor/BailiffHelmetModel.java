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
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition head_armor = head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 94).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("crown_r1", CubeListBuilder.create()
                        .texOffs(34, 62).addBox(-4.0F, -32.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offsetAndRotation(0.0F, 22.7F, 10.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition wing = head.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(8.0F, 22.5F, 0.0F));

        wing.addOrReplaceChild("lwing_2_r1", CubeListBuilder.create()
                        .texOffs(34, 78).addBox(-3.0F, -37.5F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.0F)),
                PartPose.offsetAndRotation(-9.0F, 4.5F, -1.0F, 0.0F, -0.5672F, 0.0F));

        wing.addOrReplaceChild("lwing_1_r1", CubeListBuilder.create()
                        .texOffs(34, 78).addBox(-3.0F, -37.5F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-15.0F, 0.0F, -3.0F, 0.0F, 0.4363F, 0.0F));

        wing.addOrReplaceChild("rwing_2_r1", CubeListBuilder.create()
                        .texOffs(34, 78).mirror().addBox(-5.0F, -37.5F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-1.0F)).mirror(false),
                PartPose.offsetAndRotation(-7.0F, 4.5F, -1.0F, 0.0F, 0.5672F, 0.0F));

        wing.addOrReplaceChild("rwing_1_r1", CubeListBuilder.create()
                        .texOffs(34, 78).mirror().addBox(-5.0F, -37.5F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-1.0F, 0.0F, -3.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition collar = head.addOrReplaceChild("collar", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5F, 5.0F));

        collar.addOrReplaceChild("collar_1_7_r1", CubeListBuilder.create()
                        .texOffs(0, 88).addBox(3.0F, -24.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 92).addBox(-4.0F, -24.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 22.2F, 7.3F, 0.2618F, 0.0F, 0.0F));

        collar.addOrReplaceChild("collar_1_6_r1", CubeListBuilder.create()
                        .texOffs(0, 88).addBox(3.0F, -24.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 90).addBox(-2.0F, -24.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 22.4F, 6.3F, 0.2618F, 0.0F, 0.0F));

        collar.addOrReplaceChild("collar_1_5_r1", CubeListBuilder.create()
                        .texOffs(0, 88).addBox(3.0F, -24.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 90).addBox(0.0F, -24.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 89).addBox(-2.0F, -24.0F, -4.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 22.7F, 5.3F, 0.2618F, 0.0F, 0.0F));

        PartDefinition lower_collar = head.addOrReplaceChild("lower_collar", CubeListBuilder.create()
                        .texOffs(4, 72).addBox(-4.0F, -34.3F, -2.7F, 8.0F, 16.0F, 1.0F, new CubeDeformation(-0.1F)),
                PartPose.offset(0.0F, 24.0F, 8.0F));

        lower_collar.addOrReplaceChild("down_feathers", CubeListBuilder.create()
                        .texOffs(24, 96).addBox(3.5F, -28.5F, -4.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 96).addBox(3.2F, -32.3F, -4.3F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 96).addBox(2.9F, -36.3F, -4.7F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 96).addBox(2.3F, -40.3F, -4.3F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 96).mirror().addBox(-6.5F, -28.5F, -4.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(24, 96).mirror().addBox(-6.2F, -32.3F, -4.3F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(24, 96).mirror().addBox(-5.9F, -36.3F, -4.7F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(24, 96).mirror().addBox(-5.3F, -40.3F, -4.3F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 6.2F, 1.3F));

        PartDefinition bottom_collar = head.addOrReplaceChild("bottom_collar", CubeListBuilder.create()
                        .texOffs(22, 82).addBox(-3.0F, -18.7F, 5.3F, 6.0F, 3.0F, 1.0F, new CubeDeformation(-0.15F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        bottom_collar.addOrReplaceChild("peak_feathers", CubeListBuilder.create()
                        .texOffs(22, 76).addBox(3.0F, -27.5F, -4.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 70).addBox(-1.0F, -24.0F, -4.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 76).mirror().addBox(-4.0F, -27.5F, -4.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(-1.0F, 8.0F, 9.3F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}