package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisHolyKnightBootsModel <T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"isis_holy_knight_boots"),"main");

    public IsisHolyKnightBootsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.7F))
                        .texOffs(60,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.05F))
                        .texOffs(60,112).addBox(-2.0F,0.0F,-2.0F,4.0F,11.0F,4.0F, new CubeDeformation(0.95F)).mirror(false)
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition right_boot_wing = right_boot.addOrReplaceChild("right_boot_wing", CubeListBuilder.create()
                .texOffs(76, 110).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,6.0F,new CubeDeformation(1.2F))
                , PartPose.offset(0.0F,0.0F,0.0F));


        PartDefinition left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.7F)).mirror(false)
                        .texOffs(60,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(1.05F)).mirror(false)
                        .texOffs(60,112).addBox(-2.0F,0.0F,-2.0F,4.0F,11.0F,4.0F, new CubeDeformation(0.95F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition  left_boot_wing = left_boot.addOrReplaceChild("left_boot_wing", CubeListBuilder.create()
                        .texOffs(76, 110).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,6.0F,new CubeDeformation(1.2F)).mirror(false)
                , PartPose.offset(0.0F,0.0F,0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
