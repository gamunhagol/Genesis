package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class GenesisPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SILVER_ORE_PLACED_KEY = registerKey("silver_ore_placed");
    public static final ResourceKey<PlacedFeature> PEWRIESE_ORE_PLACED_KEY = registerKey("pewriese_ore_placed");
    public static final ResourceKey<PlacedFeature> PEWRIESE_ORE_DESERT_PLACED_KEY = registerKey("pewriese_ore_desert_placed");
    public static final ResourceKey<PlacedFeature> PYULITELA_ORE_PLACED_KEY = registerKey("pyulitela_ore_placed");

    public static final ResourceKey<PlacedFeature> AMETHYST_TREE_PLACED_KEY = registerKey("amethyst_tree_placed");

    public static final ResourceKey<PlacedFeature> CITRINE_GEODE_PLACED_KEY = registerKey("citrine_geode_placed");
    public static final ResourceKey<PlacedFeature> RED_CRYSTAL_GEODE_PLACED_KEY = registerKey("red_crystal_geode_placed");
    public static final ResourceKey<PlacedFeature> BLUE_CRYSTAL_GEODE_PLACED_KEY = registerKey("blue_crystal_geode_placed");
    public static final ResourceKey<PlacedFeature> GREEN_AMBER_GEODE_PLACED_KEY = registerKey("green_amber_geode_placed");
    public static final ResourceKey<PlacedFeature> WIND_STONE_GEODE_PLACED_KEY = registerKey("wind_stone_geode_placed");
    public static final ResourceKey<PlacedFeature> LIGHTING_CRYSTAL_GEODE_PLACED_KEY = registerKey("lighting_crystal_geode_placed");
    public static final ResourceKey<PlacedFeature> ICE_FLOWER_GEODE_PLACED_KEY = registerKey("ice_flower_geode_placed");

    public static final ResourceKey<PlacedFeature> PEWRIESE_METEORITE_PLACED_KEY = registerKey("pewriese_meteorite_placed");
    public static final ResourceKey<PlacedFeature> PEWRIESE_METEORITE_SUBSPECIES_PLACED_KEY = registerKey("pewriese_meteorite_subspecies_placed");


    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SILVER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.SILVER_ORE_KEY),
                GenesisOrePlacement.commonOrePlacement(7,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(32)
                        )));

        register(context, PEWRIESE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_ORE_KEY),
                GenesisOrePlacement.rareOrePlacement(3,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-84),
                                VerticalAnchor.absolute(-32)
                        )));

        register(context, PEWRIESE_ORE_DESERT_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_ORE_KEY),
                GenesisOrePlacement.commonOrePlacement(1,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-100),
                                VerticalAnchor.absolute(-16))));

        register(context, PYULITELA_ORE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PYULITELA_ORE_KEY),
                GenesisOrePlacement.rareOrePlacement(35,
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-160),
                                VerticalAnchor.absolute(64))));

        register(context, AMETHYST_TREE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.AMETHYST_TREE_KEY),
                List.of(
                        CountPlacement.of(25),
                        RarityFilter.onAverageOnceEvery(55),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(50)),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        BiomeFilter.biome()
                ));

        register(context, RED_CRYSTAL_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.RED_CRYSTAL_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(65),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-10)),
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.LAVA)),
                        BiomeFilter.biome()
                ));
        register(context, BLUE_CRYSTAL_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.BLUE_CRYSTAL_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(85),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER)),
                        BiomeFilter.biome()
                ));
        register(context, WIND_STONE_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.WIND_STONE_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(155),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));
        register(context, CITRINE_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.CITRINE_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(95),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(30)),
                        BiomeFilter.biome()
                ));
        register(context, GREEN_AMBER_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.GREEN_AMBER_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(245),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));
        register(context, LIGHTING_CRYSTAL_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.LIGHTING_CRYSTAL_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(205),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));
        register(context, ICE_FLOWER_GEODE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.ICE_FLOWER_GEODE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(95),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));


        register(context, PEWRIESE_METEORITE_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_METEORITE_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(525),
                        InSquarePlacement.spread(),
                        // 지상(World Surface)에 배치
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));
        register(context, PEWRIESE_METEORITE_SUBSPECIES_PLACED_KEY, configuredFeatures.getOrThrow(GenesisConfiguredFeatures.PEWRIESE_METEORITE_SUBSPECIES_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(1750),
                        InSquarePlacement.spread(),
                        // 지상(World Surface)에 배치
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                ));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(GenesisMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}