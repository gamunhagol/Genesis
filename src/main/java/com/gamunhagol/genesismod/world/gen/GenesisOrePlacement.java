package com.gamunhagol.genesismod.world.gen;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class GenesisOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
        return orePlacement(CountPlacement.of(count), heightModifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightModifier);
    }
}
