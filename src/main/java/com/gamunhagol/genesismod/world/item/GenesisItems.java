package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.GenesisMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GenesisMod.MODID);

    //item
    public static final RegistryObject<Item> DREAM_POWDER = ITEMS.register("dream_powder", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_POWDER)));
    public static final RegistryObject<Item> DREAM_DANGO = ITEMS.register("dream_dango", () -> new Item(new Item.Properties()
            .food(NightmareRelief.DREAM_DANGO)));
    public static final RegistryObject<Item> REMNANTS_OF_A_DREAM = ITEMS.register("remnants_of_a_dream", () -> new Item(new Item.Properties()
            .food(NightmareAggravated.REMNANTS_OF_A_DREAM)));
    public static final RegistryObject<Item> FRAGMENT_OF_MEMORY = ITEMS.register("fragment_of_memory", () -> new Item(new Item.Properties()));

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

    public static final RegistryObject<Item> AMETHYST_NEEDLE = ITEMS.register("amethyst_needle", () -> new Item(new Item.Properties()));
}
