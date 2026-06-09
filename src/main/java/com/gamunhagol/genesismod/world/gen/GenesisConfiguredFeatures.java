package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisFeatures;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.gen.feature.configurations.GenesisCrystalConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class GenesisConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEWRIESE_ORE_KEY = registerKey("pewriese_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PYULITELA_ORE_KEY = registerKey("pyulitela_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_TREE_KEY = registerKey("amethyst_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CITRINE_GEODE_KEY = registerKey("citrine_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_CRYSTAL_GEODE_KEY = registerKey("red_crystal_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_CRYSTAL_GEODE_KEY = registerKey("blue_crystal_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GREEN_AMBER_GEODE_KEY = registerKey("green_amber_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WIND_STONE_GEODE_KEY = registerKey("wind_stone_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHTING_CRYSTAL_GEODE_KEY = registerKey("lighting_crystal_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_FLOWER_GEODE_KEY = registerKey("ice_flower_geode");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PEWRIESE_METEORITE_KEY = registerKey("pewriese_meteorite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEWRIESE_METEORITE_SUBSPECIES_KEY = registerKey("pewriese_meteorite_subspecies");



    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        TagMatchTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);



        register(context, PEWRIESE_ORE_KEY, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stoneReplaceables, GenesisBlocks.PEWRIESE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GenesisBlocks.PEWRIESE_ORE.get().defaultBlockState())
        ), 3));

        register(context, PYULITELA_ORE_KEY, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stoneReplaceables, GenesisBlocks.PYULITELA_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GenesisBlocks.PYULITELA_ORE.get().defaultBlockState())
        ), 2));


        register(context, AMETHYST_TREE_KEY, GenesisFeatures.AMETHYST_TREE.get(), NoneFeatureConfiguration.INSTANCE);

        register(context, CITRINE_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "citrine_geode"),
                2,
                GenesisBlocks.CITRINE_CLUSTER.get().defaultBlockState(),
                0.4f,
                -2
        ));
        register(context, RED_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "red_crystal_geode"),
                2,
                GenesisBlocks.RED_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.6f,
                -1
        ));
        register(context, BLUE_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "blue_crystal_geode"),
                2,
                GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.3f,
                -1
        ));
        register(context, GREEN_AMBER_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "green_amber_geode"),
                2,
                GenesisBlocks.GREEN_AMBER_CLUSTER.get().defaultBlockState(),
                0.5f,
                0
        ));
        register(context, WIND_STONE_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "wind_stone_geode"),
                2,
                GenesisBlocks.WIND_STONE_CLUSTER.get().defaultBlockState(),
                0.4f,
                0
        ));
        register(context, LIGHTING_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "lighting_crystal_geode"),
                2,
                GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.7f,
                0
        ));
        register(context, ICE_FLOWER_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "ice_flower_geode"),
                2,
                GenesisBlocks.ICE_FLOWER_CLUSTER.get().defaultBlockState(),
                0.5f,
                0
        ));


        register(context, PEWRIESE_METEORITE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "pewriese_meteorite"),
                1,
                Blocks.AIR.defaultBlockState(),
                0.0f,
                -1
        ));
        register(context, PEWRIESE_METEORITE_SUBSPECIES_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "pewriese_meteorite_subspecies"),
                1,
                Blocks.AIR.defaultBlockState(),
                0.0f,
                -1
        ));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(GenesisMod.MODID, name));
    }

    private static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
            , F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                   ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
