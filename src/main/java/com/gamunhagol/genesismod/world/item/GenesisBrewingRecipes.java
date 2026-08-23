package com.gamunhagol.genesismod.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class GenesisBrewingRecipes {

    public static void registerBrewingRecipes() {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                Ingredient.of(Items.SNOWBALL),
                PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.FROSTBITE.get())
        ));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.FROSTBITE.get())),
                Ingredient.of(Items.REDSTONE),
                PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.LONG_FROSTBITE.get())
        ));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.FROSTBITE.get())),
                Ingredient.of(Items.BLUE_ICE),
                PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.STRONG_FROSTBITE.get())
        ));


        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING)),
                Ingredient.of(GenesisItems.SOUL_PUS.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), GenesisPotions.SOUL_SCREAM.get())
        ));
    }
}