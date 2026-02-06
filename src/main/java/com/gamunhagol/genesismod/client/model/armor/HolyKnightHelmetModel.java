package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class HolyKnightHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "pewriese_holy_knight_helmet"), "main");

    public HolyKnightHelmetModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition head_armor = head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.8F))
                        .texOffs(32, 94).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_right_wing = head.addOrReplaceChild("head_right_wing", CubeListBuilder.create()
                        .texOffs(42, 78).addBox(-3.6F, -13.5F, 4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.15F, 0.0F));

        PartDefinition head_left_wing = head.addOrReplaceChild("head_left_wing", CubeListBuilder.create()
                        .texOffs(42, 78).mirror().addBox(3.6F, -13.5F, 4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.15F, 0.0F));

        PartDefinition head_feather = head.addOrReplaceChild("head_feather", CubeListBuilder.create()
                        .texOffs(3, 76).addBox(-6.0F, 0.0F, 1.0F, 12.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -8.5F, 4.0F, 0.20F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}