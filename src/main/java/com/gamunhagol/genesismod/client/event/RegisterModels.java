package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.client.model.armor.*;
import com.gamunhagol.genesismod.client.model.entity.CollectorModel;
import com.gamunhagol.genesismod.client.renderer.entity.vanilla.CollectorGuardRenderer;
import com.gamunhagol.genesismod.client.renderer.entity.vanilla.CollectorRenderer;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.client.ModModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterModels {

    public static PewriesePlateHelmetModel<?> PEWRIESE_PLATE_HELMET_MODEL = null;
    public static PewriesePlateChestplateModel<?> PEWRIESE_PLATE_CHESTPLATE_MODEL = null;
    public static PewriesePlateLeggingsModel<?> PEWRIESE_PLATE_LEGGINGS_MODEL = null;
    public static PewriesePlateBootsModel<?> PEWRIESE_PLATE_BOOTS_MODEL = null;

    public static HolyKnightHelmetModel<?> HOLY_KNIGHT_HELMET_MODEL = null;
    public static HolyKnightChestplateModel<?> HOLY_KNIGHT_CHESTPLATE_MODEL = null;
    public static HolyKnightLeggingsModel<?> HOLY_KNIGHT_LEGGINGS_MODEL = null;
    public static HolyKnightBootsModel<?> HOLY_KNIGHT_BOOTS_MODEL = null;


    public static void checkForInitModels(){
        if (PEWRIESE_PLATE_HELMET_MODEL != null) return;

        EntityModelSet mcModels = Minecraft.getInstance().getEntityModels();

        PEWRIESE_PLATE_HELMET_MODEL = new PewriesePlateHelmetModel<>(mcModels.bakeLayer(PewriesePlateHelmetModel.LAYER_LOCATION));
        PEWRIESE_PLATE_CHESTPLATE_MODEL = new PewriesePlateChestplateModel<>(mcModels.bakeLayer(PewriesePlateChestplateModel.LAYER_LOCATION));
        PEWRIESE_PLATE_LEGGINGS_MODEL = new PewriesePlateLeggingsModel<>(mcModels.bakeLayer(PewriesePlateLeggingsModel.LAYER_LOCATION));
        PEWRIESE_PLATE_BOOTS_MODEL = new PewriesePlateBootsModel<>(mcModels.bakeLayer(PewriesePlateBootsModel.LAYER_LOCATION));

        HOLY_KNIGHT_HELMET_MODEL = new HolyKnightHelmetModel<>(mcModels.bakeLayer(HolyKnightHelmetModel.LAYER_LOCATION));
        HOLY_KNIGHT_CHESTPLATE_MODEL = new HolyKnightChestplateModel<>(mcModels.bakeLayer(HolyKnightChestplateModel.LAYER_LOCATION));
        HOLY_KNIGHT_LEGGINGS_MODEL = new HolyKnightLeggingsModel<>(mcModels.bakeLayer(HolyKnightLeggingsModel.LAYER_LOCATION));
        HOLY_KNIGHT_BOOTS_MODEL = new HolyKnightBootsModel<>(mcModels.bakeLayer(HolyKnightBootsModel.LAYER_LOCATION));


    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){

        //armor
        event.registerLayerDefinition(PewriesePlateHelmetModel.LAYER_LOCATION, PewriesePlateHelmetModel::createArmorLayer);
        event.registerLayerDefinition(PewriesePlateChestplateModel.LAYER_LOCATION, PewriesePlateChestplateModel::createArmorLayer);
        event.registerLayerDefinition(PewriesePlateLeggingsModel.LAYER_LOCATION, PewriesePlateLeggingsModel::createArmorLayer);
        event.registerLayerDefinition(PewriesePlateBootsModel.LAYER_LOCATION, PewriesePlateBootsModel::createArmorLayer);

        event.registerLayerDefinition(HolyKnightHelmetModel.LAYER_LOCATION, HolyKnightHelmetModel::createArmorLayer);
        event.registerLayerDefinition(HolyKnightChestplateModel.LAYER_LOCATION, HolyKnightChestplateModel::createArmorLayer);
        event.registerLayerDefinition(HolyKnightLeggingsModel.LAYER_LOCATION, HolyKnightLeggingsModel::createArmorLayer);
        event.registerLayerDefinition(HolyKnightBootsModel.LAYER_LOCATION, HolyKnightBootsModel::createArmorLayer);

        //entity
        event.registerLayerDefinition(ModModelLayers.COLLECTOR_LAYER, CollectorModel::createLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 엔티티와 렌더러를 연결합니다.
        event.registerEntityRenderer(GenesisEntities.COLLECTOR.get(), CollectorRenderer::new);
        event.registerEntityRenderer(GenesisEntities.COLLECTOR_GUARD.get(), CollectorGuardRenderer::new);
    }
}
