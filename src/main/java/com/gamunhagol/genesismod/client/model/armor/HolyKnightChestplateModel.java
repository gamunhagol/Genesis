package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class HolyKnightChestplateModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"pewriese_holy_knight_chestplate"),"main");

    public HolyKnightChestplateModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_arm = partDefinition.getChild("right_arm");
        PartDefinition left_arm = partDefinition.getChild("left_arm");

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create()
                .texOffs(16,112).addBox(-4.0F,-24.0F,-2.0F, 8.0F,12.0F,4.0F, new CubeDeformation(0.8F))
                        .texOffs(64,90).addBox(-1.0F,-24.0F,2.0F, 0.0F,12.0F,2.0F, new CubeDeformation(0.0F))
                        .texOffs(64,90).mirror().addBox(1.0F,-24.0F,2.0F, 0.0F,12.0F,2.0F, new CubeDeformation(0.0F)).mirror(false)
                , PartPose.offset(0.0F,24.0F,0.0F));




        PartDefinition right_armor = right_arm.addOrReplaceChild("right_armor", CubeListBuilder.create()
                .texOffs(40,112).addBox(-3.25F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.7F))
                        .texOffs(84,88).addBox(-3.25F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F))
                        .texOffs(68,88).addBox(-5.25F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.75F))
                , PartPose.offset(0.0F, -1.5F, 0.0F));


        PartDefinition left_armor = left_arm.addOrReplaceChild("left_armor", CubeListBuilder.create()
                .texOffs(40,112).mirror().addBox(-0.75F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.7F)).mirror(false)
                        .texOffs(84,88).mirror().addBox(-0.75F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.0F)).mirror(false)
                        .texOffs(68,88).mirror().addBox(1.25F,-0.7F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.75F)).mirror(false)
                , PartPose.offset(0.0F, -1.5F, 0.0F));


        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
