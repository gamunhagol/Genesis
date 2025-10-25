package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

            // 🔹 정령 속성별 모델 변경용 프리디케이트
            ItemProperties.register(
                    GenesisItems.SPIRIT_COMPASS.get(),
                    new ResourceLocation(GenesisMod.MODID, "needle_type"),
                    (stack, level, entity, seed) -> {
                        String t = stack.getOrCreateTag().getString(SpiritCompassItem.KEY_NEEDLE_TYPE);
                        return switch (t) {
                            case "fire" -> 0.1F;
                            case "water" -> 0.2F;
                            case "earth" -> 0.3F;
                            case "storm" -> 0.4F;
                            case "lightning" -> 0.5F;
                            case "plants" -> 0.6F;
                            case "ice" -> 0.7F;
                            default -> 0.0F;
                        };
                    }
            );
        });
    }
}
