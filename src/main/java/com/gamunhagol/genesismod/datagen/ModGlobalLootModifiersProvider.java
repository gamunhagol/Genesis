package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.data.loot.AddItemModifier;
import com.gamunhagol.genesismod.data.loot.AddSusSandItemModifier;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, GenesisMod.MODID);
    }

    @Override
    protected void start() {
        add("amethyst_needle_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build()}, GenesisItems.AMETHYST_NEEDLE.get()));

    }
}
