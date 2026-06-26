package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisItemTier;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.capability.spell.SpellBookItem;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;
import com.gamunhagol.genesismod.world.item.armor.*;
import com.gamunhagol.genesismod.world.item.custom.*;
import com.gamunhagol.genesismod.world.item.tool.DivineGrailItem;
import com.gamunhagol.genesismod.world.item.tool.GenericScrollItem;
import com.gamunhagol.genesismod.world.item.tool.LandEyeBlockItem;
import com.gamunhagol.genesismod.world.item.tool.SpiritCompassItem;
import com.gamunhagol.genesismod.world.item.weapon.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.*;



public class GenesisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GenesisMod.MODID);




    //block

    public static final RegistryObject<BlockItem> ELVENIA_BLOCK = ITEMS.register("elvenia_block",
            () -> new BlockItem(GenesisBlocks.ELVENIA_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ANCIENT_ELVENIA_BLOCK = ITEMS.register("ancient_elvenia_block",
            () -> new BlockItem(GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> PEWRIESE_ORE = ITEMS.register("pewriese_ore",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PEWRIESE_CRYSTAL_BLOCK = ITEMS.register("pewriese_crystal_block",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get(), new Item.Properties().fireResistant()));

    public static final RegistryObject<BlockItem> WHITE_IRON_BLOCK = ITEMS.register("white_iron_block",
            () -> new BlockItem(GenesisBlocks.WHITE_IRON_BLOCK.get(), new Item.Properties().fireResistant()));


    public static final RegistryObject<BlockItem> PYULITELA_ORE = ITEMS.register("pyulitela_ore",
            () -> new BlockItem(GenesisBlocks.PYULITELA_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PYULITELA_BLOCK = ITEMS.register("pyulitela_block",
            () -> new BlockItem(GenesisBlocks.PYULITELA_BLOCK.get(), new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<BlockItem> FUSION_STONE_BLOCK = ITEMS.register("fusion_stone_block",
            () -> new BlockItem(GenesisBlocks.FUSION_STONE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> GIANT_STONE = ITEMS.register("giant_stone",
            () -> new BlockItem(GenesisBlocks.GIANT_STONE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ACTIVATED_GIANT_STONE = ITEMS.register("activated_giant_stone",
            () -> new BlockItem(GenesisBlocks.ACTIVATED_GIANT_STONE.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<BlockItem> BLUE_CRYSTAL_BLOCK = ITEMS.register("blue_crystal_block",
            () -> new BlockItem(GenesisBlocks.BLUE_CRYSTAL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> BLUE_CRYSTAL_CLUSTER = ITEMS.register("blue_crystal_cluster",
            () -> new BlockItem(GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> CITRINE_BLOCK = ITEMS.register("citrine_block",
            () -> new BlockItem(GenesisBlocks.CITRINE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CITRINE_CLUSTER = ITEMS.register("citrine_cluster",
            () -> new BlockItem(GenesisBlocks.CITRINE_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> RED_CRYSTAL_BLOCK = ITEMS.register("red_crystal_block",
            () -> new BlockItem(GenesisBlocks.RED_CRYSTAL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RED_CRYSTAL_CLUSTER = ITEMS.register("red_crystal_cluster",
            () -> new BlockItem(GenesisBlocks.RED_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> GREEN_AMBER_BLOCK = ITEMS.register("green_amber_block",
            () -> new BlockItem(GenesisBlocks.GREEN_AMBER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> GREEN_AMBER_CLUSTER = ITEMS.register("green_amber_cluster",
            () -> new BlockItem(GenesisBlocks.GREEN_AMBER_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> ICE_FLOWER_BLOCK = ITEMS.register("ice_flower_block",
            () -> new BlockItem(GenesisBlocks.ICE_FLOWER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ICE_FLOWER_CLUSTER = ITEMS.register("ice_flower_cluster",
            () -> new BlockItem(GenesisBlocks.ICE_FLOWER_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> LIGHTING_CRYSTAL_BLOCK = ITEMS.register("lighting_crystal_block",
            () -> new BlockItem(GenesisBlocks.LIGHTING_CRYSTAL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LIGHTING_CRYSTAL_CLUSTER = ITEMS.register("lighting_crystal_cluster",
            () -> new BlockItem(GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> WIND_STONE_BLOCK = ITEMS.register("wind_stone_block",
            () -> new BlockItem(GenesisBlocks.WIND_STONE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WIND_STONE_CLUSTER = ITEMS.register("wind_stone_cluster",
            () -> new BlockItem(GenesisBlocks.WIND_STONE_CLUSTER.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> WEATHERED_ANCIENT_DRAGON_ROCK = ITEMS.register("weathered_ancient_dragon_rock",
            () -> new BlockItem(GenesisBlocks.WEATHERED_ANCIENT_DRAGON_ROCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> ANCIENT_DRAGON_ROCK = ITEMS.register("ancient_dragon_rock",
            () -> new BlockItem(GenesisBlocks.ANCIENT_DRAGON_ROCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<BlockItem> AMETHYST_SAPLING = ITEMS.register("amethyst_sapling",
            () -> new BlockItem(GenesisBlocks.AMETHYST_SAPLING.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> PRECIPITATE = ITEMS.register("precipitate",
            () -> new BlockItem(GenesisBlocks.PRECIPITATE.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> UNDEAD_SHARD = ITEMS.register("undead_shard",
            () -> new BlockItem(GenesisBlocks.UNDEAD_SHARD.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> HARDENED_RED_GLASS = ITEMS.register("hardened_red_glass",
            () -> new BlockItem(GenesisBlocks.HARDENED_RED_GLASS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RED_PEARL_OF_THE_DESERT = ITEMS.register("red_pearl_of_the_desert",
            () -> new BlockItem(GenesisBlocks.RED_PEARL_OF_THE_DESERT.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<BlockItem> EYE_OF_THE_EARTH = ITEMS.register("eye_of_the_earth",
            () -> new LandEyeBlockItem(GenesisBlocks.EYE_OF_THE_EARTH.get(), new Item.Properties().rarity(Rarity.RARE)));










    public static final RegistryObject<BlockItem> FADED_STONE = ITEMS.register("faded_stone",
            () -> new BlockItem(GenesisBlocks.FADED_STONE.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_STONE_STAIRS = ITEMS.register("faded_stone_stairs",
            () -> new BlockItem(GenesisBlocks.FADED_STONE_STAIRS.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_GATEWAY = ITEMS.register("faded_gateway",
            () -> new BlockItem(GenesisBlocks.FADED_GATEWAY.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_BRICK = ITEMS.register("faded_brick",
            () -> new BlockItem(GenesisBlocks.FADED_BRICK.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_BRICK_STAIRS = ITEMS.register("faded_brick_stairs",
            () -> new BlockItem(GenesisBlocks.FADED_BRICK_STAIRS.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> CHISELED_FADED_BRICK = ITEMS.register("chiseled_faded_brick",
            () -> new BlockItem(GenesisBlocks.CHISELED_FADED_BRICK.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_PILLAR = ITEMS.register("faded_pillar",
            () -> new BlockItem(GenesisBlocks.FADED_PILLAR.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_STONE_SLAB = ITEMS.register("faded_stone_slab",
            () -> new BlockItem(GenesisBlocks.FADED_STONE_SLAB.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_BRICK_SLAB = ITEMS.register("faded_brick_slab",
            () -> new BlockItem(GenesisBlocks.FADED_BRICK_SLAB.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_BRICK_WALL = ITEMS.register("faded_brick_wall",
            () -> new BlockItem(GenesisBlocks.FADED_BRICK_WALL.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> FADED_CHEST = ITEMS.register("faded_chest",
            () -> new BlockItem(GenesisBlocks.FADED_CHEST.get(), new Item.Properties().fireResistant()));

    public static final RegistryObject<BlockItem> MIST_VAULT_1 = ITEMS.register("mist_vault_1",
            () -> new BlockItem(GenesisBlocks.MIST_VAULT_1.get(), new Item.Properties().fireResistant()));




    //item
    public static final RegistryObject<Item> BOOK_OF_CREATION = ITEMS.register("book_of_creation", () -> new BookOfCreationItem(new Item.Properties()));

    public static final RegistryObject<Item> SCATTERED_MEMORIES = ITEMS.register("scattered_memories", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DREAM_POWDER = ITEMS.register("dream_powder",
            () -> new Item(new Item.Properties().food(NightmareRelief.DREAM_POWDER)));
    public static final RegistryObject<Item> DREAM_DANGO = ITEMS.register("dream_dango",
            () -> new Item(new Item.Properties().food(NightmareRelief.DREAM_DANGO)));
    public static final RegistryObject<Item> REMNANTS_OF_A_DREAM = ITEMS.register("remnants_of_a_dream",
            () -> new Item(new Item.Properties().food(NightmareAggravated.REMNANTS_OF_A_DREAM)));
    public static final RegistryObject<Item> FRAGMENT_OF_MEMORY = ITEMS.register("fragment_of_memory",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));


    public static final RegistryObject<Item> MANA_IMBUED_AMETHYST_SHARD = ITEMS.register("mana_imbued_amethyst_shard", () -> new ManaCrystalItem(new Item.Properties(), 5.0f));
    public static final RegistryObject<Item> STAR_FRAGMENT = ITEMS.register("star_fragment", () -> new ManaCrystalItem(new Item.Properties().rarity(Rarity.RARE), 200.0f));
    public static final RegistryObject<Item> AMETHYST_MAGIC_CORE = ITEMS.register("amethyst_magic_core", () -> new ManaCrystalItem(new Item.Properties().rarity(Rarity.UNCOMMON),10.0f));


    public static final RegistryObject<Item> BLUE_CRYSTAL_SHARD = ITEMS.register("blue_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CITRINE_SHARD = ITEMS.register("citrine_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_CRYSTAL_SHARD = ITEMS.register("red_crystal_shard", () -> new FuelItem(new Item.Properties(), 20000));
    public static final RegistryObject<Item> WIND_STONE = ITEMS.register("wind_stone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTING_CRYSTAL_SHARD = ITEMS.register("lighting_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_AMBER = ITEMS.register("green_amber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ICE_FLOWER_SHARD = ITEMS.register("ice_flower_shard", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> PEWRIESE_ORE_PIECE = ITEMS.register("pewriese_ore_piece", () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> ELVENIA_INGOT = ITEMS.register("elvenia_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_PIECE = ITEMS.register("elvenia_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_INGOT = ITEMS.register("ancient_elvenia_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_PIECE = ITEMS.register("ancient_elvenia_piece", () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> PEWRIESE_PIECE = ITEMS.register("pewriese_piece", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CRYSTAL = ITEMS.register("pewriese_crystal", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PYULITELA = ITEMS.register("pyulitela", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> FLASK_SHARD = ITEMS.register("flask_shard", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BEAST_REMAINS = ITEMS.register("beast_remains", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> CARBONIZED_INGOT = ITEMS.register("carbonized_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GIANT_STONE_FRAGMENT = ITEMS.register("giant_stone_fragment", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FUSION_STONE = ITEMS.register("fusion_stone", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITE_IRON_INGOT = ITEMS.register("white_iron_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BASED_SCULPTURE = ITEMS.register("based_sculpture", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));




    public static final RegistryObject<Item> COPPER_COIN = ITEMS.register("copper_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_COIN = ITEMS.register("silver_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIN = ITEMS.register("gold_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATINUM_COIN = ITEMS.register("platinum_coin", () -> new Item(new Item.Properties()));






    //food
    public static final RegistryObject<Item> LUMINOUS_INSECT_JUICE = ITEMS.register("luminous_insect_juice", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));

    public static final RegistryObject<Item> SCORPION_MEAT = ITEMS.register("scorpion_meat", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3f)
                    .build())));
    public static final RegistryObject<Item> COOKED_SCORPION_MEAT = ITEMS.register("cooked_scorpion_meat", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(7).saturationMod(0.9f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0), 1.0f).build())));

    public static final RegistryObject<Item> ENCHANTED_GLOWING_HEART = ITEMS.register("enchanted_glowing_heart", () -> new EGHItem(new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_APPLE_SLICES = ITEMS.register("amethyst_apple_slices", () -> new Item(new Item.Properties().rarity(Rarity.COMMON)
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4f).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400, 1), 1.0f).build())));
    public static final RegistryObject<Item> AMETHYST_APPLE_PUDDING = ITEMS.register("amethyst_apple_pudding", () -> new AmethystApplePuddingItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> OPAQUE_JELLY = ITEMS.register("opaque_jelly", () -> new OpaqueJellyItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOOD_BOTTLE = ITEMS.register("blood_bottle",
            () -> new BloodBottleItem(new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .stacksTo(16)
                    .food(new FoodProperties.Builder().alwaysEat().build())
            ));

    //
    public static final RegistryObject<Item> FADED_MEMORY = ITEMS.register("faded_memory", () -> new MemoryItem(new Item.Properties(), 5));
    public static final RegistryObject<Item> FORGOTTEN_MEMORY = ITEMS.register("forgotten_memory", () -> new MemoryItem(new Item.Properties(), 10));
    public static final RegistryObject<Item> UNRELATED_MEMORY = ITEMS.register("unrelated_memory", () -> new MemoryItem(new Item.Properties().rarity(Rarity.UNCOMMON),30));
    public static final RegistryObject<Item> OBLIVION_SPHERE = ITEMS.register("oblivion_sphere", () -> new MemoryItem(new Item.Properties().rarity(Rarity.RARE),90));



    //block item
    public static final RegistryObject<Item> OBLIVION_CANDLE = ITEMS.register("oblivion_candle", () -> new BlockItem(GenesisBlocks.OBLIVION_CANDLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_APPLE = ITEMS.register("amethyst_apple", () -> new BlockItem(GenesisBlocks.AMETHYST_APPLE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_APPLE_PUDDING_BLOCK = ITEMS.register("amethyst_apple_pudding_block", () -> new BlockItem(GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> COPPER_COIN_PILE = ITEMS.register("copper_coin_pile",
            () -> new BlockItem(GenesisBlocks.COPPER_COIN_PILE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SILVER_COIN_PILE = ITEMS.register("silver_coin_pile",
            () -> new BlockItem(GenesisBlocks.SILVER_COIN_PILE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIN_PILE = ITEMS.register("gold_coin_pile",
            () -> new BlockItem(GenesisBlocks.GOLD_COIN_PILE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PLATINUM_COIN_PILE = ITEMS.register("platinum_coin_pile",
            () -> new BlockItem(GenesisBlocks.PLATINUM_COIN_PILE.get(), new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_HEART = ITEMS.register("amethyst_heart",
            () -> new BlockItem(GenesisBlocks.AMETHYST_HEART.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));


    public static final RegistryObject<Item> STATUE_OF_SENTINEL_OF_OBLIVION = ITEMS.register("statue_of_sentinel_of_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get(), new Item.Properties()));
    public static final RegistryObject<Item> STATUE_OF_HERALD_OF_OBLIVION = ITEMS.register("statue_of_herald_of_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_HERALD_OF_OBLIVION.get(), new Item.Properties()));
    public static final RegistryObject<Item> STATUE_OF_GUIDE_TO_OBLIVION = ITEMS.register("statue_of_guide_to_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_GUIDE_TO_OBLIVION.get(), new Item.Properties()));

    public static final RegistryObject<Item> AEK_STATUE = ITEMS.register("ancient_elf_knight_statue",
            () -> new BlockItem(GenesisBlocks.AEK_STATUE.get(), new Item.Properties()));

    public static final RegistryObject<Item> GOD_STATUE_A = ITEMS.register("god_statue_a",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_A.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_B = ITEMS.register("god_statue_b",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_B.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_C = ITEMS.register("god_statue_c",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_C.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_D = ITEMS.register("god_statue_d",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_D.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_E = ITEMS.register("god_statue_e",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_E.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_F = ITEMS.register("god_statue_f",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_F.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_G = ITEMS.register("god_statue_g",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_G.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> GOD_STATUE_H = ITEMS.register("god_statue_h",
            () -> new BlockItem(GenesisBlocks.GOD_STATUE_H.get(), new Item.Properties().fireResistant()));

    //tool,weapon
    public static final RegistryObject<Item> DIVINE_GRAIL = ITEMS.register("divine_grail", () -> new DivineGrailItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SPIRIT_COMPASS = ITEMS.register("spirit_compass", () -> new SpiritCompassItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));


    public static final RegistryObject<Item> HARDENED_GLASS_SWORD = ITEMS.register("hardened_glass_sword", () -> new SwordItem(GenesisItemTier.HARDENED_GLASS, 3, -2.4f,
            new Item.Properties()));
    public static final RegistryObject<Item> HARDENED_GLASS_GREATSWORD = ITEMS.register("hardened_glass_greatsword", () -> new GreatswordItem(new Item.Properties()
            , GenesisItemTier.HARDENED_GLASS));
    public static final RegistryObject<Item> HARDENED_GLASS_SPEAR = ITEMS.register("hardened_glass_spear", () -> new SpearItem(new Item.Properties()
            , GenesisItemTier.HARDENED_GLASS));
    public static final RegistryObject<Item> HARDENED_GLASS_TACHI = ITEMS.register("hardened_glass_tachi", () -> new TachiItem(new Item.Properties()
            , GenesisItemTier.HARDENED_GLASS));
    public static final RegistryObject<Item> HARDENED_GLASS_LONGSWORD = ITEMS.register("hardened_glass_longsword", () -> new LongswordItem(new Item.Properties()
            , GenesisItemTier.HARDENED_GLASS));
    public static final RegistryObject<Item> HARDENED_GLASS_DAGGER = ITEMS.register("hardened_glass_dagger", () -> new DaggerItem(new Item.Properties()
            , GenesisItemTier.HARDENED_GLASS));

    public static final RegistryObject<Item> HARDENED_RED_GLASS_SWORD = ITEMS.register("hardened_red_glass_sword", () -> new SwordItem(GenesisItemTier.HARDENED_RED_GLASS, 3, -2.4f,
            new Item.Properties()));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_GREATSWORD = ITEMS.register("hardened_red_glass_greatsword", () -> new GreatswordItem(new Item.Properties()
            , GenesisItemTier.HARDENED_RED_GLASS));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_SPEAR = ITEMS.register("hardened_red_glass_spear", () -> new SpearItem(new Item.Properties()
            , GenesisItemTier.HARDENED_RED_GLASS));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_TACHI = ITEMS.register("hardened_red_glass_tachi", () -> new TachiItem(new Item.Properties()
            , GenesisItemTier.HARDENED_RED_GLASS));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_LONGSWORD = ITEMS.register("hardened_red_glass_longsword", () -> new LongswordItem(new Item.Properties()
            , GenesisItemTier.HARDENED_RED_GLASS));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_DAGGER = ITEMS.register("hardened_red_glass_dagger", () -> new DaggerItem(new Item.Properties()
            , GenesisItemTier.HARDENED_RED_GLASS));

    public static final RegistryObject<Item> ELVENIA_SWORD = ITEMS.register("elvenia_sword", () -> new SwordItem(GenesisItemTier.ELVENIA, 3, -2.4f,
            new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_SHOVEL = ITEMS.register("elvenia_shovel", () -> new ShovelItem(GenesisItemTier.ELVENIA, 1.5f, -3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_PICKAXE = ITEMS.register("elvenia_pickaxe", () -> new PickaxeItem(GenesisItemTier.ELVENIA, 1, -2.8f,
            new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_AXE = ITEMS.register("elvenia_axe", () -> new AxeItem(GenesisItemTier.ELVENIA, 6, -3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_HOE = ITEMS.register("elvenia_hoe", () -> new HoeItem(GenesisItemTier.ELVENIA, -2, 0.0f,
            new Item.Properties()));

    public static final RegistryObject<Item> ELVENIA_GREATSWORD = ITEMS.register("elvenia_greatsword", () -> new GreatswordItem(new Item.Properties()
            , GenesisItemTier.ELVENIA));
    public static final RegistryObject<Item> ELVENIA_SPEAR = ITEMS.register("elvenia_spear", () -> new SpearItem(new Item.Properties()
            , GenesisItemTier.ELVENIA));
    public static final RegistryObject<Item> ELVENIA_TACHI = ITEMS.register("elvenia_tachi", () -> new TachiItem(new Item.Properties()
            , GenesisItemTier.ELVENIA));
    public static final RegistryObject<Item> ELVENIA_LONGSWORD = ITEMS.register("elvenia_longsword", () -> new LongswordItem(new Item.Properties()
            , GenesisItemTier.ELVENIA));
    public static final RegistryObject<Item> ELVENIA_DAGGER = ITEMS.register("elvenia_dagger", () -> new DaggerItem(new Item.Properties()
            , GenesisItemTier.ELVENIA));

    public static final RegistryObject<Item> ANCIENT_ELVENIA_SWORD = ITEMS.register("ancient_elvenia_sword", () -> new SwordItem(GenesisItemTier.ANCIENT_ELVENIA, 3, -2.4f,
            new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_SHOVEL = ITEMS.register("ancient_elvenia_shovel", () -> new ShovelItem(GenesisItemTier.ANCIENT_ELVENIA, 1.5f, -3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_PICKAXE = ITEMS.register("ancient_elvenia_pickaxe", () -> new PickaxeItem(GenesisItemTier.ANCIENT_ELVENIA, 1, -2.8f,
            new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_AXE = ITEMS.register("ancient_elvenia_axe", () -> new AxeItem(GenesisItemTier.ANCIENT_ELVENIA, 5, -3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_HOE = ITEMS.register("ancient_elvenia_hoe", () -> new HoeItem(GenesisItemTier.ANCIENT_ELVENIA, -3, 0.0f,
            new Item.Properties()));

    public static final RegistryObject<Item> ANCIENT_ELVENIA_GREATSWORD = ITEMS.register("ancient_elvenia_greatsword", () -> new GreatswordItem(new Item.Properties()
            , GenesisItemTier.ANCIENT_ELVENIA));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_SPEAR = ITEMS.register("ancient_elvenia_spear", () -> new SpearItem(new Item.Properties()
            , GenesisItemTier.ANCIENT_ELVENIA));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_TACHI = ITEMS.register("ancient_elvenia_tachi", () -> new TachiItem(new Item.Properties()
            , GenesisItemTier.ANCIENT_ELVENIA));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_LONGSWORD = ITEMS.register("ancient_elvenia_longsword", () -> new LongswordItem(new Item.Properties()
            , GenesisItemTier.ANCIENT_ELVENIA));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_DAGGER = ITEMS.register("ancient_elvenia_dagger", () -> new DaggerItem(new Item.Properties()
            , GenesisItemTier.ANCIENT_ELVENIA));

    public static final RegistryObject<Item> CARBONIZED_SWORD = ITEMS.register("carbonized_sword", () -> new SwordItem(GenesisItemTier.CARBONIZED, 3, -2.6f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CARBONIZED_SHOVEL = ITEMS.register("carbonized_shovel", () -> new ShovelItem(GenesisItemTier.CARBONIZED, 1.5f, -3.2f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CARBONIZED_PICKAXE = ITEMS.register("carbonized_pickaxe", () -> new PickaxeItem(GenesisItemTier.CARBONIZED, 1, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CARBONIZED_AXE = ITEMS.register("carbonized_axe", () -> new AxeItem(GenesisItemTier.CARBONIZED, 5, -3.2f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CARBONIZED_HOE = ITEMS.register("carbonized_hoe", () -> new HoeItem(GenesisItemTier.CARBONIZED, -4, 0.0f,
            new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> CARBONIZED_GREATSWORD = ITEMS.register("carbonized_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.CARBONIZED));
    public static final RegistryObject<Item> CARBONIZED_SPEAR = ITEMS.register("carbonized_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            , GenesisItemTier.CARBONIZED));
    public static final RegistryObject<Item> CARBONIZED_TACHI = ITEMS.register("carbonized_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            , GenesisItemTier.CARBONIZED));
    public static final RegistryObject<Item> CARBONIZED_LONGSWORD = ITEMS.register("carbonized_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.CARBONIZED));
    public static final RegistryObject<Item> CARBONIZED_DAGGER = ITEMS.register("carbonized_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            , GenesisItemTier.CARBONIZED));

    public static final RegistryObject<Item> PEWRIESE_SWORD = ITEMS.register("pewriese_sword", () -> new SwordItem(GenesisItemTier.PEWRIESE, 3, -2.4f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_SHOVEL = ITEMS.register("pewriese_shovel", () -> new ShovelItem(GenesisItemTier.PEWRIESE, 1.5f, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PICKAXE = ITEMS.register("pewriese_pickaxe", () -> new PickaxeItem(GenesisItemTier.PEWRIESE, 1, -2.8f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_AXE = ITEMS.register("pewriese_axe", () -> new AxeItem(GenesisItemTier.PEWRIESE, 5, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_HOE = ITEMS.register("pewriese_hoe", () -> new HoeItem(GenesisItemTier.PEWRIESE, -4, 0.0f,
            new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PEWRIESE_GREATSWORD = ITEMS.register("pewriese_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PEWRIESE_SPEAR = ITEMS.register("pewriese_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PEWRIESE_TACHI = ITEMS.register("pewriese_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PEWRIESE_LONGSWORD = ITEMS.register("pewriese_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PEWRIESE_DAGGER = ITEMS.register("pewriese_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));

    public static final RegistryObject<Item> PEWRIESE_GAUNTLET = ITEMS.register("pewriese_gauntlet", () -> new GloveItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));


    public static final RegistryObject<Item> PURTRUCTION_SWORD = ITEMS.register("purtruction_sword", () -> new SwordItem(GenesisItemTier.PEWRIESE, 3, -2.4f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PURTRUCTION_SHOVEL = ITEMS.register("purtruction_shovel", () -> new ShovelItem(GenesisItemTier.PEWRIESE, 1.5f, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PURTRUCTION_PICKAXE = ITEMS.register("purtruction_pickaxe", () -> new PickaxeItem(GenesisItemTier.PEWRIESE, 1, -2.8f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PURTRUCTION_AXE = ITEMS.register("purtruction_axe", () -> new AxeItem(GenesisItemTier.PEWRIESE, 5, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PURTRUCTION_HOE = ITEMS.register("purtruction_hoe", () -> new HoeItem(GenesisItemTier.PEWRIESE, -4, 0.0f,
            new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PURTRUCTION_GREATSWORD = ITEMS.register("purtruction_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PURTRUCTION_SPEAR = ITEMS.register("purtruction_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PURTRUCTION_TACHI = ITEMS.register("purtruction_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PURTRUCTION_LONGSWORD = ITEMS.register("purtruction_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> PURTRUCTION_DAGGER = ITEMS.register("purtruction_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PEWRIESE));


    public static final RegistryObject<Item> HOLY_KNIGHT_SWORD = ITEMS.register("holy_knight_sword", () -> new SwordItem(GenesisItemTier.HOLY_KNIGHT, 3, -2.4f,
            new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> HOLY_KNIGHT_GREATSWORD = ITEMS.register("holy_knight_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            .rarity(Rarity.UNCOMMON), GenesisItemTier.HOLY_KNIGHT));
    public static final RegistryObject<Item> HOLY_KNIGHT_SPEAR = ITEMS.register("holy_knight_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            .rarity(Rarity.UNCOMMON), GenesisItemTier.HOLY_KNIGHT));
    public static final RegistryObject<Item> HOLY_KNIGHT_TACHI = ITEMS.register("holy_knight_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            .rarity(Rarity.UNCOMMON), GenesisItemTier.HOLY_KNIGHT));
    public static final RegistryObject<Item> HOLY_KNIGHT_LONGSWORD = ITEMS.register("holy_knight_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            .rarity(Rarity.UNCOMMON), GenesisItemTier.HOLY_KNIGHT));
    public static final RegistryObject<Item> HOLY_KNIGHT_DAGGER = ITEMS.register("holy_knight_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            .rarity(Rarity.UNCOMMON), GenesisItemTier.HOLY_KNIGHT));

    public static final RegistryObject<Item> PYULITELA_SWORD = ITEMS.register("pyulitela_sword", () -> new SwordItem(GenesisItemTier.PYULITELA, 3, -2.4f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PYULITELA_SHOVEL = ITEMS.register("pyulitela_shovel", () -> new ShovelItem(GenesisItemTier.PYULITELA, 1.5f, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PYULITELA_PICKAXE = ITEMS.register("pyulitela_pickaxe", () -> new PickaxeItem(GenesisItemTier.PYULITELA, 1, -2.8f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PYULITELA_AXE = ITEMS.register("pyulitela_axe", () -> new AxeItem(GenesisItemTier.PYULITELA, 5, -3.0f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PYULITELA_HOE = ITEMS.register("pyulitela_hoe", () -> new HoeItem(GenesisItemTier.PYULITELA, -4, 0.0f,
            new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PYULITELA_GREATSWORD = ITEMS.register("pyulitela_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PYULITELA));
    public static final RegistryObject<Item> PYULITELA_SPEAR = ITEMS.register("pyulitela_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PYULITELA));
    public static final RegistryObject<Item> PYULITELA_TACHI = ITEMS.register("pyulitela_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PYULITELA));
    public static final RegistryObject<Item> PYULITELA_LONGSWORD = ITEMS.register("pyulitela_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PYULITELA));
    public static final RegistryObject<Item> PYULITELA_DAGGER = ITEMS.register("pyulitela_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            , GenesisItemTier.PYULITELA));



    public static final RegistryObject<Item> DEPTHS_SWORD = ITEMS.register("depths_sword", () -> new SwordItem(GenesisItemTier.PEWRIESE, 3, -2.4f,
            new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> DEPTHS_GREATSWORD = ITEMS.register("depths_greatsword", () -> new GreatswordItem(new Item.Properties().fireResistant()
            .rarity(Rarity.RARE), GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> DEPTHS_SPEAR = ITEMS.register("depths_spear", () -> new SpearItem(new Item.Properties().fireResistant()
            .rarity(Rarity.RARE), GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> DEPTHS_TACHI = ITEMS.register("depths_tachi", () -> new TachiItem(new Item.Properties().fireResistant()
            .rarity(Rarity.RARE), GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> DEPTHS_LONGSWORD = ITEMS.register("depths_longsword", () -> new LongswordItem(new Item.Properties().fireResistant()
            .rarity(Rarity.RARE), GenesisItemTier.PEWRIESE));
    public static final RegistryObject<Item> DEPTHS_DAGGER = ITEMS.register("depths_dagger", () -> new DaggerItem(new Item.Properties().fireResistant()
            .rarity(Rarity.RARE), GenesisItemTier.PEWRIESE));

    public static final RegistryObject<Item> GIANT_STONE_GREATSWORD = ITEMS.register("giant_stone_greatsword", () -> new GreatswordItem(new Item.Properties()
            .rarity(Rarity.RARE), GenesisItemTier.GIANT_STONE));
    public static final RegistryObject<Item> ACTIVATED_GIANT_STONE_GREATSWORD = ITEMS.register("activated_giant_stone_greatsword", () -> new GreatswordItem(new Item.Properties()
            .rarity(Rarity.RARE), GenesisItemTier.GIANT_STONE));

    public static final RegistryObject<Item> CRYSTAL_GROWN_LONGSWORD = ITEMS.register("crystal_grown_longsword", () -> new LongswordItem(new Item.Properties()
            .rarity(Rarity.RARE), Tiers.DIAMOND));

    public static final RegistryObject<Item> OATH_IN_DEEP_DARK = ITEMS.register("oath_in_deep_dark",
            () -> new SwordItem(GenesisItemTier.PEWRIESE, 3, -2.4f, new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> BAILIFF_LONGSWORD = ITEMS.register("bailiff_longsword", () -> new LongswordItem(new Item.Properties()
            .rarity(Rarity.RARE), GenesisItemTier.HOLY_KNIGHT));

    public static final RegistryObject<Item> TAKRYU = ITEMS.register("takryu", () -> new UchigatanaItem(new Item.Properties()
            .rarity(Rarity.RARE)));




    public static final RegistryObject<Item> GREAT_BOW = ITEMS.register("great_bow", () -> new GreatBowItem(new Item.Properties()
            , GreatBowTier.NORMAL));

    public static final RegistryObject<Item> ROOT_WOVEN_BOW = ITEMS.register("root_woven_bow", () -> new PoisonGreatBowItem(new Item.Properties()
            , GreatBowTier.ROOT_WOVEN));

    public static final RegistryObject<Item> ELVENIA_GREAT_BOW = ITEMS.register("elvenia_great_bow", () -> new GreatBowItem(new Item.Properties()
            , GreatBowTier.ELVENIA));

    public static final RegistryObject<Item> ANCIENT_ELVENIA_GREAT_BOW = ITEMS.register("ancient_elvenia_great_bow", () -> new GreatBowItem(new Item.Properties()
            , GreatBowTier.ANCIENT_ELVENIA));

    public static final RegistryObject<Item> PEWRIESE_GREAT_BOW = ITEMS.register("pewriese_great_bow", () -> new GreatBowItem(new Item.Properties()
            , GreatBowTier.PEWRIESE));

    public static final RegistryObject<Item> LARGE_ARROW = ITEMS.register("large_arrow", () -> new LargeArrowItem(new Item.Properties()));

    //wand&seal
    public static final RegistryObject<Item> AMETHYST_WAND = ITEMS.register("amethyst_wand",
            () -> new CatalystItem(new Item.Properties().stacksTo(1).durability(193)));

    public static final RegistryObject<Item> GREEN_STAR_SEAL = ITEMS.register("green_star_seal",
            () -> new CatalystItem(new Item.Properties().stacksTo(1).durability(211)));

    public static final RegistryObject<Item> HAND_HARBORING_OBLIVION = ITEMS.register("hand_harboring_oblivion",
            () -> new CatalystItem(new Item.Properties().stacksTo(1).durability(347).rarity(Rarity.UNCOMMON)));


    //spell
    public static final RegistryObject<Item> FIREBALL = ITEMS.register("fireball",
            () -> new SpellBookItem("fireball", new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LITTLE_HEAL = ITEMS.register("little_heal",
            () -> new SpellBookItem("little_heal", new Item.Properties().stacksTo(1)));


    //armor

    public static final RegistryObject<Item> PADDED_CHAIN_HELMET = ITEMS.register("padded_chain_helmet", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_CHESTPLATE = ITEMS.register("padded_chain_chestplate", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_LEGGINGS = ITEMS.register("padded_chain_leggings", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_BOOTS = ITEMS.register("padded_chain_boots", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> SCORPION_HELMET = ITEMS.register("scorpion_helmet", () -> new GenesisArmorItem(GenesisArmorMaterials.SCORPION,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> SCORPION_CHESTPLATE = ITEMS.register("scorpion_chestplate", () -> new GenesisArmorItem(GenesisArmorMaterials.SCORPION,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> SCORPION_LEGGINGS = ITEMS.register("scorpion_leggings", () -> new GenesisArmorItem(GenesisArmorMaterials.SCORPION,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> SCORPION_BOOTS = ITEMS.register("scorpion_boots", () -> new GenesisArmorItem(GenesisArmorMaterials.SCORPION,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> ELVENIA_HELMET = ITEMS.register("elvenia_helmet", () -> new ElveniaArmor(GenesisArmorMaterials.ELVENIA,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_CHESTPLATE = ITEMS.register("elvenia_chestplate", () -> new ElveniaArmor(GenesisArmorMaterials.ELVENIA,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_LEGGINGS = ITEMS.register("elvenia_leggings", () -> new ElveniaArmor(GenesisArmorMaterials.ELVENIA,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_BOOTS = ITEMS.register("elvenia_boots", () -> new ElveniaArmor(GenesisArmorMaterials.ELVENIA,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> ANCIENT_ELVENIA_HELMET = ITEMS.register("ancient_elvenia_helmet", () -> new AncientElveniaArmor(GenesisArmorMaterials.ANCIENT_ELVENIA,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_CHESTPLATE = ITEMS.register("ancient_elvenia_chestplate", () -> new AncientElveniaArmor(GenesisArmorMaterials.ANCIENT_ELVENIA,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_LEGGINGS = ITEMS.register("ancient_elvenia_leggings", () -> new AncientElveniaArmor(GenesisArmorMaterials.ANCIENT_ELVENIA,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_BOOTS = ITEMS.register("ancient_elvenia_boots", () -> new AncientElveniaArmor(GenesisArmorMaterials.ANCIENT_ELVENIA,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> CARBONIZED_HELMET = ITEMS.register("carbonized_helmet", () -> new CarbonizedArmor(GenesisArmorMaterials.CARBONIZED,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> CARBONIZED_CHESTPLATE = ITEMS.register("carbonized_chestplate", () -> new CarbonizedArmor(GenesisArmorMaterials.CARBONIZED,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> CARBONIZED_LEGGINGS = ITEMS.register("carbonized_leggings", () -> new CarbonizedArmor(GenesisArmorMaterials.CARBONIZED,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> CARBONIZED_BOOTS = ITEMS.register("carbonized_boots", () -> new CarbonizedArmor(GenesisArmorMaterials.CARBONIZED,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_HELMET = ITEMS.register("amethyst_helmet", () -> new GenesisArmorItem(GenesisArmorMaterials.AMETHYST,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_CHESTPLATE = ITEMS.register("amethyst_chestplate", () -> new GenesisArmorItem(GenesisArmorMaterials.AMETHYST,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_LEGGINGS = ITEMS.register("amethyst_leggings", () -> new GenesisArmorItem(GenesisArmorMaterials.AMETHYST,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_BOOTS = ITEMS.register("amethyst_boots", () -> new GenesisArmorItem(GenesisArmorMaterials.AMETHYST,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> PEWRIESE_HELMET = ITEMS.register("pewriese_helmet", () -> new GenesisArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CHESTPLATE = ITEMS.register("pewriese_chestplate", () -> new GenesisArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_LEGGINGS = ITEMS.register("pewriese_leggings", () -> new GenesisArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_BOOTS = ITEMS.register("pewriese_boots", () -> new GenesisArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PEWRIESE_PLATE_HELMET = ITEMS.register("pewriese_plate_helmet", () -> new PewriesePlateArmor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_CHESTPLATE = ITEMS.register("pewriese_plate_chestplate", () -> new PewriesePlateArmor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_LEGGINGS = ITEMS.register("pewriese_plate_leggings", () -> new PewriesePlateArmor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_BOOTS = ITEMS.register("pewriese_plate_boots", () -> new PewriesePlateArmor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> HOLY_KNIGHT_HELMET = ITEMS.register("holy_knight_helmet", () -> new HolyKnightArmor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_CHESTPLATE = ITEMS.register("holy_knight_chestplate", () -> new HolyKnightArmor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_LEGGINGS = ITEMS.register("holy_knight_leggings", () -> new HolyKnightArmor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_BOOTS = ITEMS.register("holy_knight_boots", () -> new HolyKnightArmor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BAILIFF_HELMET = ITEMS.register("bailiff_helmet", () -> new BailiffArmor(GenesisArmorMaterials.BAILIFF,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BAILIFF_CHESTPLATE = ITEMS.register("bailiff_chestplate", () -> new BailiffArmor(GenesisArmorMaterials.BAILIFF,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BAILIFF_LEGGINGS = ITEMS.register("bailiff_leggings", () -> new BailiffArmor(GenesisArmorMaterials.BAILIFF,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BAILIFF_BOOTS = ITEMS.register("bailiff_boots", () -> new BailiffArmor(GenesisArmorMaterials.BAILIFF,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant().rarity(Rarity.RARE)));



    public static final RegistryObject<Item> CLOTH_BANDANA = ITEMS.register("cloth_bandana", () -> new ClothArmor(GenesisArmorMaterials.CLOTH,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> CLOTH_ROBE = ITEMS.register("cloth_robe", () -> new ClothArmor(GenesisArmorMaterials.CLOTH,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> CLOTH_LEGGINGS = ITEMS.register("cloth_leggings", () -> new ClothArmor(GenesisArmorMaterials.CLOTH,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> CLOTH_BOOTS = ITEMS.register("cloth_boots", () -> new ClothArmor(GenesisArmorMaterials.CLOTH,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> PILGRIM_BANDANA = ITEMS.register("pilgrim_bandana", () -> new PilgrimArmor(GenesisArmorMaterials.PILGRIM,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> PILGRIM_ROBE = ITEMS.register("pilgrim_robe", () -> new PilgrimArmor(GenesisArmorMaterials.PILGRIM,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> PILGRIM_LEGGINGS = ITEMS.register("pilgrim_leggings", () -> new PilgrimArmor(GenesisArmorMaterials.PILGRIM,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> PILGRIM_BOOTS = ITEMS.register("pilgrim_boots", () -> new PilgrimArmor(GenesisArmorMaterials.PILGRIM,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> ASTROLOGER_BANDANA = ITEMS.register("astrologer_bandana", () -> new AstrologerArmor(GenesisArmorMaterials.ASTROLOGER,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> ASTROLOGER_ROBE = ITEMS.register("astrologer_robe", () -> new AstrologerArmor(GenesisArmorMaterials.ASTROLOGER,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> ASTROLOGER_LEGGINGS = ITEMS.register("astrologer_leggings", () -> new AstrologerArmor(GenesisArmorMaterials.ASTROLOGER,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> ASTROLOGER_BOOTS = ITEMS.register("astrologer_boots", () -> new AstrologerArmor(GenesisArmorMaterials.ASTROLOGER,
            ArmorItem.Type.BOOTS,new Item.Properties()));

    public static final RegistryObject<Item> EMBROIDERED_VEIL = ITEMS.register("embroidered_veil", () -> new EmbroideredArmor(GenesisArmorMaterials.EMBROIDERED,
            ArmorItem.Type.HELMET,new Item.Properties()));


    public static final RegistryObject<Item> WHITE_IRON_HELMET = ITEMS.register("white_iron_helmet", () -> new GenesisArmorItem(GenesisArmorMaterials.WHITE_IRON,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> WHITE_IRON_CHESTPLATE = ITEMS.register("white_iron_chestplate", () -> new GenesisArmorItem(GenesisArmorMaterials.WHITE_IRON,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> WHITE_IRON_LEGGINGS = ITEMS.register("white_iron_leggings", () -> new GenesisArmorItem(GenesisArmorMaterials.WHITE_IRON,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> WHITE_IRON_BOOTS = ITEMS.register("white_iron_boots", () -> new GenesisArmorItem(GenesisArmorMaterials.WHITE_IRON,
            ArmorItem.Type.BOOTS,new Item.Properties()));


    //accessories
    public static final RegistryObject<Item> INTACT_AMETHYST_HEART = ITEMS.register("intact_amethyst_heart", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STAR_OF_DOMINATION = ITEMS.register("star_of_domination", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));



    //misc

    public static final RegistryObject<Item> AMETHYST_HUMAN_STATUE = ITEMS.register("amethyst_human_statue", () -> new Item(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> LAGER_DESERT_SCORPION_TAIL = ITEMS.register("large_desert_scorpion_tail", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LAGER_DESERT_SCORPION_PINCERS = ITEMS.register("large_desert_scorpion_pincers", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SCORPION_CARAPACE = ITEMS.register("scorpion_carapace", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SACRED_STONE = ITEMS.register("sacred_stone", () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> CLOTH = ITEMS.register("cloth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ENCHANTED_CLOTH = ITEMS.register("enchanted_cloth", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLESSED_CLOTH = ITEMS.register("blessed_cloth", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ILLUSION_SILK = ITEMS.register("illusion_silk", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_FIBER = ITEMS.register("metal_fiber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RED_EYE_SMALL = ITEMS.register("red_eye_small", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_EYE_MEDIUM = ITEMS.register("red_eye_medium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_EYE_LARGE = ITEMS.register("red_eye_large", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HARDENED_GLASS_PIECES = ITEMS.register("hardened_glass_pieces", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HARDENED_RED_GLASS_PIECES = ITEMS.register("hardened_red_glass_pieces", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HARDENED_RED_MASS = ITEMS.register("hardened_red_mass", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ECHOING_SOUL = ITEMS.register("echoing_soul", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_PUS = ITEMS.register("soul_pus", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTAL_BAT_HIDE = ITEMS.register("crystal_bat_hide", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_BONE = ITEMS.register("amethyst_bone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_HEART_PIECE = ITEMS.register("amethyst_heart_piece", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> AMETHYST_SHIELD_SHARD = ITEMS.register("amethyst_shield_shard", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> SMALL_BELL_OF_OBLIVION = ITEMS.register("small_bell_of_oblivion", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FOG_GUARDIAN_SHARD = ITEMS.register("fog_guardian_shard", () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> UNDERGROUND_BONE = ITEMS.register("underground_bone", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNDEAD_REMNANT = ITEMS.register("undead_remnant", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNFINISHED_SHIELD = ITEMS.register("unfinished_shield", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> SKULLK_SPROUT = ITEMS.register("skullk_sprout", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRYSTALS_OF_THE_LAND = ITEMS.register("crystals_of_the_land", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> ELVENIA_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("elvenia_upgrade_smithing_template", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEWRIESE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("pewriese_upgrade_smithing_template", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> SCALE_FOSSIL_SHARD = ITEMS.register("scale_fossil_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCALE_FOSSIL = ITEMS.register("scale_fossil", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCALE_FOSSIL_CLUMP = ITEMS.register("scale_fossil_clump", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_DRAGON_SCALE = ITEMS.register("ancient_dragon_scale", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAGON_KING_SCALE = ITEMS.register("dragon_king_scale", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SHARD_OF_THE_MOUNTAIN = ITEMS.register("shard_of_the_mountain", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> FRAGMENT_OF_THE_MOUNTAIN = ITEMS.register("fragment_of_the_mountain", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> CLUMP_OF_THE_MOUNTAIN = ITEMS.register("clump_of_the_mountain", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TABLET_SHARD = ITEMS.register("tablet_shard", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> TABLET_OF_THE_RADIANT_MOUNTAIN = ITEMS.register("tablet_of_the_radiant_mountain", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));


    public static final RegistryObject<Item> BLADE_OF_DESTRUCTION_FRAGMENT = ITEMS.register("blade_of_destruction_fragment",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> KEY_OF_OBLIVION = ITEMS.register("key_of_oblivion", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> MISY_CORE_1 = ITEMS.register("mist_core_1", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));


    public static final RegistryObject<Item> MEDALLION_OF_DOMINION = ITEMS.register("medallion_of_dominion",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> LITTLE_STAR_OF_CLARITY = ITEMS.register("little_star_of_clarity",
        () -> new SpellExtensionItem(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> FABRICATED_STAR = ITEMS.register("fabricated_star", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CELESTIAL_STAR = ITEMS.register("celestial_star", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> EPONYMOUS_STAR = ITEMS.register("eponymous_star", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));



    public static final RegistryObject<Item> TAKRYU_SHEATH = ITEMS.register("takryu_sheath", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> QUICKSAND_BUCKET =
            ITEMS.register("quicksand_bucket",
                    () -> new BucketItem(GenesisFluids.QUICKSAND, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> BLOOD_BUCKET =
            ITEMS.register("blood_bucket",
                    () -> new BucketItem(GenesisFluids.BLOOD,
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> BASE_SPELL = ITEMS.register("base_spell", () -> new Item(new Item.Properties()));

    //Scroll
    public static final RegistryObject<Item> HEAL_SCROLL_1 = ITEMS.register("heal_scroll_1",
            () -> new GenericScrollItem(
                    new Item.Properties(), 3.0f, 3,
                    (level, player) -> {
                        player.heal(9.0f);
                    }
            ));


    //spawn egg
    public static final RegistryObject<Item> COLLECTOR_SPAWN_EGG = ITEMS.register("collector_spawn_egg", () -> new ForgeSpawnEggItem(GenesisEntities.COLLECTOR, 0xFFFFFF, 0xFFFFFF,
             new Item.Properties()));
    public static final RegistryObject<Item> COLLECTOR_GUARD_SPAWN_EGG = ITEMS.register("collector_guard_spawn_egg", () -> new ForgeSpawnEggItem(GenesisEntities.COLLECTOR_GUARD, 0xFFFFFF, 0xFFFFFF,
            new Item.Properties()));



}
