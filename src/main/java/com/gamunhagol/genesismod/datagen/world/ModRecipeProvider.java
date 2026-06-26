package com.gamunhagol.genesismod.datagen.world;

import com.gamunhagol.genesismod.data.recipe.GenesisRecipeSerializers;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> HARDENED_RED_SMELTING = List.of(GenesisItems.HARDENED_RED_MASS.get());
    private static final List<ItemLike> PEWRIESE_SMELTING = List.of(GenesisItems.PEWRIESE_ORE_PIECE.get());
    private static final List<ItemLike> ANCIENT_ELVENIA_SMELTING = List.of(GenesisItems.SMALL_BELL_OF_OBLIVION.get()
    );

    private static final Map<Item, Item> HARDENED_RED_GLASS_SMITHING_MAP = Map.ofEntries(
            Map.entry(GenesisItems.HARDENED_GLASS_SWORD.get(), GenesisItems.HARDENED_RED_GLASS_SWORD.get()),
            Map.entry(GenesisItems.HARDENED_GLASS_GREATSWORD.get(), GenesisItems.HARDENED_RED_GLASS_GREATSWORD.get()),
            Map.entry(GenesisItems.HARDENED_GLASS_SPEAR.get(), GenesisItems.HARDENED_RED_GLASS_SPEAR.get()),
            Map.entry(GenesisItems.HARDENED_GLASS_TACHI.get(), GenesisItems.HARDENED_RED_GLASS_TACHI.get()),
            Map.entry(GenesisItems.HARDENED_GLASS_LONGSWORD.get(), GenesisItems.HARDENED_RED_GLASS_LONGSWORD.get()),
            Map.entry(GenesisItems.HARDENED_GLASS_DAGGER.get(), GenesisItems.HARDENED_RED_GLASS_DAGGER.get())
    );
    private static final Map<Item, Item> ELVENIA_SMITHING_MAP = Map.ofEntries(
            Map.entry(Items.GOLDEN_SWORD, GenesisItems.ELVENIA_SWORD.get()),
            Map.entry(Items.GOLDEN_SHOVEL, GenesisItems.ELVENIA_SHOVEL.get()),
            Map.entry(Items.GOLDEN_PICKAXE, GenesisItems.ELVENIA_PICKAXE.get()),
            Map.entry(Items.GOLDEN_AXE, GenesisItems.ELVENIA_AXE.get()),
            Map.entry(Items.GOLDEN_HOE, GenesisItems.ELVENIA_HOE.get()),
            Map.entry(EpicFightItems.GOLDEN_GREATSWORD.get(), GenesisItems.ELVENIA_GREATSWORD.get()),
            Map.entry(EpicFightItems.GOLDEN_SPEAR.get(), GenesisItems.ELVENIA_SPEAR.get()),
            Map.entry(EpicFightItems.GOLDEN_TACHI.get(), GenesisItems.ELVENIA_TACHI.get()),
            Map.entry(EpicFightItems.GOLDEN_LONGSWORD.get(), GenesisItems.ELVENIA_LONGSWORD.get()),
            Map.entry(EpicFightItems.GOLDEN_DAGGER.get(), GenesisItems.ELVENIA_DAGGER.get()),
            Map.entry(Items.GOLDEN_HELMET, GenesisItems.ELVENIA_HELMET.get()),
            Map.entry(Items.GOLDEN_CHESTPLATE, GenesisItems.ELVENIA_CHESTPLATE.get()),
            Map.entry(Items.GOLDEN_LEGGINGS, GenesisItems.ELVENIA_LEGGINGS.get()),
            Map.entry(Items.GOLDEN_BOOTS, GenesisItems.ELVENIA_BOOTS.get()),
            Map.entry(GenesisItems.GREAT_BOW.get(), GenesisItems.ELVENIA_GREAT_BOW.get())
    );
    private static final Map<Item, Item> ANCIENT_ELVENIA_SMITHING_MAP = Map.ofEntries(
            Map.entry(GenesisItems.ELVENIA_SWORD.get(), GenesisItems.ANCIENT_ELVENIA_SWORD.get()),
            Map.entry(GenesisItems.ELVENIA_SHOVEL.get(), GenesisItems.ANCIENT_ELVENIA_SHOVEL.get()),
            Map.entry(GenesisItems.ELVENIA_PICKAXE.get(), GenesisItems.ANCIENT_ELVENIA_PICKAXE.get()),
            Map.entry(GenesisItems.ELVENIA_AXE.get(), GenesisItems.ANCIENT_ELVENIA_AXE.get()),
            Map.entry(GenesisItems.ELVENIA_HOE.get(), GenesisItems.ANCIENT_ELVENIA_HOE.get()),
            Map.entry(GenesisItems.ELVENIA_GREATSWORD.get(), GenesisItems.ANCIENT_ELVENIA_GREATSWORD.get()),
            Map.entry(GenesisItems.ELVENIA_SPEAR.get(), GenesisItems.ANCIENT_ELVENIA_SPEAR.get()),
            Map.entry(GenesisItems.ELVENIA_TACHI.get(), GenesisItems.ANCIENT_ELVENIA_TACHI.get()),
            Map.entry(GenesisItems.ELVENIA_LONGSWORD.get(), GenesisItems.ANCIENT_ELVENIA_LONGSWORD.get()),
            Map.entry(GenesisItems.ELVENIA_DAGGER.get(), GenesisItems.ANCIENT_ELVENIA_DAGGER.get()),
            Map.entry(GenesisItems.ELVENIA_HELMET.get(), GenesisItems.ANCIENT_ELVENIA_HELMET.get()),
            Map.entry(GenesisItems.ELVENIA_CHESTPLATE.get(), GenesisItems.ANCIENT_ELVENIA_CHESTPLATE.get()),
            Map.entry(GenesisItems.ELVENIA_LEGGINGS.get(), GenesisItems.ANCIENT_ELVENIA_LEGGINGS.get()),
            Map.entry(GenesisItems.ELVENIA_BOOTS.get(), GenesisItems.ANCIENT_ELVENIA_BOOTS.get()),
            Map.entry(GenesisItems.GREAT_BOW.get(), GenesisItems.ANCIENT_ELVENIA_GREAT_BOW.get())
    );

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
            Map.entry(EpicFightItems.IRON_TACHI.get(), GenesisItems.PEWRIESE_TACHI.get()),
            Map.entry(EpicFightItems.GLOVE.get(), GenesisItems.PEWRIESE_GAUNTLET.get()),
            Map.entry(GenesisItems.GREAT_BOW.get(), GenesisItems.PEWRIESE_GREAT_BOW.get())
    );
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
    private static final Map<Item, Item> PEWRIESE_TOOL_UPGRADE_MAP = Map.ofEntries(
            Map.entry(GenesisItems.PEWRIESE_SWORD.get(), GenesisItems.HOLY_KNIGHT_SWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_DAGGER.get(), GenesisItems.HOLY_KNIGHT_DAGGER.get()),
            Map.entry(GenesisItems.PEWRIESE_GREATSWORD.get(), GenesisItems.HOLY_KNIGHT_GREATSWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_LONGSWORD.get(), GenesisItems.HOLY_KNIGHT_LONGSWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_SPEAR.get(), GenesisItems.HOLY_KNIGHT_SPEAR.get()),
            Map.entry(GenesisItems.PEWRIESE_TACHI.get(), GenesisItems.HOLY_KNIGHT_TACHI.get())
    );
    private static final Map<Item, Item> PEWRIESE_PURTRUCTION_UPGRADE_MAP = Map.ofEntries(
            Map.entry(GenesisItems.PEWRIESE_SWORD.get(), GenesisItems.PURTRUCTION_SWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_SHOVEL.get(), GenesisItems.PURTRUCTION_SHOVEL.get()),
            Map.entry(GenesisItems.PEWRIESE_PICKAXE.get(), GenesisItems.PURTRUCTION_PICKAXE.get()),
            Map.entry(GenesisItems.PEWRIESE_AXE.get(), GenesisItems.PURTRUCTION_AXE.get()),
            Map.entry(GenesisItems.PEWRIESE_HOE.get(), GenesisItems.PURTRUCTION_HOE.get()),
            Map.entry(GenesisItems.PEWRIESE_DAGGER.get(), GenesisItems.PURTRUCTION_DAGGER.get()),
            Map.entry(GenesisItems.PEWRIESE_GREATSWORD.get(), GenesisItems.PURTRUCTION_GREATSWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_LONGSWORD.get(), GenesisItems.PURTRUCTION_LONGSWORD.get()),
            Map.entry(GenesisItems.PEWRIESE_SPEAR.get(), GenesisItems.PURTRUCTION_SPEAR.get()),
            Map.entry(GenesisItems.PEWRIESE_TACHI.get(), GenesisItems.PURTRUCTION_TACHI.get())
    );

    private static final Map<Item, Item> AMETHYST_SMITHING_MAP = Map.ofEntries(
            Map.entry(Items.DIAMOND_HELMET, GenesisItems.AMETHYST_HELMET.get()),
            Map.entry(Items.DIAMOND_CHESTPLATE, GenesisItems.AMETHYST_CHESTPLATE.get()),
            Map.entry(Items.DIAMOND_LEGGINGS, GenesisItems.AMETHYST_LEGGINGS.get()),
            Map.entry(Items.DIAMOND_BOOTS, GenesisItems.AMETHYST_BOOTS.get())
    );
    private static final Map<Item, Item> AMETHYST_HEART_SMITHING_MAP = Map.ofEntries(
            Map.entry(GenesisItems.AMETHYST_HEART_PIECE.get(), GenesisItems.INTACT_AMETHYST_HEART.get()),
            Map.entry(EpicFightItems.DIAMOND_LONGSWORD.get(), GenesisItems.CRYSTAL_GROWN_LONGSWORD.get())
    );






    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.BOOK_OF_CREATION.get())
                .requires(Items.BOOK)
                .requires(Items.EXPERIENCE_BOTTLE)
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .unlockedBy(getHasName(Items.EXPERIENCE_BOTTLE), has(Items.EXPERIENCE_BOTTLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.DREAM_POWDER.get())
                .requires(GenesisItems.SCATTERED_MEMORIES.get(), 9)
                .unlockedBy(getHasName(GenesisItems.SCATTERED_MEMORIES.get()), has(GenesisItems.SCATTERED_MEMORIES.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.MANA_IMBUED_AMETHYST_SHARD.get())
                .requires(Items.AMETHYST_SHARD)
                .requires(Items.LAPIS_LAZULI)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(pWriter);

        oreSmelting(pWriter, HARDENED_RED_SMELTING, RecipeCategory.MISC, GenesisItems.HARDENED_RED_GLASS.get(), 0.2f, 250, "hardened_red_glass");

        oreSmelting(pWriter, ANCIENT_ELVENIA_SMELTING, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get(), 0.8f, 300, "ancient_elvenia");
        oreBlasting(pWriter, ANCIENT_ELVENIA_SMELTING, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get(), 0.8f, 150, "ancient_elvenia");

        oreSmelting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 20000, "pewriese");
        oreBlasting(pWriter, PEWRIESE_SMELTING, RecipeCategory.MISC, GenesisItems.PEWRIESE_PIECE.get(), 2.4f, 10000, "pewriese");


        //cooked
        List<ItemLike> MYSTERY_MEAT_COOKING = List.of(GenesisItems.SCORPION_MEAT.get());

        foodSmelting(pWriter, MYSTERY_MEAT_COOKING, RecipeCategory.FOOD, GenesisItems.COOKED_SCORPION_MEAT.get(), 0.35f, 200, "mystery_meat");
        foodSmoking(pWriter, MYSTERY_MEAT_COOKING, RecipeCategory.FOOD, GenesisItems.COOKED_SCORPION_MEAT.get(), 0.35f, 100, "mystery_meat");
        foodCampfire(pWriter, MYSTERY_MEAT_COOKING, RecipeCategory.FOOD, GenesisItems.COOKED_SCORPION_MEAT.get(), 0.35f, 600, "mystery_meat");


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.GIANT_STONE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', GenesisItems.GIANT_STONE_FRAGMENT.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE_FRAGMENT.get()), has(GenesisItems.GIANT_STONE_FRAGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.FUSION_STONE_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', GenesisItems.FUSION_STONE.get())
                .unlockedBy(getHasName(GenesisItems.FUSION_STONE.get()), has(GenesisItems.FUSION_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get())
                .pattern(" # ")
                .pattern("#a#")
                .pattern(" # ")
                .define('a', Items.LAPIS_LAZULI)
                .define('#', GenesisItems.PEWRIESE_PIECE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_PIECE.get()), has(GenesisItems.PEWRIESE_PIECE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.ACTIVATED_GIANT_STONE.get())
                .pattern(" # ")
                .pattern("#a#")
                .pattern(" # ")
                .define('a', GenesisItems.GIANT_STONE.get())
                .define('#', GenesisItems.PEWRIESE_PIECE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.FABRICATED_STAR.get())
                .pattern("abc")
                .pattern("d#e")
                .pattern("fgh")
                .define('#', Items.NETHER_STAR)
                .define('a', GenesisItems.CITRINE_SHARD.get())
                .define('b', GenesisItems.RED_CRYSTAL_SHARD.get())
                .define('c', GenesisItems.ICE_FLOWER_SHARD.get())
                .define('d', GenesisItems.BLUE_CRYSTAL_SHARD.get())
                .define('e', GenesisItems.WIND_STONE.get())
                .define('f', GenesisItems.LIGHTING_CRYSTAL_SHARD.get())
                .define('g', GenesisItems.GREEN_AMBER.get())
                .define('h', GenesisItems.MANA_IMBUED_AMETHYST_SHARD.get())
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.CLOTH.get())
                .pattern("  #")
                .pattern("###")
                .pattern("#  ")
                .define('#', Items.STRING)
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.ENCHANTED_CLOTH.get())
                .pattern("###")
                .pattern("#a#")
                .pattern("###")
                .define('#', GenesisItems.CLOTH.get())
                .define('a', Items.LAPIS_LAZULI)
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.BLESSED_CLOTH.get())
                .pattern("###")
                .pattern("#a#")
                .pattern("###")
                .define('#', GenesisItems.CLOTH.get())
                .define('a', GenesisItems.SACRED_STONE.get())
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.SACRED_STONE.get())
                .pattern("b#b")
                .pattern("#a#")
                .pattern("b#b")
                .define('#', ItemTags.STONE_TOOL_MATERIALS)
                .define('a', ItemTags.BOOKSHELF_BOOKS)
                .define('b', Items.GOLD_NUGGET)
                .unlockedBy(getHasName(Items.GOLD_NUGGET), has(Items.GOLD_NUGGET))
                .save(pWriter);


