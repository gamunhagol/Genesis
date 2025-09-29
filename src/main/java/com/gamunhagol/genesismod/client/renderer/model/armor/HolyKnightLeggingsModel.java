package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class HolyKnightLeggingsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"pewriese_holy_knight_leggings"),"main");

    public HolyKnightLeggingsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition waist_armor = body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16,112).addBox(-4.0F,-24.0F,-2.0F,8.0F,12.0F,4.0F, new CubeDeformation(0.7F))
                        .texOffs(82,112).addBox(-4.0F,-24.0F,-2.0F,8.0F,12.0F,4.0F, new CubeDeformation(0.9F))
                , PartPose.offset(0.0F,23.5F,0.0F));

        PartDefinition waist_cloth = body.addOrReplaceChild("waist_cloth", CubeListBuilder.create()
                .texOffs(106,116).addBox(-4.0F,0.0F,-2.85F,8.0F,12.0F,0.0F, new CubeDeformation(0.0F))
                .texOffs(106,103).addBox(-5.0F,-0.0F,3.0F,10.0F,13.0F,0.0F, new CubeDeformation(0.0F))
                , PartPose.offset(0.0F,10.0F,0.0F));


        PartDefinition right_legging = right_leg.addOrReplaceChild("right_legging", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                        .texOffs(64,111).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,5.0F, new CubeDeformation(0.6F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        PartDefinition left_legging = left_leg.addOrReplaceChild("left_legging", CubeListBuilder.create()
                        .texOffs(0,112).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                        .texOffs(64,111).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,5.0F, new CubeDeformation(0.6F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
