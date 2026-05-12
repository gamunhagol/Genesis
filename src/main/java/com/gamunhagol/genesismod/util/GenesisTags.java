package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GenesisTags {
    public static class Blocks {

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(GenesisMod.MODID, name));
        }
    }

    public static class Items {
        // [추가] 마법/기적 구분 태그
        public static final TagKey<Item> MAGIC_SPELLS = tag("magic_spells");
        public static final TagKey<Item> MIRACLE_SPELLS = tag("miracle_spells");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(GenesisMod.MODID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> FACTION_MOBS =
                TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(GenesisMod.MODID, "faction_mobs"));
    }
}
