package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class GenesisItemTier {

    public static final Tier ELVENIA = TierSortingRegistry.registerTier(
            new ForgeTier(2, 500, 6f, 2F, 24, BlockTags.create(GenesisMod.prefix("need_elvenia_tool")),
                    () -> Ingredient.of(GenesisItems.ELVENIA_INGOT.get())),
            GenesisMod.prefix("elvenia"), List.of(Tiers.IRON), List.of());

    public static final Tier ANCIENT_ELVENIA = TierSortingRegistry.registerTier(
            new ForgeTier(3, 3121, 8f, 3F, 28, BlockTags.create(GenesisMod.prefix("need_ancient_elvenia_tool")),
                    () -> Ingredient.of(GenesisItems.ANCIENT_ELVENIA_INGOT.get())),
            GenesisMod.prefix("ancient_elvenia"), List.of(Tiers.DIAMOND), List.of());

    public static final Tier PEWRIESE = TierSortingRegistry.registerTier(
            new ForgeTier(5, 4431, 10f, 5F, 12, BlockTags.create(GenesisMod.prefix("need_pewriese_tool")),
                    () -> Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get())),
            GenesisMod.prefix("pewriese"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier HOLY_KNIGHT = TierSortingRegistry.registerTier(
            new ForgeTier(6, 5471, 11f, 5F, 17, BlockTags.create(GenesisMod.prefix("need_holy_knight_tool")),
                    () -> Ingredient.of(GenesisItems.PYULITELA.get())),
            GenesisMod.prefix("holy_knight"), List.of(GenesisItemTier.PEWRIESE), List.of());
}
