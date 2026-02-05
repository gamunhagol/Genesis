package com.gamunhagol.genesismod.world.gen;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class GenesisBiomeModifiers {
    // 바이옴 모디파이어 리소스 키 정의
    public static final ResourceKey<BiomeModifier> ADD_SILVER_ORE = registerKey("add_silver_ore");
    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_ORE = registerKey("add_pewriese_ore");

    public static final ResourceKey<BiomeModifier> ADD_PEWRIESE_ORE_DESERT = registerKey("add_pewriese_ore_desert");

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
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(GenesisMod.MODID, name));
    }
}
