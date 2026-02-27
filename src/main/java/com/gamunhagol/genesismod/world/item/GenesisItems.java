package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisItemTier;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;
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
    public static final RegistryObject<BlockItem> SILVER_ORE = ITEMS.register("silver_ore",
            () -> new BlockItem(GenesisBlocks.SILVER_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DEEPSLATEL_SILVER_ORE = ITEMS.register("deepslate_silver_ore",
            () -> new BlockItem(GenesisBlocks.DEEPSLATE_SILVER_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block",
            () -> new BlockItem(GenesisBlocks.RAW_SILVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SILVER_BLOCK = ITEMS.register("silver_block",
            () -> new BlockItem(GenesisBlocks.SILVER_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> ELVENIA_BLOCK = ITEMS.register("elvenia_block",
            () -> new BlockItem(GenesisBlocks.ELVENIA_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ANCIENT_ELVENIA_BLOCK = ITEMS.register("ancient_elvenia_block",
            () -> new BlockItem(GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> PEWRIESE_ORE = ITEMS.register("pewriese_ore",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PEWRIESE_CRYSTAL_BLOCK = ITEMS.register("pewriese_crystal_block",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get(), new Item.Properties().fireResistant()));


    public static final RegistryObject<BlockItem> PYULITELA_ORE = ITEMS.register("pyulitela_ore",
            () -> new BlockItem(GenesisBlocks.PYULITELA_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PYULITELA_BLOCK = ITEMS.register("pyulitela_block",
            () -> new BlockItem(GenesisBlocks.PYULITELA_BLOCK.get(), new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<BlockItem> GIANT_STONE = ITEMS.register("giant_stone",
            () -> new BlockItem(GenesisBlocks.GIANT_STONE.get(), new Item.Properties()));

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


    public static final RegistryObject<Item> MANA_IMBUED_AMETHYST_SHARD = ITEMS.register("mana_imbued_amethyst_shard", () -> new ManaCrystalItem(new Item.Properties()));

    public static final RegistryObject<Item> BLUE_CRYSTAL_SHARD = ITEMS.register("blue_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CITRINE_SHARD = ITEMS.register("citrine_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_CRYSTAL_SHARD = ITEMS.register("red_crystal_shard", () -> new FuelItem(new Item.Properties(), 20000));
    public static final RegistryObject<Item> WIND_STONE = ITEMS.register("wind_stone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTING_CRYSTAL_SHARD = ITEMS.register("lighting_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_AMBER = ITEMS.register("green_amber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ICE_FLOWER_SHARD = ITEMS.register("ice_flower_shard", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEWRIESE_ORE_PIECE = ITEMS.register("pewriese_ore_piece", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SILVER_PIECE = ITEMS.register("silver_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELVENIA_INGOT = ITEMS.register("elvenia_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELVENIA_PIECE = ITEMS.register("elvenia_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_INGOT = ITEMS.register("ancient_elvenia_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ELVENIA_PIECE = ITEMS.register("ancient_elvenia_piece", () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> PEWRIESE_PIECE = ITEMS.register("pewriese_piece", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CRYSTAL = ITEMS.register("pewriese_crystal", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PYULITELA = ITEMS.register("pyulitela", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> FLASK_SHARD = ITEMS.register("flask_shard", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BEAST_REMAINS = ITEMS.register("beast_remains", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> COPPER_COIN = ITEMS.register("copper_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_COIN = ITEMS.register("silver_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIN = ITEMS.register("gold_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATINUM_COIN = ITEMS.register("platinum_coin", () -> new Item(new Item.Properties()));






    //food
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


    public static final RegistryObject<Item> STATUE_OF_SENTINEL_OF_OBLIVION = ITEMS.register("statue_of_sentinel_of_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_SENTINEL_OF_OBLIVION.get(), new Item.Properties()));
    public static final RegistryObject<Item> STATUE_OF_HERALD_OF_OBLIVION = ITEMS.register("statue_of_herald_of_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_HERALD_OF_OBLIVION.get(), new Item.Properties()));
    public static final RegistryObject<Item> STATUE_OF_GUIDE_TO_OBLIVION = ITEMS.register("statue_of_guide_to_oblivion",
            () -> new BlockItem(GenesisBlocks.STATUE_OF_GUIDE_TO_OBLIVION.get(), new Item.Properties()));

    public static final RegistryObject<Item> AEK_STATUE = ITEMS.register("ancient_elf_knight_statue",
            () -> new BlockItem(GenesisBlocks.AEK_STATUE.get(), new Item.Properties()));

//tool,weapon
    public static final RegistryObject<Item> DIVINE_GRAIL = ITEMS.register("divine_grail", () -> new DivineGrailItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SPIRIT_COMPASS = ITEMS.register("spirit_compass", () -> new SpiritCompassItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));

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

    public static final RegistryObject<Item> HOLY_KNIGHT_SWORD = ITEMS.register("holy_knight_sword", () -> new SwordItem(GenesisItemTier.PEWRIESE, 3, -2.4f,
            new Item.Properties().fireResistant()));

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


    //armor

    public static final RegistryObject<Item> PADDED_CHAIN_HELMET = ITEMS.register("padded_chain_helmet", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.HELMET,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_CHESTPLATE = ITEMS.register("padded_chain_chestplate", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.CHESTPLATE,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_LEGGINGS = ITEMS.register("padded_chain_leggings", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
            ArmorItem.Type.LEGGINGS,new Item.Properties()));
    public static final RegistryObject<Item> PADDED_CHAIN_BOOTS = ITEMS.register("padded_chain_boots", () -> new LightArmorItem(GenesisArmorMaterials.PADDED_CHAIN,
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

    public static final RegistryObject<Item> PEWRIESE_HELMET = ITEMS.register("pewriese_helmet", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CHESTPLATE = ITEMS.register("pewriese_chestplate", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_LEGGINGS = ITEMS.register("pewriese_leggings", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_BOOTS = ITEMS.register("pewriese_boots", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
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




    //misc
    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
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



    public static final RegistryObject<Item> KEY_OF_OBLIVION = ITEMS.register("key_of_oblivion", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> MISY_CORE_1 = ITEMS.register("mist_core_1", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));


    public static final RegistryObject<Item> MEDALLION_OF_DOMINION = ITEMS.register("medallion_of_dominion",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> FABRICATED_STAR = ITEMS.register("fabricated_star", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));




    public static final RegistryObject<Item> HOT_SPRING_BUCKET =
            ITEMS.register("hot_spring_bucket",
                    () -> new BucketItem(GenesisFluids.HOT_SPRING, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> QUICKSAND_BUCKET =
            ITEMS.register("quicksand_bucket",
                    () -> new BucketItem(GenesisFluids.QUICKSAND, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> BLOOD_BUCKET =
            ITEMS.register("blood_bucket",
                    () -> new BucketItem(GenesisFluids.BLOOD,
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));


    //spawn egg
    public static final RegistryObject<Item> COLLECTOR_SPAWN_EGG = ITEMS.register("collector_spawn_egg", () -> new ForgeSpawnEggItem(GenesisEntities.COLLECTOR, 0xFFFFFF, 0xFFFFFF,
             new Item.Properties()));
    public static final RegistryObject<Item> COLLECTOR_GUARD_SPAWN_EGG = ITEMS.register("collector_guard_spawn_egg", () -> new ForgeSpawnEggItem(GenesisEntities.COLLECTOR_GUARD, 0xFFFFFF, 0xFFFFFF,
            new Item.Properties()));



}
