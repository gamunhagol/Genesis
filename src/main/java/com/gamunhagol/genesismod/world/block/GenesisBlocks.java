package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.main.GenesisMod;


import com.gamunhagol.genesismod.world.block.entity.GenesisBlockEntities;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
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
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(SILVER_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> RAW_SILVER_BLOCK = register("raw_silver_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> ELVENIA_BLOCK = register("elvenia_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(4.5F, 8.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> ANCIENT_ELVENIA_BLOCK = register("ancient_elvenia_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(6.0F, 8.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PEWRIESE_ORE = register("pewriese_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(60.0F, 1200.0F).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> PEWRIESE_CRYSTAL_BLOCK = register("pewriese_crystal_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(180.0F, 1200.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> PYULITELA_BLOCK = register("pyulitela_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(360.0F, 2400.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> AMETHYST_APPLE_BLOCK = register("amethyst_apple_block",
            () -> new AmethystAppleBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).noOcclusion().lightLevel(state -> 5)));


    public static final RegistryObject<Block> BLUE_CRYSTAL_BLOCK = register("blue_crystal_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> BLUE_CRYSTAL_CLUSTER = register("blue_crystal_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> CITRINE_BLOCK = register("citrine_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> CITRINE_CLUSTER = register("citrine_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> RED_CRYSTAL_BLOCK = register("red_crystal_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> RED_CRYSTAL_CLUSTER = register("red_crystal_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> GREEN_AMBER_BLOCK = register("green_amber_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> GREEN_AMBER_CLUSTER = register("green_amber_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> ICE_FLOWER_BLOCK = register("ice_flower_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> ICE_FLOWER_CLUSTER = register("ice_flower_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> LIGHTING_CRYSTAL_BLOCK = register("lighting_crystal_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> LIGHTING_CRYSTAL_CLUSTER = register("lighting_crystal_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> WIND_STONE_BLOCK = register("wind_stone_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> WIND_STONE_CLUSTER = register("wind_stone_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion()
                    .lightLevel(state -> 5).pushReaction(PushReaction.DESTROY)));


    public static final RegistryObject<Block> OBLIVION_CANDLE = register("oblivion_candle",
            () -> new OblivionCandleBlock(BlockBehaviour.Properties.copy(Blocks.CANDLE).noOcclusion()));

    //liquid
    public static final RegistryObject<LiquidBlock> HOT_SPRING_BLOCK =
            BLOCKS.register("hot_spring_block",
                    () -> new HotSpringFluidBlock(GenesisFluids.HOT_SPRING,
                            BlockBehaviour.Properties.copy(Blocks.WATER)
                                    .noLootTable().noCollission().liquid()));

    public static final RegistryObject<LiquidBlock> QUICKSAND_BLOCK =
            BLOCKS.register("quicksand_block",
                    () -> new QuicksandFluidBlock(GenesisFluids.QUICKSAND,
                            BlockBehaviour.Properties.copy(Blocks.WATER)
                                    .noLootTable().noCollission().liquid()));






    //crafting
    public static final RegistryObject<Block> AMETHYST_APPLE_PUDDING_BLOCK = register("amethyst_apple_pudding_block",
            () -> new AmethystApplePuddingBlock(BlockBehaviour.Properties.copy(Blocks.MAGENTA_WOOL).sound(SoundType.SLIME_BLOCK).noOcclusion().lightLevel(state -> 5)));


    public static final RegistryObject<Block> COPPER_COIN_PILE = register("copper_coin_pile", () -> new CoinPileBlock
            (BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2f).sound(SoundType.CHAIN).noOcclusion()));
    public static final RegistryObject<Block> SILVER_COIN_PILE = register("silver_coin_pile", () -> new CoinPileBlock
            (BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2f).sound(SoundType.CHAIN).noOcclusion()));
    public static final RegistryObject<Block> GOLD_COIN_PILE = register("gold_coin_pile", () -> new CoinPileBlock
            (BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2f).sound(SoundType.CHAIN).noOcclusion()));
    public static final RegistryObject<Block> PLATINUM_COIN_PILE = register("platinum_coin_pile", () -> new CoinPileBlock
            (BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(0.2f).sound(SoundType.CHAIN).noOcclusion()));


    //Get mob


    //Unobtainable
    public static final RegistryObject<Block> FADED_STONE = register("faded_stone",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_BRICK = register("faded_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> CHISELED_FADED_BRICK = register("chiseled_faded_brick",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_PILLAR = register("faded_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_GATEWAY = register("faded_gateway",
            () -> new FadedGatewayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable().lightLevel(state -> 15)));

    public static final RegistryObject<Block> FADED_STONE_STAIRS = register("faded_stone_stairs",
            () -> new StairBlock(() -> GenesisBlocks.FADED_STONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_BRICK_STAIRS = register("faded_brick_stairs",
            () -> new StairBlock(() -> GenesisBlocks.FADED_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_STONE_SLAB = register("faded_stone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_BRICK_SLAB = register("faded_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));
    public static final RegistryObject<Block> FADED_BRICK_WALL = register("faded_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));


    public static final RegistryObject<Block> FADED_CHEST = register("faded_chest",
            () -> new FadedChestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable().lightLevel(state -> 6)));


    public static final RegistryObject<Block> STATUE_OF_SENTINEL_OF_OBLIVION = register("statue_of_sentinel_of_oblivion",
            () -> new StatueBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW)
                    .noLootTable().noOcclusion(), GenesisBlockEntities.SENTINEL_STATUE_BE));
    public static final RegistryObject<Block> STATUE_OF_HERALD_OF_OBLIVION = register("statue_of_herald_of_oblivion",
            () -> new StatueBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW)
                    .noLootTable().noOcclusion(), GenesisBlockEntities.HERALD_STATUE_BE));
    public static final RegistryObject<Block> STATUE_OF_GUIDE_TO_OBLIVION = register("statue_of_guide_to_oblivion",
            () -> new StatueBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW)
                    .noLootTable().noOcclusion(), GenesisBlockEntities.GUIDE_STATUE_BE));

    public static final RegistryObject<Block> AEK_STATUE = register("ancient_elf_knight_statue",
            () -> new AEKStatueBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(30.0F, 1200.0F).sound(SoundType.METAL).lightLevel(state -> 3)
                    .noOcclusion()));


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        return toReturn;
    }



}
