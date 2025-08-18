package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
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
                .texOffs(0,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.8F))
                        .texOffs(32,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(1.0F))
                        .texOffs(0,64).addBox(-5.0F,-8.0F,-4.0F,10.0F,8.0F,8.0F, new CubeDeformation(1.1F))
                        .texOffs(0,48).addBox(-5.0F,-10.0F,-4.0F,10.0F,8.0F,8.0F, new CubeDeformation(1.2F))
                        .texOffs(36,72).addBox(4.5F,-7.0F,-1.0F,1.0F,4.0F,4.0F, new CubeDeformation(0.0F))
                        .texOffs(36,72).addBox(-5.5F,-7.0F,-1.0F,1.0F,4.0F,4.0F, new CubeDeformation(0.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition head_right_wing = head.addOrReplaceChild("head_right_wing", CubeListBuilder.create()
                .texOffs(46,71).addBox(-5.0F,-4.0F,4.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-6.0F,-4.0F,0.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-6.0F,-3.0F,2.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-5.5F,-5.0F,6.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-5.5F,-6.0F,7.5F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-5.5F,-7.0F,9.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                        .texOffs(46,71).addBox(-5.5F,-8.0F,10.5F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F))
                , PartPose.offsetAndRotation(0.0F,0.0F,0.0F,0.475F,-0.275F,0.0F));

        PartDefinition head_left_wing = head.addOrReplaceChild("head_left_wing", CubeListBuilder.create()
                .texOffs(46,71).mirror().addBox(4.0F,-4.0F,4.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(5.0F,-4.0F,0.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(5.0F,-3.0F,2.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(4.5F,-5.0F,6.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(4.5F,-6.0F,7.5F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(4.5F,-7.0F,9.0F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(46,71).mirror().addBox(4.5F,-8.0F,10.5F,1.0F,1.0F,8.0F, new CubeDeformation(0.0F)).mirror(false)
                , PartPose.offsetAndRotation(0.0F,0.0F,0.0F,0.475F,0.275F,0.0F));


        PartDefinition head_feather = head.addOrReplaceChild("head_feather", CubeListBuilder.create()
                .texOffs(104, 62).addBox(0.0F,0.0F,2.5F,0.0F,18.0F,12.0F, new CubeDeformation(0.0F))
                , PartPose.offsetAndRotation(0.0F,-8.0F,0.0F,0.5F,0.0F,0.0F));



        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
