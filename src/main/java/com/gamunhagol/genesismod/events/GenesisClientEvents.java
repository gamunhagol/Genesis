package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.particle.GreenFlameParticle;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GenesisClientEvents {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // "GREEN_FLAME 파티클은 GreenFlameParticle의 모양(Provider)을 쓴다"고 등록
        event.registerSpriteSet(GenesisParticles.GREEN_FLAME.get(), GreenFlameParticle.Provider::new);
    }
}
