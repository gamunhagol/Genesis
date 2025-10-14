package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
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



    }

    private void blockWhitItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
