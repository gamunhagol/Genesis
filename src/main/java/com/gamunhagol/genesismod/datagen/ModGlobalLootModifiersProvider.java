package com.gamunhagol.genesismod.datagen;

import com.gamunhagol.genesismod.data.loot.AddChestItemModifier; // 방금 만든 클래스
import com.gamunhagol.genesismod.data.loot.AddSusSandItemModifier;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.HashMap;
import java.util.Map;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, GenesisMod.MODID);
    }

    @Override
    protected void start() {
        // 고고학 (수상한 모래/자갈)
        Map<ResourceLocation, AddSusSandItemModifier.Pair> susSandEntries = new HashMap<>();
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/desert_pyramid"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.025f));
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/desert_pyramid"),
                new AddSusSandItemModifier.Pair(GenesisItems.DIVINE_GRAIL.get(), 0.012f));
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/trail_ruins_rare"),
                new AddSusSandItemModifier.Pair(GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.051f));
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/trail_ruins_rare"),
                new AddSusSandItemModifier.Pair(GenesisItems.DIVINE_GRAIL.get(), 0.008f));
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/ocean_ruins_cold"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.008f));
        susSandEntries.put(new ResourceLocation("minecraft", "archaeology/ocean_ruins_warm"),
                new AddSusSandItemModifier.Pair(GenesisItems.SPIRIT_COMPASS.get(), 0.012f));

        add("add_sus_sand_items", new AddSusSandItemModifier(new LootItemCondition[0], susSandEntries));



        // 상자 (AddChestItemModifier 사용)


        // 사막 피라미드 (Desert Pyramid)
        add("desert_pyramid_grail", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/desert_pyramid")).build()
                }, GenesisItems.DIVINE_GRAIL.get(), 0.02f, 1, 1));

        // 고대 도시 (Ancient City)
        add("ancient_city_template", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build()
                }, GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 0.013f, 1, 1));

        add("ancient_city_piece", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/ancient_city")).build()
                }, GenesisItems.PEWRIESE_ORE_PIECE.get(), 0.42f, 1, 2));


        // 엔드 시티 (End City)
        add("end_city_powder", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/end_city_treasure")).build()
                }, GenesisItems.DREAM_POWDER.get(), 0.3f, 2, 5));


        // 유적 (Stronghold)
        add("stronghold_library_powder", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_library")).build()
                }, GenesisItems.DREAM_POWDER.get(), 0.3f, 1, 3));

        add("stronghold_crossing_powder", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_crossing")).build()
                }, GenesisItems.DREAM_POWDER.get(), 0.4f, 2, 5));

        add("stronghold_corridor_powder", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build()
                }, GenesisItems.DREAM_POWDER.get(), 0.4f, 2, 6));

        add("stronghold_corridor_grail", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/stronghold_corridor")).build()
                }, GenesisItems.DIVINE_GRAIL.get(), 0.02f, 1, 1));


        // 정글 사원 (Jungle Temple)
        add("jungle_temple_compass", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build()
                }, GenesisItems.SPIRIT_COMPASS.get(), 0.019f, 1, 1));

        add("jungle_temple_template", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build()
                }, GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(), 0.036f, 1, 1));
        add("jungle_temple_grail", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build()
                }, GenesisItems.DIVINE_GRAIL.get(), 0.006f, 1, 1));


        // 고양이 선물 (Cat Gift)
        add("cat_gift_powder", new AddChestItemModifier(
                new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "gameplay/cat_morning_gift")).build()
                }, GenesisItems.DREAM_DANGO.get(), 0.35f, 1, 3));
    }
}