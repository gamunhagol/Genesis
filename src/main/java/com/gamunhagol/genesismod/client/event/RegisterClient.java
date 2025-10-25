package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.SpiritCompassItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

            // 🔹 침 색상 모델 전환용
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

            // 🔹 구조물 추적용 Compass 함수 등록
            CompassItemPropertyFunction.CompassTarget targetProvider = new CompassItemPropertyFunction.CompassTarget() {
                @Override
                public @Nullable GlobalPos getPos(ClientLevel level, ItemStack stack, @Nullable Entity entity) {
                    return SpiritCompassItem.getCompassTarget(stack, level);
                }
            };

            // 🔹 핵심! angle 회전 로직 (바닐라 로직 그대로 활용)
            ItemProperties.register(
                    GenesisItems.SPIRIT_COMPASS.get(),
                    new ResourceLocation("angle"),
                    new CompassItemPropertyFunction(targetProvider)
            );
        });
    }

    // 🔹 침 색상 틴트 (아이콘 렌더용)
    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) return 0xFFFFFF;
            String t = stack.getOrCreateTag().getString(SpiritCompassItem.KEY_NEEDLE_TYPE);
            return switch (t) {
                case "fire" -> 0xFF7377;
                case "water" -> 0x628FFF;
                case "earth" -> 0xFFBD79;
                case "storm" -> 0x99C1D1;
                case "lightning" -> 0xF1FF7D;
                case "plants" -> 0x7DFF87;
                case "ice" -> 0x8ADAF2;
                default -> 0xFFFFFF;
            };
        }, GenesisItems.SPIRIT_COMPASS.get());
    }
}