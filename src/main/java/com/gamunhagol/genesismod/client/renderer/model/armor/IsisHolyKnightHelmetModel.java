package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisHolyKnightHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"isis_holy_knight_helmet"),"main");

    public IsisHolyKnightHelmetModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition head_armor = head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                .texOffs(0,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.5F))
                        .texOffs(32,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.7F))
                        .texOffs(110,107).addBox(-0.0F,-10.0F,4.0F,0.0F,10.0F,2.0F, new CubeDeformation(0.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition head_wing = head.addOrReplaceChild("head_wing", CubeListBuilder.create()
                .texOffs(0,48).addBox(-5.0F,-7.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.06F))
                        .texOffs(0,25).addBox(-5.0F,-7.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.065F))
                        .texOffs(0,48).addBox(-5.0F,-10.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.04F))
                        .texOffs(0,25).addBox(-5.0F,-10.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.055F))
                        .texOffs(0,48).addBox(-5.0F,-13.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.03F))
                        .texOffs(0,25).addBox(-5.0F,-13.0F,-5.0F,10.0F,10.0F,12.0F, new CubeDeformation(-0.045F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
