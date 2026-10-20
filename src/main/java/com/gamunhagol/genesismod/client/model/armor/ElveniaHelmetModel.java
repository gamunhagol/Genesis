package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ElveniaHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "elvenia_helmet"), "main");

    public ElveniaHelmetModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 1.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("head_layer", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 64).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("head_2_r1", CubeListBuilder.create()
                        .texOffs(64, 64).addBox(-4.0F, -32.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.8F)),
                PartPose.offsetAndRotation(0.0F, 23.0F, 4.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition ear = head.addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 0.0F));

        ear.addOrReplaceChild("right_ear_r1", CubeListBuilder.create()
                        .texOffs(72, 91).mirror().addBox(-1.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(5.0F, -25.0F, -1.0F, 0.0F, -0.1309F, 0.0F));

        ear.addOrReplaceChild("left_ear_r1", CubeListBuilder.create()
                        .texOffs(72, 91).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-5.0F, -25.0F, -1.0F, 0.0F, 0.1309F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}
