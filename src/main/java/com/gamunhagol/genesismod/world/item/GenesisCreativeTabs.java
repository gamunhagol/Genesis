package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.main.GenesisMod;
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
            .icon(() -> new ItemStack(GenesisItems.BOOK_OF_CREATION.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((params, output) -> {
                output.accept(GenesisItems.BOOK_OF_CREATION.get());

                output.accept(GenesisItems.SCATTERED_MEMORIES.get());
                output.accept(GenesisItems.DREAM_POWDER.get());
                output.accept(GenesisItems.DREAM_DANGO.get());
                output.accept(GenesisItems.REMNANTS_OF_A_DREAM.get());
                output.accept(GenesisItems.FRAGMENT_OF_MEMORY.get());

                output.accept(GenesisItems.MANA_IMBUED_AMETHYST_SHARD.get());

                output.accept(GenesisItems.BLUE_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.CITRINE_SHARD.get());
                output.accept(GenesisItems.RED_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.WIND_STONE.get());
                output.accept(GenesisItems.LIGHTING_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.GREEN_AMBER.get());
                output.accept(GenesisItems.ICE_FLOWER_SHARD.get());

                output.accept(GenesisItems.RAW_SILVER.get());


                output.accept(GenesisItems.PEWRIESE_ORE_PIECE.get());

                output.accept(GenesisItems.SILVER_PIECE.get());
                output.accept(GenesisItems.SILVER_INGOT.get());
                output.accept(GenesisItems.PEWRIESE_PIECE.get());

                output.accept(GenesisItems.ELVENIA_PIECE.get());
                output.accept(GenesisItems.ELVENIA_INGOT.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_PIECE.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_INGOT.get());



                output.accept(GenesisItems.PEWRIESE_CRYSTAL.get());

                output.accept(GenesisItems.PYULITELA.get());

                output.accept(GenesisItems.FLASK_SHARD.get());

                output.accept(GenesisItems.BEAST_REMAINS.get());

                output.accept(GenesisItems.COPPER_COIN.get());
                output.accept(GenesisItems.SILVER_COIN.get());
                output.accept(GenesisItems.GOLD_COIN.get());
                output.accept(GenesisItems.PLATINUM_COIN.get());



                output.accept(GenesisItems.AMETHYST_APPLE.get());

                output.accept(GenesisItems.AMETHYST_APPLE_SLICES.get());
                output.accept(GenesisItems.AMETHYST_APPLE_PUDDING.get());

                output.accept(GenesisItems.AMETHYST_APPLE_PUDDING_BLOCK.get());




                output.accept(GenesisItems.DIVINE_GRAIL.get());

                output.accept(GenesisItems.SPIRIT_COMPASS.get());

                output.accept(GenesisItems.ELVENIA_SWORD.get());
                output.accept(GenesisItems.ELVENIA_SHOVEL.get());
                output.accept(GenesisItems.ELVENIA_PICKAXE.get());
                output.accept(GenesisItems.ELVENIA_AXE.get());
                output.accept(GenesisItems.ELVENIA_HOE.get());

                output.accept(GenesisItems.ELVENIA_GREATSWORD.get());
                output.accept(GenesisItems.ELVENIA_SPEAR.get());
                output.accept(GenesisItems.ELVENIA_TACHI.get());
                output.accept(GenesisItems.ELVENIA_LONGSWORD.get());
                output.accept(GenesisItems.ELVENIA_DAGGER.get());

                output.accept(GenesisItems.ANCIENT_ELVENIA_SWORD.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_SHOVEL.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_PICKAXE.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_AXE.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_HOE.get());

                output.accept(GenesisItems.ANCIENT_ELVENIA_GREATSWORD.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_SPEAR.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_TACHI.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_LONGSWORD.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_DAGGER.get());


                output.accept(GenesisItems.PEWRIESE_SWORD.get());
                output.accept(GenesisItems.PEWRIESE_SHOVEL.get());
                output.accept(GenesisItems.PEWRIESE_PICKAXE.get());
                output.accept(GenesisItems.PEWRIESE_AXE.get());
                output.accept(GenesisItems.PEWRIESE_HOE.get());

                output.accept(GenesisItems.PEWRIESE_GREATSWORD.get());
                output.accept(GenesisItems.PEWRIESE_SPEAR.get());
                output.accept(GenesisItems.PEWRIESE_TACHI.get());
                output.accept(GenesisItems.PEWRIESE_LONGSWORD.get());
                output.accept(GenesisItems.PEWRIESE_DAGGER.get());


                output.accept(GenesisItems.PADDED_CHAIN_HELMET.get());
                output.accept(GenesisItems.PADDED_CHAIN_CHESTPLATE.get());
                output.accept(GenesisItems.PADDED_CHAIN_LEGGINGS.get());
                output.accept(GenesisItems.PADDED_CHAIN_BOOTS.get());

                output.accept(GenesisItems.ELVENIA_HELMET.get());
                output.accept(GenesisItems.ELVENIA_CHESTPLATE.get());
                output.accept(GenesisItems.ELVENIA_LEGGINGS.get());
                output.accept(GenesisItems.ELVENIA_BOOTS.get());

                output.accept(GenesisItems.ANCIENT_ELVENIA_HELMET.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_CHESTPLATE.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_LEGGINGS.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_BOOTS.get());

                output.accept(GenesisItems.PEWRIESE_HELMET.get());
                output.accept(GenesisItems.PEWRIESE_CHESTPLATE.get());
                output.accept(GenesisItems.PEWRIESE_LEGGINGS.get());
                output.accept(GenesisItems.PEWRIESE_BOOTS.get());

                output.accept(GenesisItems.PEWRIESE_PLATE_HELMET.get());
                output.accept(GenesisItems.PEWRIESE_PLATE_CHESTPLATE.get());
                output.accept(GenesisItems.PEWRIESE_PLATE_LEGGINGS.get());
                output.accept(GenesisItems.PEWRIESE_PLATE_BOOTS.get());

                output.accept(GenesisItems.HOLY_KNIGHT_HELMET.get());
                output.accept(GenesisItems.HOLY_KNIGHT_CHESTPLATE.get());
                output.accept(GenesisItems.HOLY_KNIGHT_LEGGINGS.get());
                output.accept(GenesisItems.HOLY_KNIGHT_BOOTS.get());
                


                output.accept(GenesisItems.AMETHYST_NEEDLE.get());
                output.accept(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get());
                output.accept(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get());

                output.accept(GenesisItems.MEDALLION_OF_DOMINION.get());

                output.accept(GenesisItems.FABRICATED_STAR.get());

                output.accept(GenesisItems.HOT_SPRING_BUCKET.get());
                output.accept(GenesisItems.QUICKSAND_BUCKET.get());

                output.accept(GenesisItems.COLLECTOR_SPAWN_EGG.get());
                output.accept(GenesisItems.COLLECTOR_GUARD_SPAWN_EGG.get());

            })
            .title(Component.translatable("itemGroup.genesis.items"))
            .build()
    );

    public static final RegistryObject<CreativeModeTab> GENESIS_BLOCK_TAB = TABS.register("genesis_block", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get()))
            .withTabsBefore(GENESIS_ITEMS_TAB.getKey())
            .displayItems((params, output) -> {
                GenesisBlocks.BLOCKS.getEntries().forEach(item -> {
                    if (item.get() == GenesisBlocks.HOT_SPRING_BLOCK.get() ||item.get() == GenesisBlocks.QUICKSAND_BLOCK.get()
                            ||item.get() == GenesisBlocks.AMETHYST_APPLE_BLOCK.get() ||item.get() == GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get()) {
                        return;
                    }
                    output.accept(item.get());
                });
            })
            .title(Component.translatable("itemGroup.genesis_block.blocks"))
            .build()


    );
}
