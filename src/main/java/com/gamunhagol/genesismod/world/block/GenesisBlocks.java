package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GenesisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GenesisMod.MODID);

    //natural acquisition
    public static final RegistryObject<Block> SILVER_ORE = register("silver_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3,3).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(3,3).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F,6.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final RegistryObject<Block> ISIS_ORE = register("isis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(60.0F,1200.0F).requiresCorrectToolForDrops(), UniformInt.of(3,7)));
    public static final RegistryObject<Block> TURBID_ISIS = register("turbid_isis",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(60.0F,1200.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MIXED_ISIS_BLOCK = register("mixed_isis_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(30.0F,1200.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final RegistryObject<Block> ISIS_ALLOY_BLOCK = register("isis_alloy_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(40.0F,1200.0F).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final RegistryObject<Block> ISIS_CRYSTAL_BLOCK = register("isis_crystal_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(90.0F,1200.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> BLUE_CRYSTAL_BLOCK = register("blue_crystal_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> BLUE_CRYSTAL_CLUSTER = register("blue_crystal_cluster",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
    public static final RegistryObject<Block> LARGER_BLUE_CRYSTAL_BUD = register("large_blue_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final RegistryObject<Block> MEDIUM_BLUE_CRYSTAL_BUD = register("medium_blue_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final RegistryObject<Block> SMALL_BLUE_CRYSTAL_BUD = register("small_blue_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));

    public static final RegistryObject<Block> CITRINE_BLOCK = register("citrine_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
   public static final RegistryObject<Block> CITRINE_CLUSTER = register("citrine_cluster",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
    public static final RegistryObject<Block> LARGER_CITRINE_BUD = register("large_citrine_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final RegistryObject<Block> MEDIUM_CITRINE_BUD = register("medium_citrine_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final RegistryObject<Block> SMALL_CITRINE_BUD = register("small_citrine_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));

    public static final RegistryObject<Block> RED_CRYSTAL_BLOCK = register("red_crystal_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> RED_CRYSTAL_CLUSTER = register("red_crystal_cluster",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)));
    public static final RegistryObject<Block> LARGER_RED_CRYSTAL_BUD = register("large_red_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD)));
    public static final RegistryObject<Block> MEDIUM_RED_CRYSTAL_BUD = register("medium_red_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD)));
    public static final RegistryObject<Block> SMALL_RED_CRYSTAL_BUD = register("small_red_crystal_bud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD)));

    //Get mob
    public static final RegistryObject<Block> CRYSTALLIZED_SANDSTONE = register("crystallized_sandstone",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(0.9F).sound(SoundType.GLASS)));


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return GenesisItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

}
