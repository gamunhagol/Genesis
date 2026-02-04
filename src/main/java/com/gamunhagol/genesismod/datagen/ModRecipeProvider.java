package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.data.recipe.ModRecipeSerializers;
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

    private static final List<ItemLike> SILVER_SMELTING = List.of(GenesisItems.RAW_SILVER.get(),
            GenesisBlocks.SILVER_ORE.get(),
            GenesisBlocks.DEEPSLATE_SILVER_ORE.get());


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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.BOOK_OF_CREATION.get())
                .requires(Items.BOOK)
                .requires(Items.EMERALD)
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.DREAM_POWDER.get())
                .requires(GenesisItems.SCATTERED_MEMORIES.get(), 9)
                .unlockedBy(getHasName(GenesisItems.SCATTERED_MEMORIES.get()), has(GenesisItems.SCATTERED_MEMORIES.get()))
                .save(pWriter);


        oreSmelting(pWriter, SILVER_SMELTING, RecipeCategory.MISC, GenesisItems.SILVER_INGOT.get(), 1.0f, 200, "silver");
        oreBlasting(pWriter, SILVER_SMELTING, RecipeCategory.MISC, GenesisItems.SILVER_INGOT.get(), 1.0f, 100, "silver");
        oreSmelting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 20000, "pewriese");
        oreBlasting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 10000, "pewriese");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get())
                .pattern(" # ")
                .pattern("#a#")
                .pattern(" # ")
                .define('a', Items.LAPIS_LAZULI)
                .define('#', GenesisItems.PEWRIESE_PIECE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_PIECE.get()), has(GenesisItems.PEWRIESE_PIECE.get()))
                .save(pWriter);


        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.SILVER_PIECE.get(), RecipeCategory.MISC, GenesisItems.SILVER_INGOT.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.SILVER_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.SILVER_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.RAW_SILVER.get(), RecipeCategory.MISC, GenesisBlocks.RAW_SILVER_BLOCK.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ELVENIA_PIECE.get(), RecipeCategory.MISC, GenesisItems.ELVENIA_INGOT.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ELVENIA_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.ELVENIA_BLOCK.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_PIECE.get(), RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get());


        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get(), RecipeCategory.MISC, GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.DREAM_POWDER.get(), RecipeCategory.MISC, GenesisItems.DREAM_DANGO.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.COPPER_COIN.get(), RecipeCategory.MISC, GenesisItems.COPPER_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.SILVER_COIN.get(), RecipeCategory.MISC, GenesisItems.SILVER_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.GOLD_COIN.get(), RecipeCategory.MISC, GenesisItems.GOLD_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.PLATINUM_COIN.get(), RecipeCategory.MISC, GenesisItems.PLATINUM_COIN_PILE.get());

        coinDeconstruct(pWriter, GenesisItems.SILVER_COIN.get(), GenesisItems.COPPER_COIN.get(), 9);
        coinDeconstruct(pWriter, GenesisItems.GOLD_COIN.get(), GenesisItems.SILVER_COIN.get(), 9);
        coinDeconstruct(pWriter, GenesisItems.PLATINUM_COIN.get(), GenesisItems.GOLD_COIN.get(), 9);

        //food
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.AMETHYST_APPLE_SLICES.get(), 4)
                .requires(GenesisItems.AMETHYST_APPLE.get())
                .requires(GenesisItems.AMETHYST_NEEDLE.get())
                .unlockedBy(getHasName(GenesisItems.AMETHYST_APPLE.get()), has(GenesisItems.AMETHYST_APPLE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get(), 1)
                .requires(GenesisItems.AMETHYST_APPLE_SLICES.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.SUGAR)
                .requires(Items.EGG)
                .requires(Items.BLUE_ICE)
                .requires(Items.SLIME_BALL)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.BOWL)
                .unlockedBy(getHasName(GenesisItems.AMETHYST_APPLE_SLICES.get()), has(GenesisItems.AMETHYST_APPLE_SLICES.get()))
                .save(pWriter);


        //block&restoration

        compress2x2(pWriter, GenesisItems.BLUE_CRYSTAL_SHARD.get(), GenesisBlocks.BLUE_CRYSTAL_BLOCK.get());
        compress2x2(pWriter, GenesisItems.CITRINE_SHARD.get(), GenesisBlocks.CITRINE_BLOCK.get());
        compress2x2(pWriter, GenesisItems.RED_CRYSTAL_SHARD.get(), GenesisBlocks.RED_CRYSTAL_BLOCK.get());

        //misc
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_HELMET, GenesisItems.PADDED_CHAIN_HELMET.get(), " a ", "aba");
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_CHESTPLATE, GenesisItems.PADDED_CHAIN_CHESTPLATE.get(), "aaa", "aba");
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_LEGGINGS, GenesisItems.PADDED_CHAIN_LEGGINGS.get(), "aba", "a a");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PADDED_CHAIN_BOOTS.get())
                .pattern("aba")
                .define('a',Items.LEATHER)
                .define('b',Items.CHAINMAIL_BOOTS)
                .unlockedBy("has_chainmail", has(Items.CHAINMAIL_BOOTS))
                .save(pWriter);




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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.SILVER_COIN.get(), 8)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.COPPER_COIN_PILE.get())
                .unlockedBy(getHasName(GenesisItems.COPPER_COIN_PILE.get()), has(GenesisItems.COPPER_COIN_PILE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.GOLD_COIN.get(), 8)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.SILVER_COIN_PILE.get())
                .unlockedBy(getHasName(GenesisItems.SILVER_COIN_PILE.get()), has(GenesisItems.SILVER_COIN_PILE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.PLATINUM_COIN.get(), 8)
                .requires(GenesisItems.GOLD_COIN_PILE.get(), 9) // 해당 아이템 9개 필요
                .unlockedBy(getHasName(GenesisItems.GOLD_COIN_PILE.get()), has(GenesisItems.GOLD_COIN_PILE.get()))
                .save(pWriter);


        SpecialRecipeBuilder.special(ModRecipeSerializers.SPIRIT_COMPASS_COMBINE.get())
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, "spirit_compass_combine").toString());
        SpecialRecipeBuilder.special(ModRecipeSerializers.SPIRIT_COMPASS_REMOVE.get())
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, "spirit_compass_remove").toString());




        //smithing template

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("#m#")
                .pattern("#a#")
                .pattern("###")
                .define('a',Items.LAPIS_BLOCK)
                .define('#',GenesisItems.PEWRIESE_PIECE.get())
                .define('m', GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()), has(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(pWriter);


        //smithing table upgrade

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

    // --- Helper Methods ---

    // 동전 압축 레시피 헬퍼
    protected static void coinDeconstruct(Consumer<FinishedRecipe> pWriter, ItemLike pHigh, ItemLike pLow, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pLow, count)
                .requires(pHigh)
                .unlockedBy(getHasName(pHigh), has(pHigh))
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, getItemName(pLow) + "_from_" + getItemName(pHigh)));
    }

    // [New] 2x2 압축 레시피 헬퍼
    protected static void compress2x2(Consumer<FinishedRecipe> consumer, ItemLike small, ItemLike big) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, big)
                .pattern("aa")
                .pattern("aa")
                .define('a', small)
                .unlockedBy(getHasName(small), has(small))
                .save(consumer);
    }

    // [New] 패딩 갑옷 레시피 헬퍼
    protected static void paddedArmorRecipe(Consumer<FinishedRecipe> consumer, ItemLike chainmail, ItemLike result, String line1, String line2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern(line1)
                .pattern(line2)
                .define('a', Items.LEATHER)
                .define('b', chainmail)
                .unlockedBy("has_chainmail", has(chainmail))
                .save(consumer);
    }

    // [New] Vanilla 스타일 9개 압축/해제 자동화 헬퍼 (Item -> Block, Block -> Item)
    // 원래는 RecipeProvider에 nineBlockStorageRecipe가 있지만, 양방향을 한 번에 처리하려면 직접 만드는 게 편함.
    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> pWriter, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked) {
        // Packed -> Unpacked (1 -> 9)
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9)
                .requires(pPacked)
                .unlockedBy(getHasName(pPacked), has(pPacked))
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, getItemName(pUnpacked) + "_from_" + getItemName(pPacked)));

        // Unpacked -> Packed (9 -> 1)
        ShapedRecipeBuilder.shaped(pPackedCategory, pPacked)
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', pUnpacked)
                .unlockedBy(getHasName(pUnpacked), has(pUnpacked))
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, getItemName(pPacked) + "_from_" + getItemName(pUnpacked))); // 여기 이름이 중요
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smelting");
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
