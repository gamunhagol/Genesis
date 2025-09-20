package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class HolyKnightBootsModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"pewriese_holy_knight_boots"),"main");

    public HolyKnightBootsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F))
                        .texOffs(56,114).addBox(-1.0F,1.0F,2.0F,2.0F,12.0F,2.0F, new CubeDeformation(0.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));



        PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F)).mirror(false)
                        .texOffs(56,114).mirror().addBox(-1.0F,1.0F,2.0F,2.0F,12.0F,2.0F, new CubeDeformation(0.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
