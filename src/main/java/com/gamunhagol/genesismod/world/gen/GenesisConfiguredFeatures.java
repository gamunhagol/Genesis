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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class GenesisConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = registerKey("silver_ore");
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

        register(context, PYULITELA_ORE_KEY, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stoneReplaceables, GenesisBlocks.PYULITELA_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GenesisBlocks.PYULITELA_ORE.get().defaultBlockState())
        ), 2));

        register(context, SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldGenesisOres, 9));
        register(context, AMETHYST_TREE_KEY, GenesisFeatures.AMETHYST_TREE.get(), NoneFeatureConfiguration.INSTANCE);

        // 1. 황수정 (Citrine): 땅속 매립형. 40% 공기 치환
        register(context, CITRINE_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "citrine_geode"),
                2, // variants (_1, _2)
                GenesisBlocks.CITRINE_CLUSTER.get().defaultBlockState(),
                0.4f, // 40% 확률로 클러스터 제거 (적당히 꽉 참)
                -2 // Y 오프셋 (땅에 약간 파묻힘)
        ));
        // 2. 적수정 (Red Crystal): 용암 호수. 70% 공기 치환 (가시 형상 강조)
        register(context, RED_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "red_crystal_geode"),
                2,
                GenesisBlocks.RED_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.6f, // 많이 비워내서 뾰족한 가시만 남김
                -1 // 용암 바닥에서 깊게 솟아오름
        ));
        // 3. 청수정 (Blue Crystal): 산호초. 30% 공기 치환 (덩어리감 유지)
        register(context, BLUE_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "blue_crystal_geode"),
                2,
                GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.3f, // 꽤 빽빽하게 유지
                -1 // 모래에 살짝 박힘
        ));
        // 4. 녹호박 (Green Amber): 정글. 50% 치환
        register(context, GREEN_AMBER_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "green_amber_geode"),
                2,
                GenesisBlocks.GREEN_AMBER_CLUSTER.get().defaultBlockState(),
                0.5f,
                0 // 지면(혹은 나무) 위에 딱 붙음
        ));
        // 5. 바람석 (Wind Stone): 절벽. 20% 치환 (단단한 바위 느낌)
        register(context, WIND_STONE_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "wind_stone_geode"),
                2,
                GenesisBlocks.WIND_STONE_CLUSTER.get().defaultBlockState(),
                0.4f, // 거의 꽉 찬 상태
                0 // 절벽 옆면에 붙음
        ));
        // 6. 뇌전수정 (Lightning): 사막. 60% 치환 (번개 맞은 흔적)
        register(context, LIGHTING_CRYSTAL_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "lighting_crystal_geode"),
                2,
                GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER.get().defaultBlockState(),
                0.6f,
                -1
        ));
        // 7. 얼음꽃 (Ice Flower): 빙하. 40% 치환 (꽃잎 모양)
        register(context, ICE_FLOWER_GEODE_KEY, GenesisFeatures.GENESIS_CRYSTAL.get(), new GenesisCrystalConfiguration(
                new ResourceLocation(GenesisMod.MODID, "ice_flower_geode"),
                2,
                GenesisBlocks.ICE_FLOWER_CLUSTER.get().defaultBlockState(),
                0.4f,
                0
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
