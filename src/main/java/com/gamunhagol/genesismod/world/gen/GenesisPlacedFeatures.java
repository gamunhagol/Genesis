package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class GenesisPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SILVER_ORE_PLACED_KEY = registerKey("silver_ore_placed");
    public static final ResourceKey<PlacedFeature> PEWRIESE_ORE_PLACED_KEY = registerKey("pewriese_ore_placed");

    public static final ResourceKey<PlacedFeature> PEWRIESE_ORE_DESERT_PLACED_KEY = registerKey("pewriese_ore_desert_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // 배치 규칙 설정
        register(context, SILVER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.SILVER_ORE_KEY),
                GenesisOrePlacement.commonOrePlacement(7, // 청크당 광맥 생성 시도 횟수
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64), // 생성 최저 높이
                                VerticalAnchor.absolute(32)   // 생성 최고 높이
                        )));

        register(context, PEWRIESE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_ORE_KEY),
                GenesisOrePlacement.rareOrePlacement(3,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-84), // 생성 최저 높이
                                VerticalAnchor.absolute(-32)   // 생성 최고 높이
                        )));

        register(context, PEWRIESE_ORE_DESERT_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_ORE_KEY),
                GenesisOrePlacement.commonOrePlacement(1,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-100),
                                VerticalAnchor.absolute(-16))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(GenesisMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}
