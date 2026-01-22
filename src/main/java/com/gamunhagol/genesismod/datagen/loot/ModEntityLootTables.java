package com.gamunhagol.genesismod.datagen.loot;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {
    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(GenesisEntities.COLLECTOR.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .when(LootItemRandomChanceCondition.randomChance(0.0001f))// 0.0001 = 0.01% 확률
                                .add(LootItem.lootTableItem(GenesisItems.MEDALLION_OF_DOMINION.get()))));

        this.add(GenesisEntities.COLLECTOR_GUARD.get(), LootTable.lootTable());

    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return GenesisEntities.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }
}
