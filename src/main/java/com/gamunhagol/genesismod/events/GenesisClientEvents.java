package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.particle.GreenFlameParticle;
import com.gamunhagol.genesismod.events.client.ClientTooltipHandler;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.init.ModKeyBindings;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketChangeSelectedSlot;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.TickEvent; // [추가]
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GenesisClientEvents {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GenesisParticles.GREEN_FLAME.get(), GreenFlameParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(GenesisBlocks.OBLIVION_CANDLE.get(), RenderType.cutout());
            ClientTooltipHandler.init();
            registerBowProperties();
        });
    }

    private static void registerBowProperties() {
        ItemProperties.register(
                GenesisItems.GREAT_BOW.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );

        ItemProperties.register(
                GenesisItems.GREAT_BOW.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null || entity.getUseItem() != stack) return 0.0F;
                    return (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 28.0F;
                }
        );
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBindings.LEVEL_UP_KEY);
        event.register(ModKeyBindings.SPELL_PREV_KEY);
        event.register(ModKeyBindings.SPELL_NEXT_KEY);
    }

    // 포지 이벤트 버스 (인게임 이벤트를 받는 곳)
    @Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBusEvents {

        @SubscribeEvent
        public static void onComputeFovModifier(ComputeFovModifierEvent event) {
            if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().is(GenesisItems.GREAT_BOW.get())) {
                int i = event.getPlayer().getTicksUsingItem();
                float f = (float)i / 38.0F;
                if (f > 1.0F) {
                    f = 1.0F;
                } else {
                    f *= f;
                }
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f * 0.15F));
            }
        }

        // 방향키 단축키 입력을 매 틱마다 감지하는 핸들러
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            // 중복 실행 방지를 위해 클라이언트 틱 종료 시점에만 1번 연산
            if (event.phase != TickEvent.Phase.END) return;
            // 위 방향키 입력 시 (-1을 보내서 이전 슬롯으로 이동 요청)
            while (ModKeyBindings.SPELL_PREV_KEY.consumeClick()) {
                GenesisNetwork.sendToServer(new PacketChangeSelectedSlot(-1));
            }
            // 아래 방향키 입력 시 (+1을 보내서 다음 슬롯으로 이동 요청)
            while (ModKeyBindings.SPELL_NEXT_KEY.consumeClick()) {
                GenesisNetwork.sendToServer(new PacketChangeSelectedSlot(1));
            }
        }
    }
}