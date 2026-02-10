package com.gamunhagol.genesismod.datagen;


import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.dimension.GenesisBiomes;
import com.gamunhagol.genesismod.world.dimension.GenesisDimensionTypes;
import com.gamunhagol.genesismod.world.dimension.GenesisDimensions;
import com.gamunhagol.genesismod.world.gen.GenesisBiomeModifiers;
import com.gamunhagol.genesismod.world.gen.GenesisConfiguredFeatures;
import com.gamunhagol.genesismod.world.gen.GenesisPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, GenesisConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, GenesisPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, GenesisBiomeModifiers::bootstrap)

            .add(Registries.DIMENSION_TYPE, GenesisDimensionTypes::bootstrap)
            .add(Registries.BIOME, GenesisBiomes::bootstrap)
            .add(Registries.LEVEL_STEM, GenesisDimensions::bootstrapStem); //

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(GenesisMod.MODID));
    }

}
