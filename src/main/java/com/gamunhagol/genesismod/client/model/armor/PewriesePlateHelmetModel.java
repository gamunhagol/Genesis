package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PewriesePlateHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "pewriese_plate_helmet"), "main");

    public PewriesePlateHelmetModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f) ,1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition head_armor = head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                .texOffs(0,64).addBox(-4.0F,-8.5F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.8F))
                .texOffs(32,64).addBox(-4.0F,-8.5F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(1.0F))
                        .texOffs(0,96).addBox(-4.0F,-11.50F,-1.0F,8.0F,8.0F,8.0F, new CubeDeformation(1.1F))
                , PartPose.offset(0.0F,0.0F,0.0F));




        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
