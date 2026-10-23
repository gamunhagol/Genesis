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
        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0.0f), 0.0F);
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition head = partDefinition.getChild("head");

        head.addOrReplaceChild("head_armor", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                        .texOffs(32, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("jaw_r1", CubeListBuilder.create()
                        .texOffs(40, 44).addBox(-6.0F, -33.0F, -4.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, 19.6F, 3.9F, 0.2007F, 0.0F, 0.0F));

        // 깃/바이저 장식 내피 (Y: 24.7F -> 21.7F)
        head.addOrReplaceChild("pitching_inner_r1", CubeListBuilder.create()
                        .texOffs(80, 44).addBox(-6.0F, -33.0F, -4.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.2F)),
                PartPose.offsetAndRotation(1.0F, 23.7F, 1.7F, 0.1134F, 0.0F, 0.0F));

        // 깃/바이저 장식 외피 (Y: 24.8F -> 21.8F)
        head.addOrReplaceChild("pitching_r1", CubeListBuilder.create()
                        .texOffs(0, 44).addBox(-6.0F, -33.0F, -4.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(1.0F, 23.8F, 1.7F, 0.1134F, 0.0F, 0.0F));

        // 날개 피벗 (Y: 24.7F -> 21.7F)
        PartDefinition wing = head.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(1.0F, 23.7F, 1.7F));

        wing.addOrReplaceChild("wing_left_r1", CubeListBuilder.create()
                        .texOffs(0, 96).mirror().addBox(-1.0F, -8.0F, -1.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(4.1F, -26.0F, 2.0F, 0.1752F, 0.0859F, 0.0152F));

        wing.addOrReplaceChild("wing_right_r1", CubeListBuilder.create()
                        .texOffs(0, 96).addBox(0.0F, -8.0F, -1.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.1F, -26.0F, 2.0F, 0.1752F, -0.0859F, -0.0152F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}