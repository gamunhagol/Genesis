package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class GenesisCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GenesisMod.MODID);

    public static final RegistryObject<CreativeModeTab> GENESIS_ITEMS_TAB = TABS.register("genesis", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(GenesisItems.FRAGMENT_OF_MEMORY.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((params, output) -> {
                output.accept(GenesisItems.DREAM_POWDER.get());
                output.accept(GenesisItems.DREAM_DANGO.get());
                output.accept(GenesisItems.REMNANTS_OF_A_DREAM.get());
                output.accept(GenesisItems.FRAGMENT_OF_MEMORY.get());

                output.accept(GenesisItems.BLUE_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.CITRINE_SHARD.get());
                output.accept(GenesisItems.RED_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.FADED_CRYSTAL_SHARD.get());

                output.accept(GenesisItems.RAW_SILVER.get());
                output.accept(GenesisItems.SILVER_INGOT.get());

                output.accept(GenesisItems.ISIS_ORE_PIECE.get());
                output.accept(GenesisItems.MIXED_ISIS_INGOT.get());
                output.accept(GenesisItems.ISIS_PIECE.get());
                output.accept(GenesisItems.ISIS_ALLOY.get());
                output.accept(GenesisItems.ISIS_FRAGMENT.get());
                output.accept(GenesisItems.ISIS_CRYSTAL.get());

                output.accept(GenesisItems.PURE_ISIS.get());

                output.accept(GenesisItems.ISIS_SWORD.get());
                output.accept(GenesisItems.ISIS_SHOVEL.get());
                output.accept(GenesisItems.ISIS_PICKAXE.get());
                output.accept(GenesisItems.ISIS_AXE.get());
                output.accept(GenesisItems.ISIS_HOE.get());

                output.accept(GenesisItems.ISIS_HELMET.get());
                output.accept(GenesisItems.ISIS_CHESTPLATE.get());
                output.accept(GenesisItems.ISIS_LEGGINGS.get());
                output.accept(GenesisItems.ISIS_BOOTS.get());

                output.accept(GenesisItems.ISIS_PLATE_HELMET.get());
                output.accept(GenesisItems.ISIS_PLATE_CHESTPLATE.get());
                output.accept(GenesisItems.ISIS_PLATE_LEGGINGS.get());
                output.accept(GenesisItems.ISIS_PLATE_BOOTS.get());

                output.accept(GenesisItems.ISIS_HOLY_KNIGHT_HELMET.get());
                output.accept(GenesisItems.ISIS_HOLY_KNIGHT_CHESTPLATE.get());
                output.accept(GenesisItems.ISIS_HOLY_KNIGHT_LEGGINGS.get());
                output.accept(GenesisItems.ISIS_HOLY_KNIGHT_BOOTS.get());



                output.accept(GenesisItems.AMETHYST_NEEDLE.get());
                output.accept(GenesisItems.ISIS_UPGRADE_SMITHING_TEMPLATE.get());
            })
            .title(Component.translatable("itemGroup.genesis.items"))
            .build()
    );

    public static final RegistryObject<CreativeModeTab> GENESIS_BLOCK_TAB = TABS.register("genesis_block", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(GenesisBlocks.MIXED_ISIS_BLOCK.get()))
            .withTabsBefore(GENESIS_ITEMS_TAB.getKey())
            .displayItems((params, output) -> {
                GenesisBlocks.BLOCKS.getEntries().forEach(item -> {
                    output.accept(item.get());
                });
            })
            .title(Component.translatable("itemGroup.genesis_block.blocks"))
            .build()


    );
}
