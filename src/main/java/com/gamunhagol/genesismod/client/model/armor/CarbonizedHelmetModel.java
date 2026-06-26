package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CarbonizedHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "carbonized_helmet"), "main");

    public CarbonizedHelmetModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        PartDefinition helmet_main = head.addOrReplaceChild("helmet_main", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 96).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear_r1 = head.addOrReplaceChild("ear_r1", CubeListBuilder.create()
                        .texOffs(56, 106).addBox(-4.0F, -33.5F, -4.0F, 8.0F, 9.0F, 13.0F, new CubeDeformation(1.25F)),
                PartPose.offsetAndRotation(0.0F, 23.0F, 4.0F, 0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}