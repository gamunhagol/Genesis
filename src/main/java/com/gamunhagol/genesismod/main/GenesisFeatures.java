package com.gamunhagol.genesismod.main;

import com.gamunhagol.genesismod.world.gen.feature.AmethystTreeFeature;
import com.gamunhagol.genesismod.world.gen.feature.GenesisCrystalFeature;
import com.gamunhagol.genesismod.world.gen.feature.configurations.GenesisCrystalConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, "genesis");

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> AMETHYST_TREE =
            FEATURES.register("amethyst_tree", () -> new AmethystTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<GenesisCrystalConfiguration>> GENESIS_CRYSTAL =
            FEATURES.register("genesis_crystal", () -> new GenesisCrystalFeature(GenesisCrystalConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
