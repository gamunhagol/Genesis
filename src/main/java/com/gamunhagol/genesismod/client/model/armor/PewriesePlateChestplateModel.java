package com.gamunhagol.genesismod.client.model.armor;


import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PewriesePlateChestplateModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"pewriese_plate_chestplate"),"main");

    public PewriesePlateChestplateModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_arm = partDefinition.getChild("right_arm");
        PartDefinition left_arm = partDefinition.getChild("left_arm");

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create()
                .texOffs(16,80).addBox(-4.0F,-24.0F,-2.0F, 8.0F,12.0F,4.0F
                        , new CubeDeformation(0.85F)), PartPose.offset(0.0F,24.0F,0.0F));

        PartDefinition right_arm_armor = right_arm.addOrReplaceChild("right_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 80).addBox(-3.25F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F))
                , PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition left_arm_armor = left_arm.addOrReplaceChild("left_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 80).mirror().addBox(-0.75F, -0.2F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                , PartPose.offset(0.0F, -1.5F, 0.0F));


        PartDefinition right_shoulder = right_arm.addOrReplaceChild("right_shoulder", CubeListBuilder.create()
                .texOffs(40,96).addBox(-3.25F,-0.2F,-2.0F,4.0F,4.0F,4.0F
                        ,new CubeDeformation(1.3F)), PartPose.offset(0.0F,-1.5F,0.0F));


        PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", CubeListBuilder.create()
                .texOffs(40,96).mirror().addBox(-0.75F,-0.2F,-2.0F,4.0F,4.0F,4.0F
                        ,new CubeDeformation(1.3F)).mirror(false), PartPose.offset(0.0F,-1.5F,0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
