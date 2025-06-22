package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.util.GenesisItemTier;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.item.Items.registerBlock;


public class GenesisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GenesisMod.MODID);

    //item
    public static final RegistryObject<Item> DREAM_POWDER = ITEMS.register("dream_powder", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_POWDER)));
    public static final RegistryObject<Item> DREAM_DANGO = ITEMS.register("dream_dango", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_DANGO)));
    public static final RegistryObject<Item> REMNANTS_OF_A_DREAM = ITEMS.register("remnants_of_a_dream", () -> new Item(new Item.Properties()
            .food(NightmareAggravated.REMNANTS_OF_A_DREAM)));
    public static final RegistryObject<Item> FRAGMENT_OF_MEMORY = ITEMS.register("fragment_of_memory", () -> new Item(new Item.Properties()
            .stacksTo(16)));

    public static final RegistryObject<Item> BLUE_CRYSTAL_SHARD = ITEMS.register("blue_crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CITRINE_SHARD = ITEMS.register("citrine_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RED_CRYSTAL_SHARD = ITEMS.register("red_crystal_shard", () -> new FuelItem(new Item.Properties(), 20000));
    public static final RegistryObject<Item> FADED_CRYSTAL_SHARD = ITEMS.register("faded_crystal_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ISIS_ORE_PIECE = ITEMS.register("isis_ore_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIXED_ISIS_INGOT = ITEMS.register("mixed_isis_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_PIECE = ITEMS.register("isis_piece", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_ALLOY = ITEMS.register("isis_alloy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_FRAGMENT = ITEMS.register("isis_fragment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ISIS_CRYSTAL = ITEMS.register("isis_crystal", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ISIS_SWORD = ITEMS.register("isis_sword", () -> new SwordItem(GenesisItemTier.ISIS,3,-2.4f,
            new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ISIS_SHOVEL = ITEMS.register("isis_shovel", () -> new ShovelItem(GenesisItemTier.ISIS,1.5f,-3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ISIS_PICKAXE = ITEMS.register("isis_pickaxe", () -> new PickaxeItem(GenesisItemTier.ISIS,1,-2.8f,
            new Item.Properties()));
    public static final RegistryObject<Item> ISIS_AXE = ITEMS.register("isis_axe", () -> new AxeItem(GenesisItemTier.ISIS,5,-3.0f,
            new Item.Properties()));
    public static final RegistryObject<Item> ISIS_HOE = ITEMS.register("isis_hoe", () -> new HoeItem(GenesisItemTier.ISIS,-4,0.0f,
            new Item.Properties()));


    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
}