;

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ELVENIA_PIECE.get(), RecipeCategory.MISC, GenesisItems.ELVENIA_INGOT.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ELVENIA_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.ELVENIA_BLOCK.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_PIECE.get(), RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get());


        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL.get(), RecipeCategory.MISC, GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.PYULITELA.get(), RecipeCategory.MISC, GenesisBlocks.PYULITELA_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.WHITE_IRON_INGOT.get(), RecipeCategory.MISC, GenesisBlocks.WHITE_IRON_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.DREAM_POWDER.get(), RecipeCategory.MISC, GenesisItems.DREAM_DANGO.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.COPPER_COIN.get(), RecipeCategory.MISC, GenesisItems.COPPER_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.SILVER_COIN.get(), RecipeCategory.MISC, GenesisItems.SILVER_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.GOLD_COIN.get(), RecipeCategory.MISC, GenesisItems.GOLD_COIN_PILE.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.PLATINUM_COIN.get(), RecipeCategory.MISC, GenesisItems.PLATINUM_COIN_PILE.get());

        coinDeconstruct(pWriter, GenesisItems.SILVER_COIN.get(), GenesisItems.COPPER_COIN.get(), 9);
        coinDeconstruct(pWriter, GenesisItems.GOLD_COIN.get(), GenesisItems.SILVER_COIN.get(), 9);
        coinDeconstruct(pWriter, GenesisItems.PLATINUM_COIN.get(), GenesisItems.GOLD_COIN.get(), 9);

        //food
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GenesisItems.SCORPION_MEAT.get(), 2)
                .requires(GenesisItems.LAGER_DESERT_SCORPION_TAIL.get())
                .requires(Items.FLINT)
                .unlockedBy(getHasName(GenesisItems.LAGER_DESERT_SCORPION_TAIL.get()), has(GenesisItems.LAGER_DESERT_SCORPION_TAIL.get()))
                .save(pWriter, "scorpion_meat_from_tail");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GenesisItems.SCORPION_MEAT.get(), 3)
                .requires(GenesisItems.LAGER_DESERT_SCORPION_PINCERS.get())
                .requires(Items.FLINT)
                .unlockedBy(getHasName(GenesisItems.LAGER_DESERT_SCORPION_PINCERS.get()), has(GenesisItems.LAGER_DESERT_SCORPION_PINCERS.get()))
                .save(pWriter, "scorpion_meat_from_pincers");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GenesisItems.AMETHYST_APPLE_SLICES.get(), 4)
                .requires(GenesisItems.AMETHYST_APPLE.get())
                .requires(GenesisItems.AMETHYST_NEEDLE.get())
                .unlockedBy(getHasName(GenesisItems.AMETHYST_APPLE.get()), has(GenesisItems.AMETHYST_APPLE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get(), 1)
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
        compress2x2(pWriter, GenesisItems.ICE_FLOWER_SHARD.get(), GenesisBlocks.ICE_FLOWER_BLOCK.get());
        compress2x2(pWriter, GenesisItems.LIGHTING_CRYSTAL_SHARD.get(), GenesisBlocks.LIGHTING_CRYSTAL_BLOCK.get());
        compress2x2(pWriter, GenesisItems.WIND_STONE.get(), GenesisBlocks.WIND_STONE_BLOCK.get());

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, GenesisItems.GREEN_AMBER.get(), RecipeCategory.MISC, GenesisBlocks.GREEN_AMBER_BLOCK.get());

        //tool&weapon

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.GREAT_BOW.get())
                .pattern(" #l")
                .pattern("wal")
                .pattern(" #l")
                .define('a', Items.BOW)
                .define('#', Items.STICK)
                .define('w', ItemTags.LOGS)
                .define('l', Items.STRING)
                .unlockedBy(getHasName(Items.BOW), has(Items.BOW))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.ROOT_WOVEN_BOW.get())
                .pattern("r#l")
                .pattern("wal")
                .pattern("r#l")
                .define('a', Items.BOW)
                .define('#', Items.STICK)
                .define('w', ItemTags.LOGS)
                .define('l', Items.STRING)
                .define('r', Items.MANGROVE_ROOTS)
                .unlockedBy(getHasName(Items.BOW), has(Items.BOW))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.LARGE_ARROW.get())
                .pattern(" aw")
                .pattern("b#a")
                .pattern("#b ")
                .define('a', Items.IRON_NUGGET)
                .define('#', Items.STICK)
                .define('w', Items.IRON_INGOT)
                .define('b', Items.FEATHER)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.AMETHYST_WAND.get())
                .pattern("  a")
                .pattern(" # ")
                .pattern("#  ")
                .define('a', Items.AMETHYST_SHARD)
                .define('#', Items.STICK)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.GREEN_STAR_SEAL.get())
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.EMERALD)
                .define('b', Items.GOLD_NUGGET)
                .unlockedBy(getHasName(Items.EMERALD), has(Items.EMERALD))
                .save(pWriter);


        //misc
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_HELMET, GenesisItems.PADDED_CHAIN_HELMET.get(), " a ", "aba");
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_CHESTPLATE, GenesisItems.PADDED_CHAIN_CHESTPLATE.get(), "aaa", "aba");
        paddedArmorRecipe(pWriter, Items.CHAINMAIL_LEGGINGS, GenesisItems.PADDED_CHAIN_LEGGINGS.get(), "aba", "a a");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.WHITE_IRON_INGOT.get())
                .requires(Items.IRON_INGOT)
                .requires(GenesisItems.PEWRIESE_PIECE.get())
                .unlockedBy(getHasName(GenesisItems.PEWRIESE_PIECE.get()), has(GenesisItems.PEWRIESE_PIECE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STONE)
                .requires(Items.COBBLESTONE)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COAL_BLOCK)
                .requires(Items.COAL)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_BLOCK)
                .requires(Items.COPPER_INGOT)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_BLOCK)
                .requires(Items.IRON_INGOT)
            .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLD_BLOCK)
                .requires(Items.GOLD_INGOT)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND_BLOCK)
                .requires(Items.DIAMOND)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHERITE_BLOCK)
                .requires(Items.NETHERITE_INGOT)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.EMERALD_BLOCK)
                .requires(Items.EMERALD)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.REDSTONE_BLOCK)
                .requires(Items.REDSTONE)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LAPIS_BLOCK)
                .requires(Items.LAPIS_LAZULI)
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.CARBONIZED_INGOT.get())
                .requires(Items.GOLD_INGOT)
                .requires(Items.COPPER_INGOT)
                .requires(Items.IRON_INGOT)
                .requires(GenesisItems.FUSION_STONE.get())
                .unlockedBy(getHasName(GenesisItems.FUSION_STONE.get()), has(GenesisItems.FUSION_STONE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.ELVENIA_BLOCK.get())
                .requires(GenesisItems.ELVENIA_INGOT.get())
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.ANCIENT_ELVENIA_BLOCK.get())
                .requires(GenesisItems.ANCIENT_ELVENIA_INGOT.get())
                .requires(GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.PEWRIESE_CRYSTAL_BLOCK.get())
                .requires(GenesisItems.PEWRIESE_CRYSTAL.get())
                .requires(GenesisItems.ACTIVATED_GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.PYULITELA_BLOCK.get())
                .requires(GenesisItems.PYULITELA.get())
                .requires(GenesisItems.ACTIVATED_GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.GIANT_STONE.get()), has(GenesisItems.GIANT_STONE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.BASED_SCULPTURE.get())
                .requires(Items.NETHERITE_BLOCK)
                .requires(GenesisItems.FUSION_STONE_BLOCK.get())
                .requires(GenesisItems.PEWRIESE_CRYSTAL_BLOCK.get())
                .unlockedBy(getHasName(GenesisItems.FUSION_STONE_BLOCK.get()), has(GenesisItems.FUSION_STONE_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.STAR_OF_DOMINATION.get())
                .requires(GenesisItems.MEDALLION_OF_DOMINION.get())
                .requires(GenesisItems.EPONYMOUS_STAR.get())
                .unlockedBy(getHasName(GenesisItems.EPONYMOUS_STAR.get()), has(GenesisItems.EPONYMOUS_STAR.get()))
                .save(pWriter);




        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PADDED_CHAIN_BOOTS.get())
                .pattern("aba")
                .define('a',Items.LEATHER)
                .define('b',Items.CHAINMAIL_BOOTS)
                .unlockedBy("has_chainmail", has(Items.CHAINMAIL_BOOTS))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.SCORPION_HELMET.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.SCORPION_CARAPACE.get())
                .unlockedBy(getHasName(GenesisItems.SCORPION_CARAPACE.get()), has(GenesisItems.SCORPION_CARAPACE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.SCORPION_CHESTPLATE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.SCORPION_CARAPACE.get())
                .unlockedBy(getHasName(GenesisItems.SCORPION_CARAPACE.get()), has(GenesisItems.SCORPION_CARAPACE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.SCORPION_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.SCORPION_CARAPACE.get())
                .unlockedBy(getHasName(GenesisItems.SCORPION_CARAPACE.get()), has(GenesisItems.SCORPION_CARAPACE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.SCORPION_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.SCORPION_CARAPACE.get())
                .unlockedBy(getHasName(GenesisItems.SCORPION_CARAPACE.get()), has(GenesisItems.SCORPION_CARAPACE.get()))
                .save(pWriter);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_HELMET.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_CHESTPLATE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CLOTH_BANDANA.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CLOTH_ROBE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CLOTH_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CLOTH_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.CLOTH.get()), has(GenesisItems.CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PILGRIM_BANDANA.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.BLESSED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.BLESSED_CLOTH.get()), has(GenesisItems.BLESSED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PILGRIM_ROBE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.BLESSED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.BLESSED_CLOTH.get()), has(GenesisItems.BLESSED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PILGRIM_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.BLESSED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.BLESSED_CLOTH.get()), has(GenesisItems.BLESSED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PILGRIM_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.BLESSED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.BLESSED_CLOTH.get()), has(GenesisItems.BLESSED_CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.ASTROLOGER_BANDANA.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.ENCHANTED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.ENCHANTED_CLOTH.get()), has(GenesisItems.ENCHANTED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.ASTROLOGER_ROBE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.ENCHANTED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.ENCHANTED_CLOTH.get()), has(GenesisItems.ENCHANTED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.ASTROLOGER_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.ENCHANTED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.ENCHANTED_CLOTH.get()), has(GenesisItems.ENCHANTED_CLOTH.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.ASTROLOGER_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.ENCHANTED_CLOTH.get())
                .unlockedBy(getHasName(GenesisItems.ENCHANTED_CLOTH.get()), has(GenesisItems.ENCHANTED_CLOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.WHITE_IRON_HELMET.get())
                .pattern("aaa")
                .pattern("a a")
                .define('a',GenesisItems.WHITE_IRON_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.WHITE_IRON_INGOT.get()), has(GenesisItems.WHITE_IRON_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.WHITE_IRON_CHESTPLATE.get())
                .pattern("a a")
                .pattern("aaa")
                .pattern("aaa")
                .define('a',GenesisItems.WHITE_IRON_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.WHITE_IRON_INGOT.get()), has(GenesisItems.WHITE_IRON_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.WHITE_IRON_LEGGINGS.get())
                .pattern("aaa")
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.WHITE_IRON_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.WHITE_IRON_INGOT.get()), has(GenesisItems.WHITE_IRON_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.WHITE_IRON_BOOTS.get())
                .pattern("a a")
                .pattern("a a")
                .define('a',GenesisItems.WHITE_IRON_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.WHITE_IRON_INGOT.get()), has(GenesisItems.WHITE_IRON_INGOT.get()))
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GenesisItems.FLASK_SHARD.get())
                .requires(GenesisItems.DIVINE_GRAIL.get())
                .unlockedBy(getHasName(GenesisItems.DIVINE_GRAIL.get()), has(GenesisItems.DIVINE_GRAIL.get()))
                .save(pWriter);

        SpecialRecipeBuilder.special(GenesisRecipeSerializers.DIVINE_GRAIL_RECIPE.get())
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, "divine_grail_crafting").toString());

        SpecialRecipeBuilder.special(GenesisRecipeSerializers.SPIRIT_COMPASS_COMBINE.get())
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, "spirit_compass_combine").toString());
        SpecialRecipeBuilder.special(GenesisRecipeSerializers.SPIRIT_COMPASS_REMOVE.get())
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, "spirit_compass_remove").toString());

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_SWORD.get())
                .pattern("a")
                .pattern("a")
                .pattern("#")
                .define('#', Items.STICK)
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_DAGGER.get())
                .pattern(" a")
                .pattern("# ")
                .define('#', Items.STICK)
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_GREATSWORD.get())
                .pattern(" aa")
                .pattern("aaa")
                .pattern("#a ")
                .define('#', ItemTags.PLANKS)
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_LONGSWORD.get())
                .pattern("  a")
                .pattern(" a ")
                .pattern("#  ")
                .define('#', GenesisItems.HARDENED_GLASS_SWORD.get())
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_SPEAR.get())
                .pattern("  #")
                .pattern(" a ")
                .pattern("a  ")
                .define('#', GenesisItems.HARDENED_GLASS_SWORD.get())
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.HARDENED_GLASS_TACHI.get())
                .pattern(" a")
                .pattern(" a")
                .pattern("# ")
                .define('#', GenesisItems.HARDENED_GLASS_SWORD.get())
                .define('a', GenesisItems.HARDENED_GLASS_PIECES.get())
                .unlockedBy(getHasName(GenesisItems.HARDENED_GLASS_PIECES.get()), has(GenesisItems.HARDENED_GLASS_PIECES.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_SWORD.get())
                .pattern("a")
                .pattern("a")
                .pattern("#")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.CARBONIZED_SHOVEL.get())
                .pattern("a")
                .pattern("#")
                .pattern("#")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.CARBONIZED_PICKAXE.get())
                .pattern("aaa")
                .pattern(" # ")
                .pattern(" # ")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.CARBONIZED_AXE.get())
                .pattern("aa")
                .pattern("a#")
                .pattern(" #")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.CARBONIZED_HOE.get())
                .pattern("aa")
                .pattern(" #")
                .pattern(" #")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_DAGGER.get())
                .pattern(" a")
                .pattern("# ")
                .define('#', Items.STICK)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_GREATSWORD.get())
                .pattern(" aa")
                .pattern("aaa")
                .pattern("#a ")
                .define('#', ItemTags.PLANKS)
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_LONGSWORD.get())
                .pattern("  a")
                .pattern(" a ")
                .pattern("#  ")
                .define('#', GenesisItems.CARBONIZED_SWORD.get())
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_SPEAR.get())
                .pattern("  #")
                .pattern(" a ")
                .pattern("a  ")
                .define('#', GenesisItems.CARBONIZED_SWORD.get())
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.CARBONIZED_TACHI.get())
                .pattern(" a")
                .pattern(" a")
                .pattern("# ")
                .define('#', GenesisItems.CARBONIZED_SWORD.get())
                .define('a', GenesisItems.CARBONIZED_INGOT.get())
                .unlockedBy(getHasName(GenesisItems.CARBONIZED_INGOT.get()), has(GenesisItems.CARBONIZED_INGOT.get()))
                .save(pWriter);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_SWORD.get())
                .pattern("a")
                .pattern("a")
                .pattern("#")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.PYULITELA_SHOVEL.get())
                .pattern("a")
                .pattern("#")
                .pattern("#")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.PYULITELA_PICKAXE.get())
                .pattern("aaa")
                .pattern(" # ")
                .pattern(" # ")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.PYULITELA_AXE.get())
                .pattern("aa")
                .pattern("a#")
                .pattern(" #")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GenesisItems.PYULITELA_HOE.get())
                .pattern("aa")
                .pattern(" #")
                .pattern(" #")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_DAGGER.get())
                .pattern(" a")
                .pattern("# ")
                .define('#', Items.STICK)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_GREATSWORD.get())
                .pattern(" aa")
                .pattern("aaa")
                .pattern("#a ")
                .define('#', ItemTags.PLANKS)
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_LONGSWORD.get())
                .pattern("  a")
                .pattern(" a ")
                .pattern("#  ")
                .define('#', GenesisItems.PYULITELA_SWORD.get())
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_SPEAR.get())
                .pattern("  #")
                .pattern(" a ")
                .pattern("a  ")
                .define('#', GenesisItems.PYULITELA_SWORD.get())
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.PYULITELA_TACHI.get())
                .pattern(" a")
                .pattern(" a")
                .pattern("# ")
                .define('#', GenesisItems.PYULITELA_SWORD.get())
                .define('a', GenesisItems.PYULITELA.get())
                .unlockedBy(getHasName(GenesisItems.PYULITELA.get()), has(GenesisItems.PYULITELA.get()))
                .save(pWriter);



        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.GIANT_STONE_GREATSWORD.get())
                .pattern(" ab")
                .pattern("aba")
                .pattern("#a ")
                .define('#', Items.STICK)
                .define('a', ItemTags.STONE_TOOL_MATERIALS)
                .define('b', GenesisItems.GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get()), has(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GenesisItems.ACTIVATED_GIANT_STONE_GREATSWORD.get())
                .pattern(" ab")
                .pattern("aba")
                .pattern("#a ")
                .define('#', Items.BLAZE_ROD)
                .define('a', ItemTags.STONE_TOOL_MATERIALS)
                .define('b', GenesisItems.ACTIVATED_GIANT_STONE.get())
                .unlockedBy(getHasName(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get()), has(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get()))
                .save(pWriter);



        //smithing template

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .pattern("#m#")
                .pattern("#a#")
                .pattern("###")
                .define('a',Items.MOSS_BLOCK)
                .define('#',Items.GOLD_INGOT)
                .define('m', GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get()), has(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get()))
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


        //smithing table upgrade

        for (var entry : HARDENED_RED_GLASS_SMITHING_MAP.entrySet()) {
            smithingUpgrade(pWriter,
                    GenesisItems.HARDENED_RED_GLASS.get(),
                    entry.getKey(),
                    GenesisItems.HARDENED_RED_GLASS_PIECES.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()) + "_smithing");
        }

        for (var entry : ELVENIA_SMITHING_MAP.entrySet()) {
            smithingUpgrade(pWriter,
                    GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(),
                    entry.getKey(),
                    GenesisItems.ELVENIA_INGOT.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()) + "_smithing");
        }

        for (var entry : ANCIENT_ELVENIA_SMITHING_MAP.entrySet()) {
            smithingUpgrade(pWriter,
                    GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(),
                    entry.getKey(),
                    GenesisItems.ANCIENT_ELVENIA_INGOT.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()) + "_smithing");
        }


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
                    GenesisItems.PYULITELA.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()));
        }

        for (var entry : PEWRIESE_PURTRUCTION_UPGRADE_MAP.entrySet()) {
            pewrieseAdvancedSmithing(pWriter,
                    entry.getKey(),
                    GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()));
        }


        for (var entry : AMETHYST_SMITHING_MAP.entrySet()) {
            smithingUpgrade(pWriter,
                    GenesisItems.AMETHYST_MAGIC_CORE.get(),
                    entry.getKey(),
                    GenesisItems.AMETHYST_SHIELD_SHARD.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()) + "_smithing");
        }

        for (var entry : AMETHYST_HEART_SMITHING_MAP.entrySet()) {
            smithingUpgrade(pWriter,
                    GenesisItems.AMETHYST_MAGIC_CORE.get(),
                    entry.getKey(),
                    GenesisItems.AMETHYST_HEART_PIECE.get(),
                    entry.getValue(),
                    getItemName(entry.getValue()) + "_smithing");
        }

    }

    // --- Helper Methods ---

    protected static void coinDeconstruct(Consumer<FinishedRecipe> pWriter, ItemLike pHigh, ItemLike pLow, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pLow, count)
                .requires(pHigh)
                .unlockedBy(getHasName(pHigh), has(pHigh))
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, getItemName(pLow) + "_from_" + getItemName(pHigh)));
    }

    protected static void compress2x2(Consumer<FinishedRecipe> consumer, ItemLike small, ItemLike big) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, big)
                .pattern("aa")
                .pattern("aa")
                .define('a', small)
                .unlockedBy(getHasName(small), has(small))
                .save(consumer);
    }

    protected static void paddedArmorRecipe(Consumer<FinishedRecipe> consumer, ItemLike chainmail, ItemLike result, String line1, String line2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                .pattern(line1)
                .pattern(line2)
                .define('a', Items.LEATHER)
                .define('b', chainmail)
                .unlockedBy("has_chainmail", has(chainmail))
                .save(consumer);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> pWriter, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked) {
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9)
                .requires(pPacked)
                .unlockedBy(getHasName(pPacked), has(pPacked))
                .save(pWriter, new ResourceLocation(GenesisMod.MODID, getItemName(pUnpacked) + "_from_" + getItemName(pPacked)));

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
    protected static void foodSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cookFood(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected static void foodSmoking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cookFood(pFinishedRecipeConsumer, RecipeSerializer.SMOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void foodCampfire(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cookFood(pFinishedRecipeConsumer, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_campfire");
    }

    protected static void cookFood(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
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

    protected static void smithingUpgrade(Consumer<FinishedRecipe> consumer,
                                          ItemLike template,
                                          ItemLike base,
                                          ItemLike addition,
                                          Item result,
                                          String recipeName) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        RecipeCategory.COMBAT,
                        result)
                .unlocks("has_" + getItemName(addition), has(addition))
                .save(consumer, new ResourceLocation(GenesisMod.MODID, recipeName));
    }


}
