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
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("head_layer", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 96).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear = head.addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));

        ear.addOrReplaceChild("right_ear_r1", CubeListBuilder.create()
                        .texOffs(72, 120).mirror().addBox(-1.0F, -7.0F, -1.0F, 13.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(3.4F, -23.1F, -5.3F, 0.9798F, -1.2563F, -0.9875F));

        ear.addOrReplaceChild("left_ear_r1", CubeListBuilder.create()
                        .texOffs(72, 120).addBox(-12.0F, -7.0F, -1.0F, 13.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-3.4F, -23.1F, -5.3F, 0.9798F, 1.2563F, 0.9875F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
