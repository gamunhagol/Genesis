package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PewriesePlateLeggingsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "pewriese_plate_leggings"), "main");

    public PewriesePlateLeggingsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition waist_armor = body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16,16).addBox(-4.0F,-24.0F,-2.0F,8.0F,12.0F,4.0F, new CubeDeformation(0.7F))
                , PartPose.offset(0.0F,23.5F,0.0F));


        PartDefinition right_legging = right_leg.addOrReplaceChild("right_legging", CubeListBuilder.create()
                .texOffs(0,16).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                .texOffs(24,48).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.65F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition left_legging = left_leg.addOrReplaceChild("left_legging", CubeListBuilder.create()
                .texOffs(0,16).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                .texOffs(24,48).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.65F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
