package com.gamunhagol.genesismod.data.loot;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class GenesisLootTables {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "genesis");
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);


    @SubscribeEvent
    public static void modifyVanillaLootPools(final LootTableLoadEvent event) {

        if (event.getName().equals(BuiltInLootTables.ANCIENT_CITY)) {
            event.getTable().addPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F,2.0F))
                    .add(LootItem.lootTableItem(GenesisItems.ISIS_UPGRADE_SMITHING_TEMPLATE.get()).when(LootItemRandomChanceCondition.randomChance(0.3F)))
                    .build());
        }
    }

}
