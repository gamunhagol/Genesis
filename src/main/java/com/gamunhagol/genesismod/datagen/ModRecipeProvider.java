package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.data.repice.ModRecipeSerializers;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> PEWRIESE_SMELTING = List.of(GenesisItems.PEWRIESE_ORE_PIECE.get());


    private static final Map<Item, Item> PEWRIESE_SMITHING_MAP = Map.ofEntries(
            Map.entry(Items.IRON_SWORD, GenesisItems.PEWRIESE_SWORD.get()),
            Map.entry(Items.IRON_PICKAXE, GenesisItems.PEWRIESE_PICKAXE.get()),
            Map.entry(Items.IRON_AXE, GenesisItems.PEWRIESE_AXE.get()),
            Map.entry(Items.IRON_SHOVEL, GenesisItems.PEWRIESE_SHOVEL.get()),
            Map.entry(Items.IRON_HOE, GenesisItems.PEWRIESE_HOE.get()),
            Map.entry(Items.IRON_HELMET, GenesisItems.PEWRIESE_HELMET.get()),
            Map.entry(Items.IRON_CHESTPLATE, GenesisItems.PEWRIESE_CHESTPLATE.get()),
            Map.entry(Items.IRON_LEGGINGS, GenesisItems.PEWRIESE_LEGGINGS.get()),
            Map.entry(Items.IRON_BOOTS, GenesisItems.PEWRIESE_BOOTS.get()),
            Map.entry(EpicFightItems.IRON_DAGGER.get(), GenesisItems.PEWRIESE_DAGGER.get()),
            Map.entry(EpicFightItems.IRON_GREATSWORD.get(), GenesisItems.PEWRIESE_GREATSWORD.get()),
            Map.entry(EpicFightItems.IRON_LONGSWORD.get(), GenesisItems.PEWRIESE_LONGSWORD.get()),
            Map.entry(EpicFightItems.IRON_SPEAR.get(), GenesisItems.PEWRIESE_SPEAR.get()),
            Map.entry(EpicFightItems.IRON_TACHI.get(), GenesisItems.PEWRIESE_TACHI.get()));

    private static final Map<Item, Item> PEWRIESE_ARMOR_T1_MAP = Map.ofEntries(
            Map.entry(GenesisItems.PEWRIESE_HELMET.get(), GenesisItems.PEWRIESE_PLATE_HELMET.get()),
            Map.entry(GenesisItems.PEWRIESE_CHESTPLATE.get(), GenesisItems.PEWRIESE_PLATE_CHESTPLATE.get()),
            Map.entry(GenesisItems.PEWRIESE_LEGGINGS.get(), GenesisItems.PEWRIESE_PLATE_LEGGINGS.get()),
            Map.entry(GenesisItems.PEWRIESE_BOOTS.get(), GenesisItems.PEWRIESE_PLATE_BOOTS.get())
    );

    private static final Map<Item, Item> PEWRIESE_ARMOR_T2_MAP = Map.ofEntries(
            Map.entry(GenesisItems.PEWRIESE_HELMET.get(), GenesisItems.HOLY_KNIGHT_HELMET.get()),
            Map.entry(GenesisItems.PEWRIESE_CHESTPLATE.get(), GenesisItems.HOLY_KNIGHT_CHESTPLATE.get()),
            Map.entry(GenesisItems.PEWRIESE_LEGGINGS.get(), GenesisItems.HOLY_KNIGHT_LEGGINGS.get()),
            Map.entry(GenesisItems.PEWRIESE_BOOTS.get(), GenesisItems.HOLY_KNIGHT_BOOTS.get())
    );

    private static final Map<Item, Item> PEWRIESE_TOOL_UPGRADE_MAP = Map.ofEntries();





    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        oreSmelting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 20000, "pewriese");
        oreBlasting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 10000, "pewriese");


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.BOOK_OF_CREATION.get())
                .requires(Items.BOOK)
                .requires(Items.EMERALD)
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(pWriter);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.DREAM_DANGO.get())
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.DREAM_POWDER.get())
                .unlockedBy(getHasName(GenesisItems.DREAM_POWDER.get()), has(GenesisItems.DREAM_POWDER.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.DREAM_POWDER.get(), 9)
                .requires(GenesisItems.DREAM_DANGO.get())
                .unlockedBy(getHasName(GenesisItems.DREAM_DANGO.get()), has(GenesisItems.DREAM_DANGO.get()))
                .save(pWriter,new ResourceLocation(GenesisMod.MODID,"dream_powder_from_dango"));

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
                .save(pWriter,new ResourceLocation(GenesisMod.MODID, "pewriese_crystal_from_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.REMNANTS_OF_A_DREAM.get())
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.DREAM_DANGO.get())
                .unlockedBy(getHasName(GenesisItems.DREAM_POWDER.get()), has(GenesisItems.DREAM_POWDER.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.FRAGMENT_OF_MEMORY.get())
                .pattern("#a#")
                .pattern("#m#")
                .pattern("###")
                .define('a',Items.NETHER_STAR)
                .define('m',GenesisItems.REMNANTS_OF_A_DREAM.get())
                .define('#',Items.BLAZE_POWDER)
                .unlockedBy(getHasName(GenesisItems.DREAM_POWDER.get()), has(GenesisItems.DREAM_POWDER.get()))
                .save(pWriter);

        net.minecraft.data.recipes.SpecialRecipeBuilder
                .special(ModRecipeSerializers.SPIRIT_COMPASS_COMBINE.get())
                .save(pWriter, String.valueOf(new ResourceLocation(GenesisMod.MODID, "spirit_compass_combine")));

        net.minecraft.data.recipes.SpecialRecipeBuilder.special(ModRecipeSerializers.SPIRIT_COMPASS_REMOVE.get())
                .save(pWriter, String.valueOf(new ResourceLocation(GenesisMod.MODID, "spirit_compass_remove")));




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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get())
                .pattern(" # ")
                .pattern("#a#")
                .pattern(" # ")
                .define('a',Items.LAPIS_LAZULI)
                .define('#',GenesisItems.PEWRIESE_PIECE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_PIECE.get()), has(GenesisItems.PEWRIESE_PIECE.get()))
                .save(pWriter);



        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("#m#")
                .pattern("#a#")
                .pattern("###")
                .define('a',Items.LAPIS_BLOCK)
                .define('#',GenesisItems.PEWRIESE_PIECE.get())
                .define('m', GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()), has(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(pWriter);


        for (var entry : PEWRIESE_SMITHING_MAP.entrySet()) {
            pewrieseSmithing(pWriter,
                    entry.getKey(),
                    RecipeCategory.COMBAT,
                    entry.getValue());
        }
        for (var entry : PEWRIESE_ARMOR_T1_MAP.entrySet()) {
            pewrieseAdvancedSmithing(pWriter,
                    entry.getKey(),
                    GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get().asItem(),
                    entry.getValue(),
                    getItemName(entry.getValue()));
        }

        for (var entry : PEWRIESE_ARMOR_T2_MAP.entrySet()) {
            pewrieseAdvancedSmithing(pWriter,
                    entry.getKey(),
                    GenesisItems.PYULITELA.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()));
        }

        for (var entry : PEWRIESE_TOOL_UPGRADE_MAP.entrySet()) {
            pewrieseAdvancedSmithing(pWriter,
                    entry.getKey(),
                    GenesisItems.PYULITELA.get(),             // 같은 재료로 강화 가능
                    entry.getValue(),
                    getItemName(entry.getValue()));
        }

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
                .save(pFinishedRecipeConsumer, new ResourceLocation(GenesisMod.MODID, getItemName(pResultItem)));

    }
    protected static void pewrieseAdvancedSmithing(Consumer<FinishedRecipe> consumer,
                                                   Item baseItem, Item additionItem,
                                                   Item resultItem, String recipeName) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(baseItem),
                        Ingredient.of(additionItem),
                        RecipeCategory.COMBAT,
                        resultItem)
                .unlocks("has_" + getItemName(additionItem), has(additionItem))
                .save(consumer, new ResourceLocation(GenesisMod.MODID, recipeName));
    }


}
