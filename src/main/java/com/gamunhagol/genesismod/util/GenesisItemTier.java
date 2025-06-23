package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class GenesisItemTier {

    public static final Tier SILVER_PLATED = TierSortingRegistry.registerTier(
            new ForgeTier(2,260,6.0f,2.0f,15, BlockTags.create(GenesisMod.prefix("need_silver_tool")),
                    () -> Ingredient.of(GenesisItems.SILVER_INGOT.get())),
            GenesisMod.prefix("silver_plated"), List.of(Tiers.IRON), List.of());

    public static final Tier ISIS = TierSortingRegistry.registerTier(
            new ForgeTier(4, 2031, 9f, 4F, 16, BlockTags.create(GenesisMod.prefix("need_isis_tool")),
                    () -> Ingredient.of(GenesisItems.ISIS_CRYSTAL.get())),
            GenesisMod.prefix("isis"), List.of(Tiers.NETHERITE), List.of());
}
