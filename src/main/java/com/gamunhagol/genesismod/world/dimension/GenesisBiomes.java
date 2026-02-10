package com.gamunhagol.genesismod.world.dimension;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class GenesisBiomes {
    public static final ResourceKey<Biome> APERTURE_BIOME = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(GenesisMod.MODID, "dust_in_the_crevices"));

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(APERTURE_BIOME, voidBiome(context));
    }

    private static Biome voidBiome(BootstapContext<Biome> context) {
        // 1. 몹 스폰 설정: 아무것도 스폰되지 않음
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        // 2. 지형지물(나무, 꽃 등) 설정: 아무것도 생성되지 않음
        BiomeGenerationSettings.Builder generationBuilder = new BiomeGenerationSettings.Builder(
                context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER)
        );

        // 3. 바이옴 분위기 설정 (색상 및 효과)
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false) // 비 안 옴
                .downfall(0.0f)
                .temperature(0.5f)
                .generationSettings(generationBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x3F3F3F)       // 물 색깔 (매우 어두운 회색)
                        .waterFogColor(0x000000)    // 물 안개 (검정)
                        .fogColor(0x000000)         // 안개 (검정)
                        .skyColor(0x000000)         // 하늘 (검정)
                        .foliageColorOverride(0x555555) // 나뭇잎 (죽은 색)
                        .grassColorOverride(0x555555)   // 잔디 (죽은 색)
                        .backgroundMusic(null)      // 배경음악 없음 (필요하면 추가 가능)
                        .build())
                .build();
    }
}
