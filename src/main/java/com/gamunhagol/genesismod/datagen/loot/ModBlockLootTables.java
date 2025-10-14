package com.gamunhagol.genesismod.datagen.loot;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(GenesisBlocks.PEWRIESE_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.BLUE_CRYSTAL_BLOCK.get());
        this.dropSelf(GenesisBlocks.CITRINE_BLOCK.get());
        this.dropSelf(GenesisBlocks.RED_CRYSTAL_BLOCK.get());

        this.add(GenesisBlocks.PEWRIESE_ORE.get(),
                block -> createCopperLikeOreDrops(GenesisBlocks.PEWRIESE_ORE.get(), GenesisItems.PEWRIESE_ORE_PIECE.get()));

        this.add(GenesisBlocks.BLUE_CRYSTAL_CLUSTER.get(),
                block -> createSilkTouchDispatchTable(block,
                        LootItem.lootTableItem(GenesisItems.BLUE_CRYSTAL_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F,4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                ));
        this.add(GenesisBlocks.CITRINE_CLUSTER.get(),
                block -> createSilkTouchDispatchTable(block,
                        LootItem.lootTableItem(GenesisItems.CITRINE_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F,4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                ));
        this.add(GenesisBlocks.RED_CRYSTAL_CLUSTER.get(),
                block -> createSilkTouchDispatchTable(block,
                        LootItem.lootTableItem(GenesisItems.RED_CRYSTAL_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F,4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                ));


    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GenesisBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
