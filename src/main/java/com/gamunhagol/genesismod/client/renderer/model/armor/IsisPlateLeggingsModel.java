package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisPlateLeggingsModel <T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "isis_plate_leggings"), "main");

    public IsisPlateLeggingsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition waist_armor = body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16,48).addBox(-4.0F,-24.0F,-2.0F,8.0F,12.0F,4.0F,
                        new CubeDeformation(0.7F)), PartPose.offset(0.0F,23.5F,0.0F));

        PartDefinition back_waist_armor = body.addOrReplaceChild("back_waist_armor", CubeListBuilder.create()
                .texOffs(16,18).addBox(0.0F,6.0F,-4.0F,8.0F,3.0F,1.0F,
                        new CubeDeformation(0.9F)), PartPose.offsetAndRotation(-4.0F,0.5F,3.0F,12.955F,0.0F,0.0F));

        PartDefinition back_waist_armor_u = body.addOrReplaceChild("back_waist_armor_u", CubeListBuilder.create()
                .texOffs(16,18).addBox(0.0F,6.0F,-4.0F,8.0F,3.0F,1.0F,
                        new CubeDeformation(1.1F)), PartPose.offsetAndRotation(-4.0F,3.5F,4.2F,12.955F,0.0F,0.0F));

        PartDefinition right_waist_armor = body.addOrReplaceChild("right_waist_armor", CubeListBuilder.create()
                .texOffs(26,22).addBox(-3.0F,8.0F,-1.0F,1.0F,3.0F,4.0F,
                        new CubeDeformation(0.9F)), PartPose.offsetAndRotation(0.7F,0.75F,-1.0F,0.0F,0.0F,-18.5F));

        PartDefinition right_waist_armor_u = body.addOrReplaceChild("right_waist_armor_u", CubeListBuilder.create()
                .texOffs(26,22).addBox(-3.0F,8.0F,-1.0F,1.0F,3.0F,4.0F,
                        new CubeDeformation(1.1F)), PartPose.offsetAndRotation(-0.4F,3.75F,-1.0F,0.0F,0.0F,-18.5F));

        PartDefinition left_waist_armor = body.addOrReplaceChild("left_waist_armor", CubeListBuilder.create()
                .texOffs(26,22).mirror().addBox(-3.0F,8.0F,-1.0F,1.0F,3.0F,4.0F,
                        new CubeDeformation(0.9F)).mirror(false), PartPose.offsetAndRotation(4.0F,-1.0F,-1.0F,0.0F,0.0F,18.5F));

        PartDefinition left_waist_armor_u = body.addOrReplaceChild("left_waist_armor_u", CubeListBuilder.create()
                .texOffs(26,22).mirror().addBox(-3.0F,8.0F,-1.0F,1.0F,3.0F,4.0F,
                        new CubeDeformation(1.1F)).mirror(false), PartPose.offsetAndRotation(5.1F,2.0F,-1.0F,0.0F,0.0F,18.5F));

        PartDefinition right_legging = right_leg.addOrReplaceChild("right_legging", CubeListBuilder.create()
                .texOffs(0,48).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                .texOffs(0,16).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.65F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition left_legging = left_leg.addOrReplaceChild("left_legging", CubeListBuilder.create()
                .texOffs(0,48).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.55F))
                .texOffs(0,16).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.65F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
