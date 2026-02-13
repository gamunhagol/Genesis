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
    // 바이옴 모디파이어 리소스 키 정의
    public static final ResourceKey<BiomeModifier> ADD_SILVER_ORE = registerKey("add_silver_ore");
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

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // 오버월드 바이옴 태그를 가진 모든 곳에 광물 추가
        context.register(ADD_SILVER_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD), // 오버월드 바이옴 타겟팅
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.SILVER_ORE_PLACED_KEY)), // 배치 규칙 연결
                GenerationStep.Decoration.UNDERGROUND_ORES // 생성 단계 설정
        ));
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

        // 1. 황수정 (Citrine): 오버월드 전체 (자수정보다 희귀하게 설정됨)
        context.register(ADD_CITRINE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.CITRINE_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 2. 적수정 (Red Crystal): 오버월드 지하 용암층 대상
        context.register(ADD_RED_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.RED_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 3. 청수정 (Blue Crystal): 따뜻한 바다 (산호초 바이옴)
        context.register(ADD_BLUE_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.WARM_OCEAN)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.BLUE_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 4. 녹호박 (Green Amber): 정글 바이옴
        context.register(ADD_GREEN_AMBER_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_JUNGLE),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.GREEN_AMBER_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 5. 뇌전수정 (Lightning): 사막
        context.register(ADD_LIGHTING_CRYSTAL_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DESERT)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.LIGHTING_CRYSTAL_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 6. 바람석 (Wind Stone): 뾰족한 봉우리 등 고지대
        context.register(ADD_WIND_STONE_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.JAGGED_PEAKS), // 뾰족한 봉우리
                        biomes.getOrThrow(Biomes.FROZEN_PEAKS), // 얼어붙은 봉우리
                        biomes.getOrThrow(Biomes.STONY_PEAKS)   // 돌 봉우리
                ),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.WIND_STONE_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // 7. 얼음꽃 (Ice Flower): 빙하 (역고드름 지형)
        context.register(ADD_ICE_FLOWER_GEODE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.ICE_SPIKES)),
                HolderSet.direct(placedFeatures.getOrThrow(GenesisPlacedFeatures.ICE_FLOWER_GEODE_PLACED_KEY)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(GenesisMod.MODID, name));
    }
}
