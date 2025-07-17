package com.gamunhagol.genesismod.client.renderer.model.armor;


import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisPlateChestplateModel <T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"isis_plate_chestplate"),"main");

    public IsisPlateChestplateModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_arm = partDefinition.getChild("right_arm");
        PartDefinition left_arm = partDefinition.getChild("left_arm");

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create()
                .texOffs(16,16).addBox(-4.0F,-24.0F,-2.0F, 8.0F,12.0F,4.0F
                        , new CubeDeformation(0.8F)), PartPose.offset(0.0F,24.0F,0.0F));

        PartDefinition right_rid = body.addOrReplaceChild("right_rid", CubeListBuilder.create()
                .texOffs(0,39).addBox(-4.0F,0.0F,-2.0F,4.0F,2.0F,4.0F
                        ,new CubeDeformation(1.0F)),PartPose.offsetAndRotation(-0.75F,4.0F,0.0F,0.0F,0.0F,-0.25F));

        PartDefinition right_rid_u = body.addOrReplaceChild("right_rid_u", CubeListBuilder.create()
                .texOffs(0,39).addBox(-4.0F,0.0F,-2.0F,4.0F,2.0F,4.0F
                        ,new CubeDeformation(1.1F)),PartPose.offsetAndRotation(-0.75F,1.0F,0.0F,0.0F,0.0F,-0.25F));

        PartDefinition left_rid = body.addOrReplaceChild("left_rid", CubeListBuilder.create()
                .texOffs(0,39).mirror().addBox(0.0F,0.0F,-2.0F,4.0F,2.0F,4.0F
                        ,new CubeDeformation(0.95F)).mirror(false),PartPose.offsetAndRotation(0.75F,4.0F,0.0F,0.0F,0.0F,0.25F));

        PartDefinition left_rid_u = body.addOrReplaceChild("left_rid_u", CubeListBuilder.create()
                .texOffs(0,39).mirror().addBox(0.0F,0.0F,-2.0F,4.0F,2.0F,4.0F
                        ,new CubeDeformation(1.05F)).mirror(false),PartPose.offsetAndRotation(0.75F,1.0F,0.0F,0.0F,0.0F,0.25F));



        PartDefinition right_armor = right_arm.addOrReplaceChild("right_armor", CubeListBuilder.create()
                .texOffs(40,16).addBox(-3.25F,-0.2F,-2.0F,4.0F,12.0F,4.0F
                        , new CubeDeformation(0.6F)), PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition right_shoulder = right_arm.addOrReplaceChild("right_shoulder", CubeListBuilder.create()
                .texOffs(24,32).addBox(-3.25F,-0.2F,-2.0F,4.0F,4.0F,4.0F
                        ,new CubeDeformation(1.3F)), PartPose.offset(0.0F,-1.5F,0.0F));

        PartDefinition left_armor = left_arm.addOrReplaceChild("left_armor", CubeListBuilder.create()
                .texOffs(40,16).mirror().addBox(-0.75F,-0.2F,-2.0F,4.0F,12.0F,4.0F
                        , new CubeDeformation(0.6F)).mirror(false), PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", CubeListBuilder.create()
                .texOffs(24,32).mirror().addBox(-0.75F,-0.2F,-2.0F,4.0F,4.0F,4.0F
                        ,new CubeDeformation(1.3F)).mirror(false), PartPose.offset(0.0F,-1.5F,0.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
