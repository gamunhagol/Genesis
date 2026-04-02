package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.particle.GreenFlameParticle;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.init.ModKeyBindings;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GenesisClientEvents {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // "GREEN_FLAME 파티클은 GreenFlameParticle의 모양(Provider)을 쓴다"고 등록
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
        // GREAT_BOW 아이템에 'pulling' (당기는 중인지 여부) 속성 등록
        ItemProperties.register(
                GenesisItems.GREAT_BOW.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) -> {
                    return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
                }
        );

        // GREAT_BOW 아이템에 'pull' (얼마나 당겼는지 수치 0~1) 속성 등록
        ItemProperties.register(
                GenesisItems.GREAT_BOW.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }
                    // 현재 사용 중인 아이템이 활이 아니면 0
                    if (entity.getUseItem() != stack) {
                        return 0.0F;
                    }
                    // (최대 사용 시간 - 남은 시간) / 20틱(1초) -> 바닐라 활 기준 완충 시간
                    return (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 28.0F;
                }
        );
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(ModKeyBindings.LEVEL_UP_KEY);
    }
}
