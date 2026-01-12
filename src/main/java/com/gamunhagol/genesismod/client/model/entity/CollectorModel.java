package com.gamunhagol.genesismod.client.model.entity;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class CollectorModel<T extends LivingEntity> extends VillagerModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("genesis","collector"),"main");

    public CollectorModel(ModelPart pRoot) {
        super(pRoot);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = VillagerModel.createBodyModel();
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 핵심: super를 호출해야 주민의 기본 애니메이션(걷기, 팔 흔들기 등)이 작동합니다.
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public ModelPart root() {
        return super.root();
    }
}
