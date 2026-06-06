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


                output.accept(GenesisItems.LUMINOUS_INSECT_JUICE.get());

                output.accept(GenesisItems.SCORPION_MEAT.get());
                output.accept(GenesisItems.COOKED_SCORPION_MEAT.get());

                output.accept(GenesisItems.ENCHANTED_GLOWING_HEART.get());

                output.accept(GenesisItems.AMETHYST_APPLE.get());

                output.accept(GenesisItems.AMETHYST_APPLE_SLICES.get());
                output.accept(GenesisItems.AMETHYST_APPLE_PUDDING.get());

                output.accept(GenesisItems.OPAQUE_JELLY.get());

                output.accept(GenesisItems.SCATTERED_MEMORIES.get());
                output.accept(GenesisItems.DREAM_POWDER.get());
                output.accept(GenesisItems.DREAM_DANGO.get());
                output.accept(GenesisItems.REMNANTS_OF_A_DREAM.get());
                output.accept(GenesisItems.FRAGMENT_OF_MEMORY.get());

                output.accept(GenesisItems.AMETHYST_APPLE_PUDDING_BLOCK.get());

                output.accept(GenesisItems.BLOOD_BOTTLE.get());

                output.accept(GenesisItems.FADED_MEMORY.get());
                output.accept(GenesisItems.FORGOTTEN_MEMORY.get());
                output.accept(GenesisItems.UNRELATED_MEMORY.get());
                output.accept(GenesisItems.OBLIVION_SPHERE.get());




                output.accept(GenesisItems.DIVINE_GRAIL.get());

                output.accept(GenesisItems.SPIRIT_COMPASS.get());

                output.accept(GenesisItems.BLOODY_FLAG.get());

                output.accept(GenesisItems.HARDENED_GLASS_SWORD.get());
                output.accept(GenesisItems.HARDENED_GLASS_GREATSWORD.get());
                output.accept(GenesisItems.HARDENED_GLASS_SPEAR.get());
                output.accept(GenesisItems.HARDENED_GLASS_TACHI.get());
                output.accept(GenesisItems.HARDENED_GLASS_LONGSWORD.get());
                output.accept(GenesisItems.HARDENED_GLASS_DAGGER.get());

                output.accept(GenesisItems.HARDENED_RED_GLASS_SWORD.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_GREATSWORD.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_SPEAR.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_TACHI.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_LONGSWORD.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_DAGGER.get());

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

                output.accept(GenesisItems.PEWRIESE_GAUNTLET.get());

                output.accept(GenesisItems.HOLY_KNIGHT_SWORD.get());

                output.accept(GenesisItems.HOLY_KNIGHT_GREATSWORD.get());
                output.accept(GenesisItems.HOLY_KNIGHT_SPEAR.get());
                output.accept(GenesisItems.HOLY_KNIGHT_TACHI.get());
                output.accept(GenesisItems.HOLY_KNIGHT_LONGSWORD.get());
                output.accept(GenesisItems.HOLY_KNIGHT_DAGGER.get());

                output.accept(GenesisItems.CRYSTAL_GROWN_LONGSWORD.get());

                output.accept(GenesisItems.OATH_IN_DEEP_DARK.get());




                output.accept(GenesisItems.AMETHYST_WAND.get());

                output.accept(GenesisItems.GREEN_STAR_SEAL.get());
                output.accept(GenesisItems.HAND_HARBORING_OBLIVION.get());


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


                output.accept(GenesisItems.INTACT_AMETHYST_HEART.get());
                output.accept(GenesisItems.STAR_OF_DOMINATION.get());



                output.accept(GenesisItems.GREAT_BOW.get());

                output.accept(GenesisItems.LARGE_ARROW.get());






                output.accept(GenesisItems.MANA_IMBUED_AMETHYST_SHARD.get());
                output.accept(GenesisItems.STAR_FRAGMENT.get());

                output.accept(GenesisItems.AMETHYST_MAGIC_CORE.get());

                output.accept(GenesisItems.BLUE_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.CITRINE_SHARD.get());
                output.accept(GenesisItems.RED_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.WIND_STONE.get());
                output.accept(GenesisItems.LIGHTING_CRYSTAL_SHARD.get());
                output.accept(GenesisItems.GREEN_AMBER.get());
                output.accept(GenesisItems.ICE_FLOWER_SHARD.get());



                output.accept(GenesisItems.PEWRIESE_ORE_PIECE.get());


                output.accept(GenesisItems.PEWRIESE_PIECE.get());

                output.accept(GenesisItems.ELVENIA_PIECE.get());
                output.accept(GenesisItems.ELVENIA_INGOT.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_PIECE.get());
                output.accept(GenesisItems.ANCIENT_ELVENIA_INGOT.get());


                output.accept(GenesisItems.FUSION_ORE.get());

                output.accept(GenesisItems.GIANT_STONE_FRAGMENT.get());

                output.accept(GenesisItems.FUSION_STONE.get());

                output.accept(GenesisItems.WHITE_IRON_INGOT.get());
                output.accept(GenesisItems.BASED_SCULPTURE.get());


                output.accept(GenesisItems.PEWRIESE_CRYSTAL.get());

                output.accept(GenesisItems.PYULITELA.get());

                output.accept(GenesisItems.FLASK_SHARD.get());

                output.accept(GenesisItems.BEAST_REMAINS.get());

                output.accept(GenesisItems.COPPER_COIN.get());
                output.accept(GenesisItems.SILVER_COIN.get());
                output.accept(GenesisItems.GOLD_COIN.get());
                output.accept(GenesisItems.PLATINUM_COIN.get());

                output.accept(GenesisItems.AMETHYST_HUMAN_STATUE.get());

                output.accept(GenesisItems.LAGER_DESERT_SCORPION_TAIL.get());
                output.accept(GenesisItems.LAGER_DESERT_SCORPION_PINCERS.get());

                output.accept(GenesisItems.SCORPION_CARAPACE.get());

                output.accept(GenesisItems.SACRED_STONE.get());

                output.accept(GenesisItems.CLOTH.get());
                output.accept(GenesisItems.ENCHANTED_CLOTH.get());
                output.accept(GenesisItems.BLESSED_CLOTH.get());
                output.accept(GenesisItems.ILLUSION_SILK.get());

                output.accept(GenesisItems.METAL_FIBER.get());

                output.accept(GenesisItems.RED_EYE_SMALL.get());
                output.accept(GenesisItems.RED_EYE_MEDIUM.get());
                output.accept(GenesisItems.RED_EYE_LARGE.get());

                output.accept(GenesisItems.HARDENED_GLASS_PIECES.get());
                output.accept(GenesisItems.HARDENED_RED_GLASS_PIECES.get());
                output.accept(GenesisItems.HARDENED_RED_MASS.get());

                output.accept(GenesisItems.ECHOING_SOUL.get());
                output.accept(GenesisItems.SOUL_PUS.get());

                output.accept(GenesisItems.AMETHYST_NEEDLE.get());
                output.accept(GenesisItems.CRYSTAL_BAT_HIDE.get());

                output.accept(GenesisItems.AMETHYST_BONE.get());
                output.accept(GenesisItems.AMETHYST_HEART_PIECE.get());
                output.accept(GenesisItems.AMETHYST_SHIELD_SHARD.get());

                output.accept(GenesisItems.SMALL_BELL_OF_OBLIVION.get());

                output.accept(GenesisItems.FOG_GUARDIAN_SHARD.get());

                output.accept(GenesisItems.UNDERGROUND_BONE.get());

                output.accept(GenesisItems.UNDEAD_REMNANT.get());

                output.accept(GenesisItems.UNFINISHED_SHIELD.get());

                output.accept(GenesisItems.SKULLK_SPROUT.get());

                output.accept(GenesisItems.CRYSTALS_OF_THE_LAND.get());

                output.accept(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get());
                output.accept(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get());

                output.accept(GenesisItems.SCALE_FOSSIL_SHARD.get());
                output.accept(GenesisItems.SCALE_FOSSIL.get());
                output.accept(GenesisItems.SCALE_FOSSIL_CLUMP.get());
                output.accept(GenesisItems.WEATHERED_ANCIENT_DRAGON_ROCK.get());
                output.accept(GenesisItems.ANCIENT_DRAGON_ROCK.get());
                output.accept(GenesisItems.ANCIENT_DRAGON_SCALE.get());
                output.accept(GenesisItems.DRAGON_KING_SCALE.get());

                output.accept(GenesisItems.SHARD_OF_THE_MOUNTAIN.get());
                output.accept(GenesisItems.FRAGMENT_OF_THE_MOUNTAIN.get());
                output.accept(GenesisItems.CLUMP_OF_THE_MOUNTAIN.get());
                output.accept(GenesisItems.TABLET_SHARD.get());
                output.accept(GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN.get());

                output.accept(GenesisItems.BLADE_OF_DESTRUCTION_FRAGMENT.get());


                output.accept(GenesisItems.KEY_OF_OBLIVION.get());

                output.accept(GenesisItems.MISY_CORE_1.get());


                output.accept(GenesisItems.MEDALLION_OF_DOMINION.get());

                output.accept(GenesisItems.LITTLE_STAR_OF_CLARITY.get());

                output.accept(GenesisItems.FABRICATED_STAR.get());
                output.accept(GenesisItems.CELESTIAL_STAR.get());
                output.accept(GenesisItems.EPONYMOUS_STAR.get());

                output.accept(GenesisItems.QUICKSAND_BUCKET.get());
                output.accept(GenesisItems.BLOOD_BUCKET.get());

                output.accept(GenesisItems.HEAL_SCROLL_1.get());

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
                    if (item.get() == GenesisBlocks.QUICKSAND_BLOCK.get()
                            ||item.get() == GenesisBlocks.AMETHYST_APPLE_BLOCK.get() ||item.get() == GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get()
                            ||item.get() == GenesisBlocks.BLOOD_BLOCK.get()) {
                        return;
                    }
                    output.accept(item.get());
                });
            })
            .title(Component.translatable("itemGroup.genesis_block.blocks"))
            .build()
    );

    public static final RegistryObject<CreativeModeTab> GENESIS_SPELL_TAB = TABS.register("genesis_spell", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(GenesisItems.BASE_SPELL.get()))
            .withTabsBefore(GENESIS_BLOCK_TAB.getKey())
            .displayItems((params, output) -> {
                output.accept(GenesisItems.FIREBALL.get());

                output.accept(GenesisItems.LITTLE_HEAL.get());
            })

            .title(Component.translatable("itemGroup.genesis_spell.spell"))
            .build()
    );
}
