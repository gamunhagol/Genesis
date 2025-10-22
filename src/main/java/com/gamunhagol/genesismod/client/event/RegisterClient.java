package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterClient {


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

            ItemProperties.register(
                    GenesisItems.SPIRIT_COMPASS.get(),
                    new ResourceLocation(GenesisMod.MODID, "needle_type"),
                    (ItemStack stack, ClientLevel level, net.minecraft.world.entity.LivingEntity entity, int seed) -> {
                        if (!stack.hasTag()) return 0.0F;
                        String type = stack.getOrCreateTag().getString(SpiritCompassItem.KEY_NEEDLE_TYPE);
                        return switch (type) {
                            case "fire"      -> 0.1F;
                            case "water"     -> 0.2F;
                            case "earth"     -> 0.3F;
                            case "storm"     -> 0.4F;
                            case "lightning" -> 0.5F;
                            case "plants"    -> 0.6F;
                            case "ice"       -> 0.7F;
                            default          -> 0.0F;
                        };
                    }
            );


            ItemProperties.register(
                    GenesisItems.SPIRIT_COMPASS.get(),
                    new ResourceLocation(GenesisMod.MODID, "has_needle"),
                    (stack, level, entity, seed) ->
                            stack.getOrCreateTag().getBoolean(SpiritCompassItem.KEY_HAS_NEEDLE) ? 1.0F : 0.0F
            );


            ItemProperties.register(
                    GenesisItems.SPIRIT_COMPASS.get(),
                    new ResourceLocation(GenesisMod.MODID, "needle_color"),
                    (stack, level, entity, seed) -> {
                        String t = stack.getOrCreateTag().getString(SpiritCompassItem.KEY_NEEDLE_TYPE);
                        return switch (t) {
                            case "water"     -> 1.0F;
                            case "fire"      -> 2.0F;
                            case "earth"     -> 3.0F;
                            case "storm"     -> 4.0F;
                            case "lightning" -> 5.0F;
                            case "plants"    -> 6.0F;
                            case "ice"       -> 7.0F;
                            default          -> 0.0F;
                        };
                    }
            );
        });
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) return 0xFFFFFF;

            String t = stack.getOrCreateTag().getString(SpiritCompassItem.KEY_NEEDLE_TYPE);
            return switch (t) {
                case "fire"      -> 0xFF7377;
                case "water"     -> 0x628FFF;
                case "earth"     -> 0xFFBD79;
                case "storm"     -> 0x99C1D1;
                case "lightning" -> 0xF1FF7D;
                case "plants"    -> 0x7DFF87;
                case "ice"       -> 0x8ADAF2;
                default          -> 0xFFFFFF;
            };
        }, GenesisItems.SPIRIT_COMPASS.get());
    }
}

