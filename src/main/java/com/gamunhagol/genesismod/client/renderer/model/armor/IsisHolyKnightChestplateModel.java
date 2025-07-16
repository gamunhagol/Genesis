package com.gamunhagol.genesismod.client.renderer.model.armor;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IsisHolyKnightChestplateModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"isis_holy_knight_chestplate"),"main");

    public IsisHolyKnightChestplateModel(ModelPart pRoot) {
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

        PartDefinition outside_rid = body.addOrReplaceChild("outside_rid", CubeListBuilder.create()
                .texOffs(72,90).addBox(-5.0F,-19.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.05F))
                        .texOffs(72,62).addBox(-5.0F,-20.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.15F))
                        .texOffs(72,62).addBox(-5.0F,-23.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.25F))
                        .texOffs(72,62).addBox(-5.0F,-26.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.35F))
                        .texOffs(72,36).addBox(-5.0F,-27.0F,-3.0F,10.0F,6.0F,6.0F, new CubeDeformation(0.25F))
                , PartPose.offset(0.0F,24.0F,0.0F));

        PartDefinition inside_rid = body.addOrReplaceChild("inside_rid", CubeListBuilder.create()
                .texOffs(72,76).addBox(-5.0F,-19.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.0F))
                        .texOffs(72,48).addBox(-5.0F,-20.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.1F))
                        .texOffs(72,48).addBox(-5.0F,-23.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.2F))
                        .texOffs(72,48).addBox(-5.0F,-26.0F,-3.0F,10.0F,8.0F,6.0F, new CubeDeformation(0.3F))
                , PartPose.offset(0.0F,24.0F,0.0F));



        PartDefinition right_armor = right_arm.addOrReplaceChild("right_armor", CubeListBuilder.create()
                .texOffs(40,112).addBox(-3.25F,-0.2F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.6F))
                        .texOffs(39,79).addBox(-3.25F,6.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.7F))
                        .texOffs(39,79).addBox(-3.25F,4.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.8F))
                        .texOffs(39,79).addBox(-3.25F,2.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.9F))
                , PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition right_shoulder = right_arm.addOrReplaceChild("right_shoulder", CubeListBuilder.create()
                .texOffs(23,81).addBox(-3.25F,-2.2F,-2.0F,4.0F,4.0F,4.0F, new CubeDeformation(1.5F))
                        .texOffs(21,73).addBox(-3.25F,-2.2F,-2.0F,6.0F,4.0F,4.0F, new CubeDeformation(1.6F))
                        .texOffs(21,73).addBox(-3.25F,-4.2F,-2.0F,6.0F,4.0F,4.0F, new CubeDeformation(1.7F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        PartDefinition left_armor = left_arm.addOrReplaceChild("left_armor", CubeListBuilder.create()
                .texOffs(40,112).mirror().addBox(-0.75F,-0.2F,-2.0F,4.0F,12.0F,4.0F, new CubeDeformation(0.6F)).mirror(false)
                        .texOffs(39,79).mirror().addBox(-1.75F,6.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.7F)).mirror(false)
                        .texOffs(39,79).mirror().addBox(-1.75F,4.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.8F)).mirror(false)
                        .texOffs(39,79).mirror().addBox(-1.75F,2.2F,-2.0F,5.0F,6.0F,4.0F, new CubeDeformation(0.9F)).mirror(false)
                , PartPose.offset(0.0F, -1.5F, 0.0F));

        PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", CubeListBuilder.create()
                        .texOffs(23,81).mirror().addBox(-0.75F,-2.2F,-2.0F,4.0F,4.0F,4.0F, new CubeDeformation(1.5F)).mirror(false)
                        .texOffs(21,73).mirror().addBox(-2.75F,-2.2F,-2.0F,6.0F,4.0F,4.0F, new CubeDeformation(1.6F)).mirror(false)
                        .texOffs(21,73).mirror().addBox(-2.75F,-4.2F,-2.0F,6.0F,4.0F,4.0F, new CubeDeformation(1.7F)).mirror(false)
                , PartPose.offset(0.0F,0.0F,0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
