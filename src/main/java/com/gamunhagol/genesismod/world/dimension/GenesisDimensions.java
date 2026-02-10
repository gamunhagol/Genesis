package com.gamunhagol.genesismod.world.dimension;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;

public class GenesisDimensions {
    public static final ResourceKey<Level> APERTURE_DIM_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(GenesisMod.MODID, "aperture_dim"));
    public static final ResourceKey<LevelStem> APERTURE_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(GenesisMod.MODID, "aperture_dim"));

    public static final ResourceKey<DimensionType> GENESIS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(GenesisMod.MODID, "aperture_dim_type"));


    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypeGetter = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGetter = context.lookup(Registries.NOISE_SETTINGS);

        // 1번 차원 등록 (평지)
        registerFlatDimension(context, APERTURE_LEVEL_STEM, dimTypeGetter,
                biomeGetter.getOrThrow(GenesisBiomes.APERTURE_BIOME), GenesisBlocks.PRECIPITATE.get());

        // 2번 차원 등록 (나중에 추가할 때 이런 식)
        // registerFlatDimension(context, ABYSS_LEVEL_STEM, dimTypeGetter,
        //         biomeGetter.getOrThrow(GenesisBiomes.ABYSS_BIOME), Blocks.OBSIDIAN);
    }

    // 반복되는 등록 로직을 메서드로 분리하면 관리가 편합니다
    private static void registerFlatDimension(BootstapContext<LevelStem> context,
                                              ResourceKey<LevelStem> stemKey,
                                              HolderGetter<DimensionType> dimTypeGetter,
                                              net.minecraft.core.Holder<Biome> biome,
                                              net.minecraft.world.level.block.Block groundBlock) {

        FlatLevelGeneratorSettings flatSettings = new FlatLevelGeneratorSettings(
                Optional.empty(), biome, List.of());

        flatSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        flatSettings.getLayersInfo().add(new FlatLayerInfo(63, groundBlock.defaultBlockState().getBlock()));
        flatSettings.updateLayers();

        context.register(stemKey, new LevelStem(
                dimTypeGetter.getOrThrow(GenesisDimensions.GENESIS_DIM_TYPE),
                new FlatLevelSource(flatSettings)));
    }
}