package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> PEWRIESE_SMELTING = List.of(GenesisBlocks.PEWRIESE_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        oreSmelting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_ORE_PIECE.get(), 20.8f, 20000, "pewriese");
        oreBlasting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_ORE_PIECE.get(), 20.8f, 10000, "pewriese");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get())
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.PEWRIESE_CRYSTAL.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_CRYSTAL.get()), has(GenesisItems.PEWRIESE_CRYSTAL.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get(), 9)
                .requires(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get())
                .unlockedBy(getHasName(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get()), has(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GenesisBlocks.BLUE_CRYSTAL_BLOCK.get())
                .pattern("aa")
                .pattern("aa")
                .define('a',GenesisItems.BLUE_CRYSTAL_SHARD.get())
                .unlockedBy(getHasName(GenesisItems.BLUE_CRYSTAL_SHARD.get()), has(GenesisItems.BLUE_CRYSTAL_SHARD.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GenesisBlocks.CITRINE_BLOCK.get())
                .pattern("aa")
                .pattern("aa")
                .define('a',GenesisItems.CITRINE_SHARD.get())
                .unlockedBy(getHasName(GenesisItems.CITRINE_SHARD.get()), has(GenesisItems.CITRINE_SHARD.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GenesisBlocks.RED_CRYSTAL_BLOCK.get())
                .pattern("aa")
                .pattern("aa")
                .define('a',GenesisItems.RED_CRYSTAL_SHARD.get())
                .unlockedBy(getHasName(GenesisItems.RED_CRYSTAL_SHARD.get()), has(GenesisItems.RED_CRYSTAL_SHARD.get()))
                .save(pWriter);

    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, GenesisMod.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
    protected static void pewrieseSmithing(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item pIngredientItem, RecipeCategory pCategory, Item pResultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()), Ingredient.of(pIngredientItem),
                Ingredient.of(GenesisItems.PEWRIESE_CRYSTAL.get()), pCategory, pResultItem).unlocks("has_pewriese_crystal", has(GenesisItems.PEWRIESE_CRYSTAL.get()))
                .save(pFinishedRecipeConsumer, getItemName(pResultItem));
    }
}
