package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisHolyKnightLeggingsModel <T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"isis_holy_knight_leggings"),"main");

    public IsisHolyKnightLeggingsModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_leg = partDefinition.getChild("right_leg");
        PartDefinition left_leg = partDefinition.getChild("left_leg");

        PartDefinition waist_armor = body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16,112).addBox(-4.0F,-24.0F,-2.0F,8.0F,12.0F,4.0F, new CubeDeformation(0.4F))
                , PartPose.offset(0.0F,23.5F,0.0F));

        PartDefinition right_fauld = body.addOrReplaceChild("right_fauld", CubeListBuilder.create()
                .texOffs(100,120).addBox(-11.0F,-12.0F,-2.0F,4.0F,4.0F,4.0F, new CubeDeformation(0.95F))
                        .texOffs(92,106).addBox(-11.0F,-11.0F,-2.0F,4.0F,6.0F,4.0F, new CubeDeformation(0.75F))
                , PartPose.offsetAndRotation(0.0F,23.5F,0.0F,0.0F,0.0F,0.5F));

        PartDefinition left_fauld = body.addOrReplaceChild("left_fauld", CubeListBuilder.create()
                        .texOffs(100,120).mirror().addBox(7.0F,-12.0F,-2.0F,4.0F,4.0F,4.0F, new CubeDeformation(0.95F)).mirror(false)
                        .texOffs(92,106).mirror().addBox(7.0F,-11.0F,-2.0F,4.0F,6.0F,4.0F, new CubeDeformation(0.75F)).mirror(false)
                , PartPose.offsetAndRotation(0.0F,23.5F,0.0F,0.0F,0.0F,-0.5F));

        PartDefinition right_legging = right_leg.addOrReplaceChild("right_legging", CubeListBuilder.create()
                        .texOffs(0,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.25F))
                        .texOffs(60,112).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.35F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition right_legging_wing = right_legging.addOrReplaceChild("right_legging_wing",CubeListBuilder.create()
                .texOffs(76,110).addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,6.0F, new CubeDeformation(0.4F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition left_legging = left_leg.addOrReplaceChild("left_legging", CubeListBuilder.create()
                        .texOffs(0,112).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.25F))
                        .texOffs(60,112).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.35F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition left_legging_wing = left_legging.addOrReplaceChild("left_legging_wing",CubeListBuilder.create()
                        .texOffs(76,110).mirror().addBox(-2.0F,0.0F,-2.0F,4.0F,12.0F,6.0F, new CubeDeformation(0.4F)).mirror(false)
                , PartPose.offset(0.0F,0.0F,0.0F));


        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
