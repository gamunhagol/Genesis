package com.gamunhagol.genesismod.compat;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.tool.DivineGrailItem;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class DivineGrailRecipeMaker {
    public static List<CraftingRecipe> getRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        recipes.add(createRefillRecipe());
        recipes.add(createMaxUsesRecipe());
        recipes.add(createHealUpgradeRecipe());

        return recipes;
    }

    private static CraftingRecipe createRefillRecipe() {
        ItemStack emptyGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());
        ((DivineGrailItem) emptyGrail.getItem()).setUses(emptyGrail, 0); // 0회 남음 설정

        ItemStack fullGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());
        ((DivineGrailItem) fullGrail.getItem()).refill(fullGrail); // Max로 채움

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(emptyGrail),
                Ingredient.of(Items.TORCHFLOWER),
                Ingredient.of(Items.HONEY_BOTTLE)
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_refill"),
                "grail_refill",
                CraftingBookCategory.MISC,
                fullGrail,
                inputs
        );
    }

    private static CraftingRecipe createMaxUsesRecipe() {
        ItemStack inputGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());

        ItemStack outputGrail = inputGrail.copy();
        ((DivineGrailItem) outputGrail.getItem()).upgradeMaxUses(outputGrail);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(inputGrail),
                Ingredient.of(GenesisItems.FLASK_SHARD.get())
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_upgrade_max"),
                "grail_upgrade",
                CraftingBookCategory.MISC,
                outputGrail,
                inputs
        );
    }

    private static CraftingRecipe createHealUpgradeRecipe() {
        ItemStack inputGrail = new ItemStack(GenesisItems.DIVINE_GRAIL.get());

        ItemStack outputGrail = inputGrail.copy();
        ((DivineGrailItem) outputGrail.getItem()).upgradeHealAmount(outputGrail);

        NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(inputGrail),
                Ingredient.of(GenesisItems.BEAST_REMAINS.get())
        );

        return new ShapelessRecipe(
                new ResourceLocation(GenesisMod.MODID, "jei_grail_upgrade_heal"),
                "grail_upgrade",
                CraftingBookCategory.MISC,
                outputGrail,
                inputs
        );
    }
}
