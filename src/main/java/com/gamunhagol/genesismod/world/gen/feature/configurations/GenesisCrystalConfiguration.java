package com.gamunhagol.genesismod.world.gen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record GenesisCrystalConfiguration
        (
        ResourceLocation structurePath, // NBT 파일 기본 경로 (예: genesis:citrine_geode)
        int variants,                   // 변종 개수 (예: 2개면 _1, _2 중 랜덤)
        BlockState targetBlock,         // 공기로 바꿀 대상 블록 (예: 황수정 클러스터)
        float airChance,                // 대상 블록이 공기로 바뀔 확률 (0.0 ~ 1.0)
        int yOffset                     // Y축 높이 보정 (땅에 파묻히거나 띄울 때 사용)
    ) implements FeatureConfiguration {

        public static final Codec<GenesisCrystalConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("structure_path").forGetter(GenesisCrystalConfiguration::structurePath),
                Codec.INT.fieldOf("variants").forGetter(GenesisCrystalConfiguration::variants),
                BlockState.CODEC.fieldOf("target_block").forGetter(GenesisCrystalConfiguration::targetBlock),
                Codec.FLOAT.fieldOf("air_chance").forGetter(GenesisCrystalConfiguration::airChance),
                Codec.INT.fieldOf("y_offset").forGetter(GenesisCrystalConfiguration::yOffset)
        ).apply(instance, GenesisCrystalConfiguration::new));
}

