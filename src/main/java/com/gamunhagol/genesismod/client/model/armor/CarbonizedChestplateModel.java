package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CarbonizedChestplateModel<T extends LivingEntity> extends HumanoidModel<T>{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "carbonized_chestplate"), "main");

    public CarbonizedChestplateModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition left_arm = partDefinition.getChild("left_arm");
        PartDefinition right_arm = partDefinition.getChild("right_arm");

        PartDefinition body_armor = body.addOrReplaceChild("body_armor", CubeListBuilder.create()
                        .texOffs(16, 112).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm_armor = left_arm.addOrReplaceChild("left_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 112).mirror().addBox(-1.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                        .texOffs(64, 90).mirror().addBox(-1.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false)
                        .texOffs(80, 89).mirror().addBox(-1.0F, -3.9F, -1.0F, 4.0F, 6.0F, 5.0F, new CubeDeformation(1.1F)).mirror(false)
                        .texOffs(80, 95).mirror().addBox(-1.0F, 2.6F, -0.9F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.76F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm_armor = right_arm.addOrReplaceChild("right_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 112).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F))
                        .texOffs(64, 90).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F))
                        .texOffs(80, 89).addBox(-3.0F, -3.9F, -1.0F, 4.0F, 6.0F, 5.0F, new CubeDeformation(1.1F))
                        .texOffs(80, 95).addBox(-3.0F, 2.6F, -0.9F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.76F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}