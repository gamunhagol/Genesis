package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisItemTier;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.world.item.*;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class GenesisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GenesisMod.MODID);

    //block
    public static final RegistryObject<BlockItem> SILVER_ORE = ITEMS.register("silver_ore",
            () -> new BlockItem(GenesisBlocks.SILVER_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore",
            () -> new BlockItem(GenesisBlocks.DEEPSLATE_SILVER_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SILVER_BLOCK = ITEMS.register("silver_block",
            () -> new BlockItem(GenesisBlocks.SILVER_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> ISIS_ORE = ITEMS.register("isis_ore",
            () -> new BlockItem(GenesisBlocks.ISIS_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> TURBID_ISIS = ITEMS.register("turbid_isis",
            () -> new BlockItem(GenesisBlocks.TURBID_ISIS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MIXED_ISIS_BLOCK = ITEMS.register("mixed_isis_block",
            () -> new BlockItem(GenesisBlocks.MIXED_ISIS_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ISIS_ALLOY_BLOCK = ITEMS.register("isis_alloy_block",
            () -> new BlockItem(GenesisBlocks.ISIS_ALLOY_BLOCK.get(), new Item.Properties().fireResistant()));
    public static final RegistryObject<BlockItem> ISIS_CRYSTAL_BLOCK = ITEMS.register("isis_crystal_block",
            () -> new BlockItem(GenesisBlocks.ISIS_CRYSTAL_BLOCK.get(), new Item.Properties().fireResistant()));

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

    public static final RegistryObject<BlockItem> CRYSTALLIZED_SANDSTONE = ITEMS.register("crystallized_sandstone",
            () -> new BlockItem(GenesisBlocks.CRYSTALLIZED_SANDSTONE.get(), new Item.Properties()));

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
    public static final RegistryObject<Item> FADED_CRYSTAL_SHARD = ITEMS.register("faded_crystal_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ISIS_ORE_PIECE = ITEMS.register("isis_ore_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIXED_ISIS_INGOT = ITEMS.register("mixed_isis_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_PIECE = ITEMS.register("isis_piece", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ISIS_ALLOY = ITEMS.register("isis_alloy", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ISIS_FRAGMENT = ITEMS.register("isis_fragment", () -> new Item(new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ISIS_CRYSTAL = ITEMS.register("isis_crystal", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> ISIS_HELMET = ITEMS.register("isis_helmet", () -> new ArmorItem(GenesisArmorMaterials.ISIS,
            ArmorItem.Type.HELMET,new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_CHESTPLATE = ITEMS.register("isis_chestplate", () -> new ArmorItem(GenesisArmorMaterials.ISIS,
            ArmorItem.Type.CHESTPLATE,new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_LEGGINGS = ITEMS.register("isis_leggings", () -> new ArmorItem(GenesisArmorMaterials.ISIS,
            ArmorItem.Type.LEGGINGS,new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_BOOTS = ITEMS.register("isis_boots", () -> new ArmorItem(GenesisArmorMaterials.ISIS,
            ArmorItem.Type.BOOTS,new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> ISIS_SWORD = ITEMS.register("isis_sword", () -> new SwordItem(GenesisItemTier.ISIS, 3, -2.4f,
            new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_SHOVEL = ITEMS.register("isis_shovel", () -> new ShovelItem(GenesisItemTier.ISIS, 1.5f, -3.0f,
            new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_PICKAXE = ITEMS.register("isis_pickaxe", () -> new PickaxeItem(GenesisItemTier.ISIS, 1, -2.8f,
            new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_AXE = ITEMS.register("isis_axe", () -> new AxeItem(GenesisItemTier.ISIS, 5, -3.0f,
            new Item.Properties().fireResistant().durability(-1)));
    public static final RegistryObject<Item> ISIS_HOE = ITEMS.register("isis_hoe", () -> new HoeItem(GenesisItemTier.ISIS, -4, 0.0f,
            new Item.Properties().fireResistant().durability(-1)));


    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("isis_upgrade_smithing_template", () -> new Item(new Item.Properties().fireResistant()));
}
