package com.gamunhagol.genesismod.client.model.armor;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ElveniaHelmetModel <T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "elvenia_helmet"), "main");

    public ElveniaHelmetModel(ModelPart root) { super(root); }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 1.0F);
        PartDefinition partDefinition = meshdefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("helmet_main", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 64).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
                , PartPose.offset(0.0F,0.0F,0.0F));

        head.addOrReplaceChild("left_eararmor", CubeListBuilder.create()
                        .texOffs(40, 96).mirror().addBox(-1.0F, -3.0F, -3.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, 0.0F, -0.3054F, 0.0F));

        head.addOrReplaceChild("right_eararmor", CubeListBuilder.create()
                        .texOffs(40, 96).addBox(-4.0F, -3.0F, -3.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, -4.0F, 0.0F, 0.0F, 0.3054F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
