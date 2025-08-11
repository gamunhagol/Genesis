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
                .texOffs(0,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(0.8F))
                        .texOffs(32,96).addBox(-4.0F,-8.0F,-4.0F,8.0F,8.0F,8.0F, new CubeDeformation(1.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition head_shade = head.addOrReplaceChild("head_shade", CubeListBuilder.create()
                .texOffs(0,62).addBox(-4.0F,0.0F,-6.0F,10.0F,8.0F,10.0F, new CubeDeformation(0.81F))
                        .texOffs(8,54).addBox(-3.0F,-3.0F,-3.0F,6.0F,2.0F,6.0F, new CubeDeformation(0.0F))
                        .texOffs(8,45).addBox(-3.0F,-3.0F,-3.0F,6.0F,3.0F,6.0F, new CubeDeformation(0.2F))
                        .texOffs(0,24).addBox(-9.0F,-8.0F,-3.0F,12.0F,8.0F,12.0F, new CubeDeformation(0.25F))
                        .texOffs(0,24).addBox(-7.0F,-4.0F,-5.0F,12.0F,8.0F,12.0F, new CubeDeformation(0.25F))
                , PartPose.offsetAndRotation(0.0F,-8.0F,0.0F,0.0F,0.8F,0.0F));

        PartDefinition head_feather = head.addOrReplaceChild("head_feather", CubeListBuilder.create()
                .texOffs(104, 62).addBox(0.0F,-1.0F,2.5F,0.0F,18.0F,12.0F, new CubeDeformation(0.0F))
                , PartPose.offsetAndRotation(0.0F,-8.0F,0.0F,0.5F,0.0F,0.0F));



        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
