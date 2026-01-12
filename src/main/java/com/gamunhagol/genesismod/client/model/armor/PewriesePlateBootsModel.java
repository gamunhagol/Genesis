package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PewriesePlateBootsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "pewriese_plate_boots"), "main");

    public PewriesePlateBootsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create()
                .texOffs(0,16).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F))
                .texOffs(24,48).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.35F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create()
                .texOffs(0,16).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F)).mirror(false)
                .texOffs(24,48).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.35F)).mirror(false)
                , PartPose.offset(0.0F,0.0F,0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
