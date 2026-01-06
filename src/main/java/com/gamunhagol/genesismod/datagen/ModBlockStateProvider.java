package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, GenesisMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(GenesisBlocks.SILVER_ORE);
        blockWithItem(GenesisBlocks.DEEPSLATE_SILVER_ORE);
        blockWithItem(GenesisBlocks.RAW_SILVER_BLOCK);
        blockWithItem(GenesisBlocks.SILVER_BLOCK);

        blockWithItem(GenesisBlocks.PEWRIESE_ORE);
        blockWithItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK);

        blockWithItem(GenesisBlocks.BLUE_CRYSTAL_BLOCK);
        blockWithItem(GenesisBlocks.CITRINE_BLOCK);
        blockWithItem(GenesisBlocks.RED_CRYSTAL_BLOCK);


        blockWithItem(GenesisBlocks.FADED_STONE);
        blockWithItem(GenesisBlocks.FADED_BRICK);
        blockWithItem(GenesisBlocks.FADED_GATEWAY);
        blockWithItem(GenesisBlocks.CHISELED_FADED_BRICK);




        stairsBlock(((StairBlock) GenesisBlocks.FADED_STONE_STAIRS.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        stairsBlock(((StairBlock) GenesisBlocks.FADED_BRICK_STAIRS.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        slabBlock(((SlabBlock) GenesisBlocks.FADED_STONE_SLAB.get()), blockTexture(GenesisBlocks.FADED_STONE.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        slabBlock(((SlabBlock) GenesisBlocks.FADED_BRICK_SLAB.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        wallBlock(((WallBlock) GenesisBlocks.FADED_BRICK_WALL.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        axisBlock(((RotatedPillarBlock) GenesisBlocks.FADED_PILLAR.get()), blockTexture(GenesisBlocks.FADED_PILLAR.get()),
                new ResourceLocation(GenesisMod.MODID, "block/faded_pillar_top"));


        clusterBlock(GenesisBlocks.BLUE_CRYSTAL_CLUSTER);
        clusterBlock(GenesisBlocks.CITRINE_CLUSTER);
        clusterBlock(GenesisBlocks.RED_CRYSTAL_CLUSTER);


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void clusterBlock(RegistryObject<Block> block) {
        String name = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();

        var model = models().cross(name, modLoc("block/" + name))
                .renderType("cutout");

        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);

            int xRot = switch (dir) {
                case DOWN -> 180;
                case UP -> 0;
                default -> 90;
            };

            int yRot = switch (dir) {
                case NORTH -> 0;
                case SOUTH -> 180;
                case WEST -> 270;
                case EAST -> 90;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });
    }

    private void clusterBlockSet(
            RegistryObject<Block> small,
            RegistryObject<Block> medium,
            RegistryObject<Block> large,
            RegistryObject<Block> cluster
    ) {
        clusterBlock(small);
        clusterBlock(medium);
        clusterBlock(large);
        clusterBlock(cluster);
    }
}
