package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public ModWorldGenProvider(PackOutput output, java.util.concurrent.CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries) {
        super(output, registries, java.util.Collections.emptySet());
    }
    protected void addEntries() {

        // -------------------------
        // 온천 (Hot Spring)
        // -------------------------
        var hotSpringConfig = new LakeFeature.Configuration(
                BlockStateProvider.simple(GenesisBlocks.HOT_SPRING_BLOCK.get().defaultBlockState()),
                BlockStateProvider.simple(Blocks.STONE.defaultBlockState())
        );

        ConfiguredFeature<?, ?> hotSpringFeature = new ConfiguredFeature<>(Feature.LAKE, hotSpringConfig);
        Holder<ConfiguredFeature<?, ?>> hotSpringHolder = Holder.direct(hotSpringFeature);

        PlacedFeature hotSpringPlaced = new PlacedFeature(
                hotSpringHolder,
                List.of(
                        RarityFilter.onAverageOnceEvery(40),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                )
        );

        Holder<PlacedFeature> hotSpringPlacedHolder = Holder.direct(hotSpringPlaced);

        // -------------------------
        // 모래늪 (Flowing Sand)
        // -------------------------
        var sandConfig = new LakeFeature.Configuration(
                BlockStateProvider.simple(GenesisBlocks.FLOWING_SAND_BLOCK.get().defaultBlockState()),
                BlockStateProvider.simple(Blocks.SAND.defaultBlockState())
        );

        ConfiguredFeature<?, ?> sandFeature = new ConfiguredFeature<>(Feature.LAKE, sandConfig);
        Holder<ConfiguredFeature<?, ?>> sandHolder = Holder.direct(sandFeature);

        PlacedFeature sandPlaced = new PlacedFeature(
                sandHolder,
                List.of(
                        RarityFilter.onAverageOnceEvery(55),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                )
        );

        Holder<PlacedFeature> sandPlacedHolder = Holder.direct(sandPlaced);

        // -------------------------
        // 바이옴 등록
        // -------------------------
        Biome taiga = ForgeRegistries.BIOMES.getValue(Biomes.TAIGA.location());
        Biome snowyTaiga = ForgeRegistries.BIOMES.getValue(Biomes.SNOWY_TAIGA.location());
        Biome meadow = ForgeRegistries.BIOMES.getValue(Biomes.MEADOW.location());
        Biome jagged = ForgeRegistries.BIOMES.getValue(Biomes.JAGGED_PEAKS.location());
        Biome frozen = ForgeRegistries.BIOMES.getValue(Biomes.FROZEN_PEAKS.location());
        Biome desert = ForgeRegistries.BIOMES.getValue(Biomes.DESERT.location());

        ForgeBiomeModifiers.AddFeaturesBiomeModifier hotSpringModifier =
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(List.of(
                                Holder.direct(Objects.requireNonNull(taiga)), Holder.direct(Objects.requireNonNull(snowyTaiga)),
                                Holder.direct(Objects.requireNonNull(meadow)), Holder.direct(Objects.requireNonNull(jagged)), Holder.direct(Objects.requireNonNull(frozen))
                        )),
                        HolderSet.direct(List.of(hotSpringPlacedHolder)),
                        GenerationStep.Decoration.LAKES
                );

        ForgeBiomeModifiers.AddFeaturesBiomeModifier sandModifier =
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(List.of(Holder.direct(Objects.requireNonNull(desert)))),
                        HolderSet.direct(List.of(sandPlacedHolder)),
                        GenerationStep.Decoration.LAKES
                );

        // -------------------------
        // 데이터팩 등록
        // -------------------------
        this.addBuiltin(
                new ResourceLocation(GenesisMod.MODID, "add_hot_spring_pools"), hotSpringModifier);
        this.addBuiltin(
                new ResourceLocation(GenesisMod.MODID, "add_quicksand_pools"), sandModifier);
    }

    private final java.util.Map<ResourceLocation, Object> BUILTIN_ENTRIES = new java.util.HashMap<>();

    private <T> void addBuiltin(ResourceLocation name, T value) {
        BUILTIN_ENTRIES.put(name, value);
    }

}
