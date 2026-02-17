package com.gamunhagol.genesismod.datagen;

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
        // --- 고고학 (수상한 모래/자갈) ---
        Map<ResourceLocation, List<AddSusSandItemModifier.Pair>> susSandEntries = new HashMap<>();

        // 사막 우물
        addArc(susSandEntries, "archaeology/desert_well", GenesisItems.OPAQUE_JELLY.get(), 0.66f);

        // 사막 피라미드
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.45f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL.get(), 0.34f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.14f); // 오타 수정: , -> /
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.SPIRIT_COMPASS.get(), 0.025f);
        addArc(susSandEntries, "archaeology/desert_pyramid", GenesisItems.DIVINE_GRAIL.get(), 0.012f);

        // 트레일 유적
        addArc(susSandEntries, "archaeology/trail_ruins_common", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.16f);
        addArc(susSandEntries, "archaeology/trail_ruins_common", GenesisItems.SCALE_FOSSIL.get(), 0.08f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.051f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.021f);
        addArc(susSandEntries, "archaeology/trail_ruins_rare", GenesisItems.DIVINE_GRAIL.get(), 0.008f);

        // 해양 유적
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.51f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL.get(), 0.27f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.06f);
        addArc(susSandEntries, "archaeology/ocean_ruins_cold", GenesisItems.SPIRIT_COMPASS.get(), 0.008f);

        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.51f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL.get(), 0.27f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SCALE_FOSSIL_CLUMP.get(), 0.06f);
        addArc(susSandEntries, "archaeology/ocean_ruins_warm", GenesisItems.SPIRIT_COMPASS.get(), 0.012f);

        add("add_sus_sand_items", new AddSusSandItemModifier(new LootItemCondition[0], susSandEntries));


        // --- 상자 (Chest) 전리품 ---

        // 던전 & 폐광
        add("simple_dungeon_shard", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/simple_dungeon")).build() },
                GenesisItems.SHARD_OF_THE_MOUNTAIN.get(), 0.11f, 1, 3));

        add("mineshaft_fossil", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/abandoned_mineshaft")).build() },
                GenesisItems.SCALE_FOSSIL_SHARD.get(), 0.15f, 1, 2));

        // 사막 피라미드 (이름 중복 해결)
        add("desert_pyramid_grail", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.DIVINE_GRAIL.get(), 0.02f, 1, 1));

        add("desert_pyramid_tablet_shard", new AddChestItemModifier( // 이름 변경
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build() },
                GenesisItems.TABLET_SHARD.get(), 0.002f, 1, 1));

        // 삼림 대저택 & 보물
        add("mansion_shard", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/woodland_mansion")).build() },
                GenesisItems.SHARD_OF_THE_MOUNTAIN.get(), 0.05f, 1, 2));


        add("buried_treasure_radiant_tablet", new AddChestItemModifier( // 이름 더 명확하게 변경
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/buried_treasure")).build() },
                GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN.get(), 0.0001f, 1, 1));

        // 고대 도시
        add("ancient_city_template", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 0.013f, 1, 1));

        add("ancient_city_piece", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build() },
                GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.42f, 1, 2));

        // 엔드 시티 & 유적 (Stronghold)
        add("end_city_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build() },
                GenesisItems.DREAM_POWDER.get(), 0.3f, 2, 5));

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

        // 고양이 선물
        add("cat_gift_powder", new AddChestItemModifier(
                new LootItemCondition[] { new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "gameplay/cat_morning_gift")).build() },
                GenesisItems.DREAM_DANGO.get(), 0.35f, 1, 3));


        // --- 몹 드롭 (Fish Drops) ---

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