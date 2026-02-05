package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class GenesisConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = registerKey("silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PEWRIESE_ORE_KEY = registerKey("pewriese_ore");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        TagMatchTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldGenesisOres = List.of(
                OreConfiguration.target(stoneReplaceables, GenesisBlocks.SILVER_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GenesisBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState())
        );

        register(context, PEWRIESE_ORE_KEY, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stoneReplaceables, GenesisBlocks.PEWRIESE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GenesisBlocks.PEWRIESE_ORE.get().defaultBlockState())
        ), 3));

        register(context, SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldGenesisOres, 9));
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
