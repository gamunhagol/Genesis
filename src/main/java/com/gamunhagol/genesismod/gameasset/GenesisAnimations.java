package com.gamunhagol.genesismod.gameasset;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.ActionAnimation;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisAnimations {

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        // "genesis" 네임스페이스로 빌더 생성
        event.newBuilder("genesis", GenesisAnimations::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {

    }
}