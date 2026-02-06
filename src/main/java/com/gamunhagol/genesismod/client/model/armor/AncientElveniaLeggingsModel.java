package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class AncientElveniaLeggingsModel <T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "ancient_elvenia_leggings"), "main");

    public AncientElveniaLeggingsModel(ModelPart root) { super(root); }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.5F), 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.getChild("body");
        PartDefinition right_leg = partdefinition.getChild("right_leg");
        PartDefinition left_leg = partdefinition.getChild("left_leg");

        body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16, 80).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.7F))
                .texOffs(0, 96).addBox(-4.0F, 7.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.8F)), PartPose.ZERO);

        right_leg.addOrReplaceChild("right_thigh", CubeListBuilder.create()
                .texOffs(0, 80).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

        left_leg.addOrReplaceChild("left_thigh", CubeListBuilder.create()
                .texOffs(0, 80).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
