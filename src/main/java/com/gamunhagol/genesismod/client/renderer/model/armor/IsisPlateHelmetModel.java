package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisPlateHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "isis_plate_helmet"), "main");

    public IsisPlateHelmetModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f) ,1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition head_armor = head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                .texOffs(0,0).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.8F))
                .texOffs(32,0).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(1.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition right_horn = head.addOrReplaceChild("right_horn", CubeListBuilder.create()
                .texOffs(0,48).addBox(-5.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F,0.0F,0.0F,0.225F,-0.05F,-0.095F));

        PartDefinition right_horn_a = head.addOrReplaceChild("right_horn_a", CubeListBuilder.create()
                .texOffs(0,48).addBox(-5.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F,-2.0F,0.0F,0.225F,-0.05F,-0.095F));

        PartDefinition right_horn_b = head.addOrReplaceChild("right_horn_b", CubeListBuilder.create()
                .texOffs(0,48).addBox(-5.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F,-4.0F,0.0F,0.225F,-0.05F,-0.095F));

        PartDefinition left_horn = head.addOrReplaceChild("left_horn", CubeListBuilder.create()
                .texOffs(0,48).mirror().addBox(4.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F,0.0F,0.0F,0.225F,0.05F,0.095F));

        PartDefinition left_horn_a = head.addOrReplaceChild("left_horn_a", CubeListBuilder.create()
                .texOffs(0,48).mirror().addBox(4.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F,-2.0F,0.0F,0.225F,0.05F,0.095F));

        PartDefinition left_horn_b = head.addOrReplaceChild("left_horn_b", CubeListBuilder.create()
                .texOffs(0,48).mirror().addBox(4.25F,-5.0F,-1.05F,1.0F,2.0F,7.0F
                        ,new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F,-4.0F,0.0F,0.225F,0.05F,0.095F));



        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
