package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.main.GenesisMod;


import com.gamunhagol.genesismod.world.fluid.GenesisFluids;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistries.Keys.FLUID_TYPES;


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

    public static final RegistryObject<Block> PEWRIESE_ORE = register("pewriese_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(60.0F, 1200.0F).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> PEWRIESE_CRYSTAL_BLOCK = register("pewriese_crystal_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(180.0F, 1200.0F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> AMETHYST_APPLE_BLOCK = register("amethyst_apple_block",
            () -> new AmethystAppleBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).noOcclusion().lightLevel((p_152632_) -> {
                return 5;})));


    public static final RegistryObject<Block> BLUE_CRYSTAL_BLOCK = register("blue_crystal_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> BLUE_CRYSTAL_CLUSTER = register("blue_crystal_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion().lightLevel((p_152632_) -> {
                return 5;
            }).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> CITRINE_BLOCK = register("citrine_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> CITRINE_CLUSTER = register("citrine_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion().lightLevel((p_152632_) -> {
                return 5;
            }).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> RED_CRYSTAL_BLOCK = register("red_crystal_block",
            () -> new AmethystBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)));
    public static final RegistryObject<Block> RED_CRYSTAL_CLUSTER = register("red_crystal_cluster",
            () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).sound(SoundType.AMETHYST_CLUSTER).forceSolidOn().noOcclusion().lightLevel((p_152632_) -> {
                return 5;
            }).pushReaction(PushReaction.DESTROY)));



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
            () -> new AmethystApplePuddingBlock(BlockBehaviour.Properties.copy(Blocks.MAGENTA_WOOL).sound(SoundType.SLIME_BLOCK).noOcclusion().lightLevel((p_152632_) -> {
                return 5;})));


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
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(-1.0F, 3600000.0F).sound(SoundType.POWDER_SNOW).noLootTable()));

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


    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        return toReturn;
    }



}
