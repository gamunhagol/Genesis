package com.gamunhagol.genesismod.client.model.entity;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CollectorModel<T extends LivingEntity> extends VillagerModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(GenesisMod.MODID,"collector"),"main");

    public CollectorModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = VillagerModel.createBodyModel();
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public ModelPart root() {
        return super.root();
    }
}
