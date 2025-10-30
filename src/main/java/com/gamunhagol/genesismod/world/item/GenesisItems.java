package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisItemTier;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.fluid.FlowingSandFluid;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;
import net.minecraft.world.item.*;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.*;


public class GenesisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GenesisMod.MODID);

    //block
    public static final RegistryObject<BlockItem> PEWRIESE_ORE = ITEMS.register("pewriese_ore",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PEWRIESE_CRYSTAL_BLOCK = ITEMS.register("pewriese_crystal_block",
            () -> new BlockItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get(), new Item.Properties().fireResistant()));

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


    //item
    public static final RegistryObject<Item> BOOK_OF_CREATION = ITEMS.register("book_of_creation", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DREAM_POWDER = ITEMS.register("dream_powder", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_POWDER)));
    public static final RegistryObject<Item> DREAM_DANGO = ITEMS.register("dream_dango", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_DANGO)));
    public static final RegistryObject<Item> REMNANTS_OF_A_DREAM = ITEMS.register("remnants_of_a_dream", () -> new Item(new Item.Properties()
            .food(NightmareAggravated.REMNANTS_OF_A_DREAM)));
    public static final RegistryObject<Item> FRAGMENT_OF_MEMORY = ITEMS.register("fragment_of_memory", () -> new Item(new Item.Properties()
            .stacksTo(16).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> BLUE_CRYSTAL_SHARD = ITEMS.register("blue_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CITRINE_SHARD = ITEMS.register("citrine_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_CRYSTAL_SHARD = ITEMS.register("red_crystal_shard", () -> new FuelItem(new Item.Properties(), 20000));
    public static final RegistryObject<Item> WIND_STONE = ITEMS.register("wind_stone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTING_CRYSTAL_SHARD = ITEMS.register("lighting_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_AMBER = ITEMS.register("green_amber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROST_CRYSTAL_SHARD = ITEMS.register("frost_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FADED_CRYSTAL_SHARD = ITEMS.register("faded_crystal_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEWRIESE_ORE_PIECE = ITEMS.register("pewriese_ore_piece", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> PEWRIESE_PIECE = ITEMS.register("pewriese_piece", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CRYSTAL = ITEMS.register("pewriese_crystal", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PYULITELA = ITEMS.register("pyulitela", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> COPPER_COIN = ITEMS.register("copper_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_COIN = ITEMS.register("silver_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIN = ITEMS.register("gold_coin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLATINUM_COIN = ITEMS.register("platinum_coin", () -> new Item(new Item.Properties()));



//tool,armor

    public static final RegistryObject<Item> SPIRIT_COMPASS = ITEMS.register("spirit_compass", () -> new SpiritCompassItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE)));




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




    public static final RegistryObject<Item> PEWRIESE_HELMET = ITEMS.register("pewriese_helmet", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_CHESTPLATE = ITEMS.register("pewriese_chestplate", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_LEGGINGS = ITEMS.register("pewriese_leggings", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_BOOTS = ITEMS.register("pewriese_boots", () -> new ArmorItem(GenesisArmorMaterials.PEWRIESE,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PEWRIESE_PLATE_HELMET = ITEMS.register("pewriese_plate_helmet", () -> new Pewriese_Plate_Armor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_CHESTPLATE = ITEMS.register("pewriese_plate_chestplate", () -> new Pewriese_Plate_Armor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_LEGGINGS = ITEMS.register("pewriese_plate_leggings", () -> new Pewriese_Plate_Armor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> PEWRIESE_PLATE_BOOTS = ITEMS.register("pewriese_plate_boots", () -> new Pewriese_Plate_Armor(GenesisArmorMaterials.PEWRIESE_PLATE,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> HOLY_KNIGHT_HELMET = ITEMS.register("holy_knight_helmet", () -> new Holy_Knight_Armor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_CHESTPLATE = ITEMS.register("holy_knight_chestplate", () -> new Holy_Knight_Armor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_LEGGINGS = ITEMS.register("holy_knight_leggings", () -> new Holy_Knight_Armor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HOLY_KNIGHT_BOOTS = ITEMS.register("holy_knight_boots", () -> new Holy_Knight_Armor(GenesisArmorMaterials.HOLY_KNIGHT,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));




    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEWRIESE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("pewriese_upgrade_smithing_template", () -> new Item(new Item.Properties().fireResistant()));


    public static final RegistryObject<Item> HOT_SPRING_BUCKET =
            ITEMS.register("hot_spring_bucket",
                    () -> new BucketItem(GenesisFluids.HOT_SPRING,
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> FLOWING_SAND_BUCKET =
            ITEMS.register("flowing_sand_bucket",
                    () -> new BucketItem(GenesisFluids.FLOWING_SAND,
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
}
