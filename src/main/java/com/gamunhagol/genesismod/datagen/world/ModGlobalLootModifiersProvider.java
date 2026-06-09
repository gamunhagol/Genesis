package com.gamunhagol.genesismod.datagen.world;

import com.gamunhagol.genesismod.data.loot.AddChestItemModifier;
import com.gamunhagol.genesismod.data.loot.AddSusSandItemModifier;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, GenesisMod.MODID);
    }

    @Override
    protected void start() {
        // sussnad
        Map<ResourceLocation, List<AddSusSandItemModifier.Pair>> susSandEntries = new HashMap<>();

        // 사막 우물
        addArc(susSandEntries, "archaeology/desert_well", GenesisItems.OPAQUE_JELLY.get(), 0.66f);
        addArc(susSandEntries, "archaeology/desert_well", GenesisItems.CITRINE_SHARD.get(), 0.06f);
        addArc(susSandEntries, "archaeology/desert_well", GenesisItems.FADED_MEMORY.get(), 0.16f);

        // 사막 피라미드
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.45f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL.get(), 0.34f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.14f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SPIRIT_COMPASS.get(), 0.025f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.DIVINE_GRAIL.get(), 0.012f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.HARDENED_GLASS_PIECES.get(), 0.12f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.HARDENED_RED_GLASS_PIECES.get(), 0.23f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.FADED_MEMORY.get(), 0.17f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.FORGOTTEN_MEMORY.get(), 0.06f);

        // 트레일 유적
        addArc(susSandEntries, "archaeology/trail_ruins_common", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.16f);
        addArc(susSandEntries, "archaeology/trail_ruins_common", GenesisItems.SCALE_FOSSIL.get(), 0.08f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.051f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.021f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.DIVINE_GRAIL.get(), 0.008f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.FADED_MEMORY.get(), 0.12f);

        // 해양 유적
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.51f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL.get(), 0.27f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.06f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SPIRIT_COMPASS.get(), 0.008f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.FADED_MEMORY.get(), 0.18f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.FORGOTTEN_MEMORY.get(), 0.08f);

        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.51f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL.get(), 0.27f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.06f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SPIRIT_COMPASS.get(), 0.012f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.FADED_MEMORY.get(), 0.12f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.FORGOTTEN_MEMORY.get(), 0.09f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.UNRELATED_MEMORY.get(), 0.02f);

        add("add_sus_sand_items", new AddSusSandItemModifier(new LootItemCondition[0], susSandEntries));


        // Chest

        // 던전 & 폐광
        add("simple_dungeon_shard", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/simple_dungeon")).build() },
                GenesisItems.SHARD_OF_THE_MOUNTAIN.get(), 0.11f, 1, 3));

        add("faded_dungeon_memory", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/simple_dungeon")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.19f, 1, 3));

        add("forgotten_dungeon_memory", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/simple_dungeon")).build() },
                GenesisItems.FORGOTTEN_MEMORY.get(), 0.09f, 1, 2));

        add("unrelated_dungeon_memory", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/simple_dungeon")).build() },
                GenesisItems.UNRELATED_MEMORY.get(), 0.004f, 1, 1));

        add("mineshaft_fossil", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/abandoned_mineshaft")).build() },
                GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.15f, 1, 2));

        add("faded_mineshaft", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/abandoned_mineshaft")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.1f, 1, 2));

        // 사막 피라미드
        add("faded_desert_pyramid", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.2f, 1, 3));

        add("forgotten_desert_pyramid", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.FORGOTTEN_MEMORY.get(), 0.1f, 1, 2));

        add("unrelated_desert_pyramid", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.UNRELATED_MEMORY.get(), 0.008f, 1, 1));

        add("desert_pyramid_grail", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.DIVINE_GRAIL.get(), 0.02f, 1, 1));

        add("desert_pyramid_tablet_shard", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.TABLET_SHARD.get(), 0.002f, 1, 1));

        add("desert_pyramid_glass_pieces", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.HARDENED_GLASS_PIECES.get(), 0.26f, 1, 3));

        add("desert_pyramid_red_glass_pieces", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.HARDENED_RED_GLASS_PIECES.get(), 0.13f, 1, 2));

        // 삼림 대저택 & 보물
        add("mansion_shard", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/woodland_mansion")).build() },
                GenesisItems.SHARD_OF_THE_MOUNTAIN.get(), 0.05f, 1, 2));


        add("buried_treasure_radiant_tablet", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/buried_treasure")).build() },
                GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN.get(), 0.0001f, 1, 1));
        add("faded_buried_treasure", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/buried_treasure")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.1f, 1, 6));

        // 고대 도시
        add("ancient_city_template", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 0.023f, 1, 1));

        add("ancient_city_piece", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.42f, 1, 3));

        add("faded_ancient_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.3f, 1, 5));

        add("forgotten_ancient_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.FORGOTTEN_MEMORY.get(), 0.21f, 1, 4));

        add("unrelated_ancient_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.UNRELATED_MEMORY.get(), 0.07f, 1, 3));

        add("oblivion_ancient_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.OBLIVION_SPHERE.get(), 0.0013f, 1, 1));

        // 엔드 시티 & 유적 (Stronghold)
        add("end_city_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build() },
                GenesisItems.DREAM_POWDER.get(), 0.3f, 2, 5));

        add("faded_end_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.3f, 2, 5));

        add("forgotten_end_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build() },
                GenesisItems.FORGOTTEN_MEMORY.get(), 0.19f, 2, 3));

        add("unrelated_end_city", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build() },
                GenesisItems.UNRELATED_MEMORY.get(), 0.08f, 1, 1));

        add("stronghold_library_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_library")).build() },
                GenesisItems.DREAM_POWDER.get(), 0.3f, 1, 3));

        add("stronghold_crossing_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_crossing")).build() },
                GenesisItems.DREAM_POWDER.get(), 0.4f, 2, 5));

        add("stronghold_corridor_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build() },
                GenesisItems.DREAM_POWDER.get(), 0.4f, 2, 6));

        add("stronghold_corridor_grail", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build() },
                GenesisItems.DIVINE_GRAIL.get(), 0.02f, 1, 1));

        add("stronghold_corridor_faded", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.2f, 1, 2));

        add("stronghold_corridor_oblivion", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build() },
                GenesisItems.OBLIVION_SPHERE.get(), 0.002f, 1, 1));

        // 정글 사원
        add("jungle_temple_compass", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build() },
                GenesisItems.SPIRIT_COMPASS.get(), 0.019f, 1, 1));

        add("jungle_temple_template", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build() },
                GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(), 0.036f, 1, 1));

        add("jungle_temple_grail", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build() },
                GenesisItems.DIVINE_GRAIL.get(), 0.006f, 1, 1));

        add("faded_jungle_temple", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build() },
                GenesisItems.FADED_MEMORY.get(), 0.6f, 1, 3));

        add("forgotten_jungle_temple", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build() },
                GenesisItems.FORGOTTEN_MEMORY.get(), 0.2f, 1, 2));

        // 고양이 선물
        add("cat_gift_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "gameplay/cat_morning_gift")).build() },
                GenesisItems.DREAM_DANGO.get(), 0.35f, 1, 3));


        // Fish Drops

        add("cod_jelly_drop", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "entities/cod")).build() },
                GenesisItems.OPAQUE_JELLY.get(), 0.22f, 1, 2));

        add("salmon_jelly_drop", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "entities/salmon")).build() },
                GenesisItems.OPAQUE_JELLY.get(), 0.22f, 1, 2));

        add("pufferfish_jelly_drop", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "entities/pufferfish")).build() },
                GenesisItems.OPAQUE_JELLY.get(), 0.22f, 1, 2));

        add("tropical_fish_jelly_drop", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "entities/tropical_fish")).build() },
                GenesisItems.OPAQUE_JELLY.get(), 0.22f, 1, 2));
    }

    private void addArc(Map<ResourceLocation, List<AddSusSandItemModifier.Pair>> map, String path, Item item, float chance) {
        map.computeIfAbsent(new ResourceLocation("minecraft", path), k -> new ArrayList<>())
                .add(new AddSusSandItemModifier.Pair(item, chance));
    }
}