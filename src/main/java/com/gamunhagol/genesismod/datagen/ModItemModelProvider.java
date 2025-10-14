package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GenesisMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(GenesisItems.DREAM_POWDER);
        simpleItem(GenesisItems.DREAM_DANGO);
        simpleItem(GenesisItems.REMNANTS_OF_A_DREAM);
        simpleItem(GenesisItems.FRAGMENT_OF_MEMORY);

        simpleItem(GenesisItems.BLUE_CRYSTAL_SHARD);
        simpleItem(GenesisItems.CITRINE_SHARD);
        simpleItem(GenesisItems.RED_CRYSTAL_SHARD);
        simpleItem(GenesisItems.FADED_CRYSTAL_SHARD);

        simpleItem(GenesisItems.PEWRIESE_ORE_PIECE);

        simpleItem(GenesisItems.PEWRIESE_PIECE);
        simpleItem(GenesisItems.PEWRIESE_CRYSTAL);

        simpleItem(GenesisItems.PYULITELA);

        simpleItem(GenesisItems.COPPER_COIN);
        simpleItem(GenesisItems.SILVER_COIN);
        simpleItem(GenesisItems.GOLD_COIN);
        simpleItem(GenesisItems.PLATINUM_COIN);

        simpleItem(GenesisItems.AMETHYST_NEEDLE);
        simpleItem(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE);

        //block

        wallItem(GenesisBlocks.FADED_BRICK_WALL, GenesisBlocks.FADED_BRICK);

        




    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(GenesisMod.MODID,"item/" + item.getId().getPath()));

    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(GenesisMod.MODID,"block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(GenesisMod.MODID,"block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("texture", new ResourceLocation(GenesisMod.MODID,"block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItme(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(GenesisMod.MODID, "item/" + item.getId().getPath()));
    }
}
