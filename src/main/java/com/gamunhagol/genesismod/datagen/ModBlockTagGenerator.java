package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GenesisMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(GenesisBlocks.PEWRIESE_ORE.get()
                ,GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get()
                ,GenesisBlocks.RED_CRYSTAL_BLOCK.get()
                        ,GenesisBlocks.CITRINE_BLOCK.get()
                        ,GenesisBlocks.BLUE_CRYSTAL_BLOCK.get()
                        ,GenesisBlocks.RED_CRYSTAL_CLUSTER.get()
                        ,GenesisBlocks.CITRINE_CLUSTER.get()
                        ,GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(GenesisBlocks.PEWRIESE_ORE.get()
                ,GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get());

        this.tag(BlockTags.WALLS)
                .add(GenesisBlocks.FADED_BRICK_WALL.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(GenesisBlocks.SAND_TRAP.get());

        this.tag(BlockTags.IMPERMEABLE).remove(GenesisBlocks.SAND_TRAP.get());
    }
}
