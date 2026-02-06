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

public class ElveniaChestplateModel <T extends LivingEntity> extends HumanoidModel<T>{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "elvenia_chestplate"), "main");

    public ElveniaChestplateModel(ModelPart root) { super(root); }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.getChild(PartNames.BODY);
        PartDefinition leftArm = partdefinition.getChild(PartNames.LEFT_ARM);
        PartDefinition rightArm = partdefinition.getChild(PartNames.RIGHT_ARM);

        body.addOrReplaceChild("body_armor", CubeListBuilder.create()
                .texOffs(16, 80).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.ZERO);

        leftArm.addOrReplaceChild("left_arm_armor", CubeListBuilder.create()
                .texOffs(40, 80).mirror().addBox(-1.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.ZERO);

        rightArm.addOrReplaceChild("right_arm_armor", CubeListBuilder.create()
                .texOffs(40, 80).addBox(-3.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
