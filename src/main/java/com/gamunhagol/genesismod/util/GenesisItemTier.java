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


    public static final Tier PEWRIESE = TierSortingRegistry.registerTier(
            new ForgeTier(5, 4431, 10f, 5F, 12, BlockTags.create(GenesisMod.prefix("need_pewriese_tool")),
                    () -> Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get())),
            GenesisMod.prefix("pewriese"), List.of(Tiers.NETHERITE), List.of());
}
