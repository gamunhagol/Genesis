package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class AncientElveniaChestplateModel <T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "ancient_elvenia_chestplate"), "main");

    public AncientElveniaChestplateModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition body = partDefinition.getChild("body");
        PartDefinition right_arm = partDefinition.getChild("right_arm");
        PartDefinition left_arm = partDefinition.getChild("left_arm");

        PartDefinition chest_main = body.addOrReplaceChild("chest_main", CubeListBuilder.create()
                        .texOffs(16, 80).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)),
                PartPose.ZERO);

        PartDefinition right_arm_armor = right_arm.addOrReplaceChild("right_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 80).addBox(-3.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F))
                        .texOffs(24, 96).addBox(-4.0F, -2.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(1.0F)),
                PartPose.ZERO);

        PartDefinition left_arm_armor = left_arm.addOrReplaceChild("left_arm_armor", CubeListBuilder.create()
                        .texOffs(40, 80).mirror().addBox(-1.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
                        .texOffs(24, 96).mirror().addBox(0.0F, -2.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false),
                PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
