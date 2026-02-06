package com.gamunhagol.genesismod.datagen.loot;

import com.gamunhagol.genesismod.world.block.AmethystApplePuddingBlock;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    protected LootTable.Builder createCoinPileDrops(Block block, Item item) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F));

        LootItem.Builder<?> itemEntry = LootItem.lootTableItem(item);

        // 1층부터 8층까지 루프를 돌며 조건을 추가
        for (int i = 1; i <= 8; i++) {
            final int count = i;
            itemEntry.apply(SetItemCountFunction.setCount(ConstantValue.exactly((float) count))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(SnowLayerBlock.LAYERS, count))));
        }

        return LootTable.lootTable().withPool(pool.add(itemEntry));
    }

    @Override
    protected void generate() {
        this.dropSelf(GenesisBlocks.RAW_SILVER_BLOCK.get());
        this.dropSelf(GenesisBlocks.SILVER_BLOCK.get());
        this.dropSelf(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.PYULITELA_BLOCK.get());
        this.dropSelf(GenesisBlocks.BLUE_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.CITRINE_BLOCK.get());
        this.dropSelf(GenesisBlocks.RED_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.GREEN_AMBER_BLOCK.get());
        this.dropSelf(GenesisBlocks.ICE_FLOWER_BLOCK.get());
        this.dropSelf(GenesisBlocks.LIGHTING_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.WIND_STONE_BLOCK.get());
        this.dropSelf(GenesisBlocks.ELVENIA_BLOCK.get());
        this.dropSelf(GenesisBlocks.ANCIENT_ELVENIA_BLOCK.get());

        this.dropSelf(Block.byItem(GenesisItems.AMETHYST_APPLE.get()));

        //portions가 0(가득 참)일 때만 자기 자신을 드롭함
        this.add(GenesisBlocks.AMETHYST_APPLE_PUDDING_BLOCK.get(), (block) ->
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(AmethystApplePuddingBlock.PORTIONS, 0))))
                )
        );


        this.add(GenesisBlocks.SILVER_ORE.get(),
                block -> createStandardOreDrops(block, GenesisItems.RAW_SILVER.get()));

        this.add(GenesisBlocks.DEEPSLATE_SILVER_ORE.get(),
                block -> createStandardOreDrops(block, GenesisItems.RAW_SILVER.get()));

        this.add(GenesisBlocks.PEWRIESE_ORE.get(),
                block -> createCopperLikeOreDrops(GenesisBlocks.PEWRIESE_ORE.get(), GenesisItems.PEWRIESE_ORE_PIECE.get()));

        this.add(GenesisBlocks.PYULITELA_ORE.get(),
                block -> createStandardOreDrops(block, GenesisItems.PYULITELA.get()));


        this.add(GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.BLUE_CRYSTAL_SHARD.get()));
        this.add(GenesisBlocks.CITRINE_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.CITRINE_SHARD.get()));
        this.add(GenesisBlocks.RED_CRYSTAL_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.RED_CRYSTAL_SHARD.get()));
        this.add(GenesisBlocks.GREEN_AMBER_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.GREEN_AMBER.get()));
        this.add(GenesisBlocks.ICE_FLOWER_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.ICE_FLOWER_SHARD.get()));
        this.add(GenesisBlocks.LIGHTING_CRYSTAL_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.LIGHTING_CRYSTAL_SHARD.get()));
        this.add(GenesisBlocks.WIND_STONE_CLUSTER.get(),
                block -> createCrystalClusterDrops(block, GenesisItems.WIND_STONE.get()));


        this.add(GenesisBlocks.COPPER_COIN_PILE.get(),
                block -> createCoinPileDrops(block, GenesisItems.COPPER_COIN_PILE.get()));
        this.add(GenesisBlocks.SILVER_COIN_PILE.get(),
                block -> createCoinPileDrops(block, GenesisItems.SILVER_COIN_PILE.get()));
        this.add(GenesisBlocks.GOLD_COIN_PILE.get(),
                block -> createCoinPileDrops(block, GenesisItems.GOLD_COIN_PILE.get()));
        this.add(GenesisBlocks.PLATINUM_COIN_PILE.get(),
                block -> createCoinPileDrops(block, GenesisItems.PLATINUM_COIN_PILE.get()));

        this.add(GenesisBlocks.OBLIVION_CANDLE.get(), (block) -> createCandleDrops(block));


        this.add(GenesisBlocks.AEK_STATUE.get(),
                block -> LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(GenesisItems.ANCIENT_ELVENIA_INGOT.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(ApplyExplosionDecay.explosionDecay()))
                )
        );

    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createStandardOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createCrystalClusterDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GenesisBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
