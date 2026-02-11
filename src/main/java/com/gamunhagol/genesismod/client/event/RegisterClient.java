package com.gamunhagol.genesismod.client.event;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.DivineGrailItem;
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

            //  정령 속성별 모델 변경용 프리디케이트
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
            //  성배병 잔량 시각화용 프리디케이트
            ItemProperties.register(
                    GenesisItems.DIVINE_GRAIL.get(), // 등록하신 성배병 아이템
                    new ResourceLocation(GenesisMod.MODID, "fill_level"),
                    (stack, level, entity, seed) -> {
                        if (stack.getItem() instanceof DivineGrailItem grail) {
                            int uses = grail.getUses(stack);
                            int max = grail.getMaxUses(stack);

                            if (uses <= 0) return 0.0F; // 텅 빈 (empty)

                            float ratio = (float) uses / (float) max;

                            // JSON 모델의 overrides 수치와 매칭
                            if (ratio <= 0.25F) return 0.25F; // 거의 다 마심
                            if (ratio <= 0.50F) return 0.50F; // 절반
                            if (ratio <= 0.75F) return 0.75F; // 조금 마심
                            return 1.0F; // 꽉 참
                        }
                        return 0.0F;
                    }
            );
        });
    }


}
