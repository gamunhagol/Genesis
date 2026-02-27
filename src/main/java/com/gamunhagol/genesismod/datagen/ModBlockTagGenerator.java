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
                ,GenesisBlocks.PYULITELA_ORE.get()
                ,GenesisBlocks.PYULITELA_BLOCK.get()
                        ,GenesisBlocks.SILVER_ORE.get()
                        ,GenesisBlocks.DEEPSLATE_SILVER_ORE.get()
                        ,GenesisBlocks.RAW_SILVER_BLOCK.get()
                        ,GenesisBlocks.SILVER_BLOCK.get()
                        ,GenesisBlocks.ELVENIA_BLOCK.get()
                        ,GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get()
                        ,GenesisBlocks.AMETHYST_APPLE_BLOCK.get()
                        ,GenesisBlocks.GIANT_STONE.get()
                ,GenesisBlocks.RED_CRYSTAL_BLOCK.get()
                        ,GenesisBlocks.CITRINE_BLOCK.get()
                        ,GenesisBlocks.BLUE_CRYSTAL_BLOCK.get()
                        ,GenesisBlocks.GREEN_AMBER_BLOCK.get()
                        ,GenesisBlocks.ICE_FLOWER_BLOCK.get()
                        ,GenesisBlocks.LIGHTING_CRYSTAL_BLOCK.get()
                        ,GenesisBlocks.WIND_STONE_BLOCK.get()
                        ,GenesisBlocks.RED_CRYSTAL_CLUSTER.get()
                        ,GenesisBlocks.CITRINE_CLUSTER.get()
                        ,GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get()
                        ,GenesisBlocks.GREEN_AMBER_CLUSTER.get()
                        ,GenesisBlocks.ICE_FLOWER_CLUSTER.get()
                        ,GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER.get()
                        ,GenesisBlocks.WIND_STONE_CLUSTER.get()
                        ,GenesisBlocks.WEATHERED_ANCIENT_DRAGON_ROCK.get()
                        ,GenesisBlocks.ANCIENT_DRAGON_ROCK.get()
                        ,GenesisBlocks.AMETHYST_SAPLING.get()
                        ,GenesisBlocks.AEK_STATUE.get()
                    ,GenesisBlocks.COPPER_COIN_PILE.get()
                    ,GenesisBlocks.SILVER_COIN_PILE.get()
                    ,GenesisBlocks.GOLD_COIN_PILE.get()
                    ,GenesisBlocks.PLATINUM_COIN_PILE.get());


        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(GenesisBlocks.PEWRIESE_ORE.get()
                ,GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get()
                ,GenesisBlocks.PYULITELA_ORE.get()
                ,GenesisBlocks.PYULITELA_BLOCK.get()
                        ,GenesisBlocks.AEK_STATUE.get()
                        ,GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get()
                        ,GenesisBlocks.WEATHERED_ANCIENT_DRAGON_ROCK.get()
                        ,GenesisBlocks.ANCIENT_DRAGON_ROCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(GenesisBlocks.SILVER_ORE.get()
                        ,GenesisBlocks.DEEPSLATE_SILVER_ORE.get()
                        ,GenesisBlocks.RAW_SILVER_BLOCK.get()
                        ,GenesisBlocks.SILVER_BLOCK.get()
                        ,GenesisBlocks.ELVENIA_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(GenesisBlocks.GIANT_STONE.get());

        this.tag(BlockTags.WALLS)
                .add(GenesisBlocks.FADED_BRICK_WALL.get());

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL);

        this.tag(BlockTags.MINEABLE_WITH_AXE);


    }
}
