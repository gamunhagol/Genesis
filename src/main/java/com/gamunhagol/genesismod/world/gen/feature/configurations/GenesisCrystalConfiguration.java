package com.gamunhagol.genesismod.world.gen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record GenesisCrystalConfiguration
        (
        ResourceLocation structurePath,
        int variants,
        BlockState targetBlock,
        float airChance,
        int yOffset
    ) implements FeatureConfiguration {

        public static final Codec<GenesisCrystalConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("structure_path").forGetter(GenesisCrystalConfiguration::structurePath),
                Codec.INT.fieldOf("variants").forGetter(GenesisCrystalConfiguration::variants),
                BlockState.CODEC.fieldOf("target_block").forGetter(GenesisCrystalConfiguration::targetBlock),
                Codec.FLOAT.fieldOf("air_chance").forGetter(GenesisCrystalConfiguration::airChance),
                Codec.INT.fieldOf("y_offset").forGetter(GenesisCrystalConfiguration::yOffset)
        ).apply(instance, GenesisCrystalConfiguration::new));
}

