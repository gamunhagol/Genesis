package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.data.loot.AddSusSandItemModifier;
import com.gamunhagol.genesismod.data.loot.GenesisLootTables;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.mojang.serialization.Codec;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.HashMap;
import java.util.Map;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, GenesisMod.MODID);
    }

    @Override
    protected void start() {
        Map<ResourceLocation, AddSusSandItemModifier.Pair> entries = new HashMap<>();

        entries.put(new ResourceLocation("minecraft", "archaeology/desert_pyramid"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.025f));
        entries.put(new ResourceLocation("minecraft", "archaeology/trail_ruins_rare"),
                new AddSusSandItemModifier.Pair(GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.051f));
        entries.put(new ResourceLocation("minecraft", "archaeology/ocean_ruins_cold"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.008f));
        entries.put(new ResourceLocation("minecraft", "archaeology/ocean_ruins_warm"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.012f));

        add("add_sus_sand_item",
                new AddSusSandItemModifier(new LootItemCondition[0], entries));
    }
}
