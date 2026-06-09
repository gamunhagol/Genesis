package com.gamunhagol.genesismod.world.dimension;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class GenesisDimensionTypes {
    public static void bootstrap(BootstapContext<DimensionType> context) {
        context.register(GenesisDimensions.GENESIS_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                0,
                320,
                320,
                BlockTags.INFINIBURN_OVERWORLD,
                new ResourceLocation("minecraft:the_end"),
                0.0f,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }
}
