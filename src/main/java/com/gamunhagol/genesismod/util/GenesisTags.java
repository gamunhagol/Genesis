package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GenesisTags {
    public static class Blocks {

        private static TagKey<Block> ta(String name) {
            return BlockTags.create(new ResourceLocation(GenesisMod.MODID, name));
        }
    }

    public static class Items {
        private static TagKey<Item> ta(String name) {
            return ItemTags.create(new ResourceLocation(GenesisMod.MODID, name));

        }
    }
}
