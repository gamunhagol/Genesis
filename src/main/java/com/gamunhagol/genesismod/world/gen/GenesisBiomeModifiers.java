package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class GenesisBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_ORE = registerKey("add_pewriese_ore");
    public static final ResourceKey<BiomeModifier> ADD_PYULITELA_ORE = registerKey("add_pyulitela_ore");

    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_ORE_DESERT = registerKey("add_pewriese_ore_desert");

    public static final ResourceKey<BiomeModifier> ADD_AMETHYST_TREE = registerKey("add_amethyst_tree");

    public static final ResourceKey<BiomeModifier> ADD_CITRINE_GEODE = registerKey("add_citrine_geode");
    public static final ResourceKey<BiomeModifier> ADD_RED_CRYSTAL_GEODE = registerKey("add_red_crystal_geode");
    public static final ResourceKey<BiomeModifier> ADD_BLUE_CRYSTAL_GEODE = registerKey("add_blue_crystal_geode");
    public static final ResourceKey<BiomeModifier> ADD_GREEN_AMBER_GEODE = registerKey("add_green_amber_geode");
    public static final ResourceKey<BiomeModifier> ADD_WIND_STONE_GEODE = registerKey("add_wind_stone_geode");
    public static final ResourceKey<BiomeModifier> ADD_LIGHTING_CRYSTAL_GEODE = registerKey("add_lighting_crystal_geode");
    public static final ResourceKey<BiomeModifier> ADD_ICE_FLOWER_GEODE = registerKey("add_ice_flower_geode");

    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_METEORITE = registerKey("add_pewriese_meteorite");
    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_SUBSPECIES_METEORITE = registerKey("add_pewriese_meteorite_subspecies");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_PEWRIESE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.PEWRIESE_ORE_PLACED_KEY)), // 배치 규칙 연결
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        context.register(ADD_PEWRIESE_ORE_DESERT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DESERT)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.PEWRIESE_ORE_DESERT_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_PYULITELA_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.PYULITELA_ORE_PLACED_KEY)), // 배치 규칙 연결
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));


        context.register(ADD_AMETHYST_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), // 모든 오버월드 바이옴 대상
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.AMETHYST_TREE_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_CITRINE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.CITRINE_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_RED_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.RED_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_BLUE_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.WARM_OCEAN)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.BLUE_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_GREEN_AMBER_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.GREEN_AMBER_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_LIGHTING_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DESERT)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.LIGHTING_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_WIND_STONE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.JAGGED_PEAKS),
                        biomes.getOrThrow(Biomes.FROZEN_PEAKS),
                        biomes.getOrThrow(Biomes.STONY_PEAKS)
                ),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.WIND_STONE_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_ICE_FLOWER_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.ICE_SPIKES)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.ICE_FLOWER_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));


        context.register(ADD_PEWRIESE_METEORITE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.PEWRIESE_METEORITE_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));
        context.register(ADD_PEWRIESE_SUBSPECIES_METEORITE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.PEWRIESE_METEORITE_SUBSPECIES_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(GenesisMod.MODID, name));
    }
}
