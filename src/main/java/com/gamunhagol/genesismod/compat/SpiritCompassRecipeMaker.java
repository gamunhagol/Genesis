package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.gamunhagol.genesismod.world.item.tool.SpiritCompassItem;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class SpiritCompassRecipeMaker {

    public static List<CraftingRecipe> getRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        recipes.addAll(createCombineRecipes());
        recipes.add(createRemoveRecipe());

        return recipes;
    }

    private static List<CraftingRecipe> createCombineRecipes() {
        List<CraftingRecipe> list = new ArrayList<>();

        addCombine(list, GenesisItems.BLUE_CRYSTAL_SHARD.get(), "water", "genesis:blue_crystal_cluster");
        addCombine(list, GenesisItems.RED_CRYSTAL_SHARD.get(), "fire", "genesis:red_crystal_cluster");
        addCombine(list, GenesisItems.CITRINE_SHARD.get(), "earth", "genesis:citrine_cluster");
        addCombine(list, GenesisItems.WIND_STONE.get(), "storm", "genesis:wind_stone_cluster");
        addCombine(list, GenesisItems.LIGHTING_CRYSTAL_SHARD.get(), "lightning", "genesis:lighting_crystal_cluster");
        addCombine(list, GenesisItems.GREEN_AMBER.get(), "plants", "genesis:green_amber_cluster");
        addCombine(list, GenesisItems.ICE_FLOWER_SHARD.get(), "ice", "genesis:ice_flower_cluster");

        return list;
    }

    private static void addCombine(List<CraftingRecipe> list, ItemLike stone, String type, String target) {
        ItemStack result = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, type);
        result.getOrCreateTag().putString(SpiritCompassItem.KEY_TARGET, target);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(GenesisItems.SPIRIT_COMPASS.get()),
                Ingredient.of(stone)
        );

        list.add(new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_spirit_combine_" + type),
                "spirit_compass_upgrade",
                CraftingBookCategory.MISC,
                result,
                inputs
        ));
    }

    private static ShapelessRecipe createRemoveRecipe() {
        ItemStack inputCompass = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        inputCompass.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, true);
        inputCompass.getOrCreateTag().putString(SpiritCompassItem.KEY_NEEDLE_TYPE, "any");

        ItemStack result = new ItemStack(GenesisItems.SPIRIT_COMPASS.get());
        result.getOrCreateTag().putBoolean(SpiritCompassItem.KEY_HAS_NEEDLE, false);

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_spirit_remove"),
                "spirit_compass_reset",
                CraftingBookCategory.MISC,
                result,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(inputCompass))
        );
    }
}