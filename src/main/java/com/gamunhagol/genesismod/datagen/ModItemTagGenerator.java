package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public static final TagKey<Item> SPIRIT_STONES = TagKey.create(
            Registries.ITEM, new ResourceLocation(GenesisMod.MODID, "spirit_stone")
    );


    public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, GenesisMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        tag(SPIRIT_STONES).add(
                GenesisItems.RED_CRYSTAL_SHARD.get(),
                GenesisItems.CITRINE_SHARD.get(),
                GenesisItems.BLUE_CRYSTAL_SHARD.get(),
                GenesisItems.WIND_STONE.get(),
                GenesisItems.LIGHTING_CRYSTAL_SHARD.get(),
                GenesisItems.GREEN_AMBER.get(),
                GenesisItems.FROST_CRYSTAL_SHARD.get()
        );

        tag(net.minecraft.tags.ItemTags.TRIMMABLE_ARMOR).add(
                GenesisItems.PADDED_CHAIN_HELMET.get(),
                GenesisItems.PADDED_CHAIN_CHESTPLATE.get(),
                GenesisItems.PADDED_CHAIN_LEGGINGS.get(),
                GenesisItems.PADDED_CHAIN_BOOTS.get()
        );



    }

    @Override
    public String getName() {
        return "GenesisMod Item Tags";
    }
}
