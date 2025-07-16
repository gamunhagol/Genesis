package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.client.renderer.model.armor.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterModels {

    public static IsisPlateHelmetModel<?> ISIS_PLATE_HELMET_MODEL = null;
    public static IsisPlateChestplateModel<?> ISIS_PLATE_CHESTPLATE_MODEL = null;
    public static IsisPlateLeggingsModel<?> ISIS_PLATE_LEGGINGS_MODEL = null;
    public static IsisPlateBootsModel<?> ISIS_PLATE_BOOTS_MODEL = null;

    public static IsisHolyKnightHelmetModel<?> ISIS_HOLY_KNIGHT_HELMET_MODEL = null;
    public static IsisHolyKnightChestplateModel<?> ISIS_HOLY_KNIGHT_CHESTPLATE_MODEL = null;
    public static IsisHolyKnightLeggingsModel<?> ISIS_HOLY_KNIGHT_LEGGINGS_MODEL = null;
    public static IsisHolyKnightBootsModel<?> ISIS_HOLY_KNIGHT_BOOTS_MODEL = null;


    public static void checkForInitModels(){
        if (ISIS_PLATE_HELMET_MODEL != null) return;

        EntityModelSet mcModels = Minecraft.getInstance().getEntityModels();

        ISIS_PLATE_HELMET_MODEL = new IsisPlateHelmetModel<>(mcModels.bakeLayer(IsisPlateHelmetModel.LAYER_LOCATION));
        ISIS_PLATE_CHESTPLATE_MODEL = new IsisPlateChestplateModel<>(mcModels.bakeLayer(IsisPlateChestplateModel.LAYER_LOCATION));
        ISIS_PLATE_LEGGINGS_MODEL = new IsisPlateLeggingsModel<>(mcModels.bakeLayer(IsisPlateLeggingsModel.LAYER_LOCATION));
        ISIS_PLATE_BOOTS_MODEL = new IsisPlateBootsModel<>(mcModels.bakeLayer(IsisPlateBootsModel.LAYER_LOCATION));

        ISIS_HOLY_KNIGHT_HELMET_MODEL = new IsisHolyKnightHelmetModel<>(mcModels.bakeLayer(IsisHolyKnightHelmetModel.LAYER_LOCATION));
        ISIS_HOLY_KNIGHT_CHESTPLATE_MODEL = new IsisHolyKnightChestplateModel<>(mcModels.bakeLayer(IsisHolyKnightChestplateModel.LAYER_LOCATION));
        ISIS_HOLY_KNIGHT_LEGGINGS_MODEL = new IsisHolyKnightLeggingsModel<>(mcModels.bakeLayer(IsisHolyKnightLeggingsModel.LAYER_LOCATION));
        ISIS_HOLY_KNIGHT_BOOTS_MODEL = new IsisHolyKnightBootsModel<>(mcModels.bakeLayer(IsisHolyKnightBootsModel.LAYER_LOCATION));


    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){

        event.registerLayerDefinition(IsisPlateHelmetModel.LAYER_LOCATION, IsisPlateHelmetModel::createArmorLayer);
        event.registerLayerDefinition(IsisPlateChestplateModel.LAYER_LOCATION, IsisPlateChestplateModel::createArmorLayer);
        event.registerLayerDefinition(IsisPlateLeggingsModel.LAYER_LOCATION, IsisPlateLeggingsModel::createArmorLayer);
        event.registerLayerDefinition(IsisPlateBootsModel.LAYER_LOCATION, IsisPlateBootsModel::createArmorLayer);

        event.registerLayerDefinition(IsisHolyKnightHelmetModel.LAYER_LOCATION, IsisHolyKnightHelmetModel::createArmorLayer);
        event.registerLayerDefinition(IsisHolyKnightChestplateModel.LAYER_LOCATION, IsisHolyKnightChestplateModel::createArmorLayer);
        event.registerLayerDefinition(IsisHolyKnightLeggingsModel.LAYER_LOCATION, IsisHolyKnightLeggingsModel::createArmorLayer);
        event.registerLayerDefinition(IsisHolyKnightBootsModel.LAYER_LOCATION, IsisHolyKnightBootsModel::createArmorLayer);
    }
}
