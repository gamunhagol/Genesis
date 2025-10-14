package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, GenesisMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWhitItem(GenesisBlocks.PEWRIESE_ORE);
        blockWhitItem(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK);

        blockWhitItem(GenesisBlocks.BLUE_CRYSTAL_BLOCK);
        blockWhitItem(GenesisBlocks.CITRINE_BLOCK);
        blockWhitItem(GenesisBlocks.RED_CRYSTAL_BLOCK);


        blockWhitItem(GenesisBlocks.FADED_STONE);
        blockWhitItem(GenesisBlocks.FADED_BRICK);
        blockWhitItem(GenesisBlocks.FADED_GATEWAY);
        blockWhitItem(GenesisBlocks.CHISELED_FADED_BRICK);

        stairsBlock(((StairBlock) GenesisBlocks.FADED_STONE_STAIRS.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        stairsBlock(((StairBlock) GenesisBlocks.FADED_BRICK_STAIRS.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        slabBlock(((SlabBlock) GenesisBlocks.FADED_STONE_SLAB.get()), blockTexture(GenesisBlocks.FADED_STONE.get()), blockTexture(GenesisBlocks.FADED_STONE.get()));
        slabBlock(((SlabBlock) GenesisBlocks.FADED_BRICK_SLAB.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        wallBlock(((WallBlock) GenesisBlocks.FADED_BRICK_WALL.get()), blockTexture(GenesisBlocks.FADED_BRICK.get()));

        axisBlock(((RotatedPillarBlock) GenesisBlocks.FADED_PILLAR.get()), blockTexture(GenesisBlocks.FADED_PILLAR.get()),
                new ResourceLocation(GenesisMod.MODID, "block/faded_pillar_top"));


    }

    private void blockWhitItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
