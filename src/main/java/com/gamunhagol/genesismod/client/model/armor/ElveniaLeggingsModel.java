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

public class ElveniaLeggingsModel <T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "elvenia_leggings"), "main");

    public ElveniaLeggingsModel(ModelPart root) { super(root); }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.getChild(PartNames.BODY);
        PartDefinition leftLeg = partdefinition.getChild(PartNames.LEFT_LEG);
        PartDefinition rightLeg = partdefinition.getChild(PartNames.RIGHT_LEG);

        body.addOrReplaceChild("waist_armor", CubeListBuilder.create()
                .texOffs(16, 80).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.ZERO);

        leftLeg.addOrReplaceChild("left_leg_armor", CubeListBuilder.create()
                .texOffs(0, 80).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_leg_armor", CubeListBuilder.create()
                .texOffs(0, 80).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
