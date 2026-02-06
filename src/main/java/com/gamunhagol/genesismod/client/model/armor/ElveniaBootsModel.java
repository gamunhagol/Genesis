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

public class ElveniaBootsModel <T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID, "elvenia_boots"), "main");

    public ElveniaBootsModel(ModelPart root) { super(root); }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 1.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition leftLeg = partdefinition.getChild(PartNames.LEFT_LEG);
        PartDefinition rightLeg = partdefinition.getChild(PartNames.RIGHT_LEG);

        leftLeg.addOrReplaceChild("left_boot", CubeListBuilder.create()
                        .texOffs(0, 80).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false),
                PartPose.ZERO);

        rightLeg.addOrReplaceChild("right_boot", CubeListBuilder.create()
                        .texOffs(0, 80).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

}
