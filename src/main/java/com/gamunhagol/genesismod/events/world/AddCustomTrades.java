package com.gamunhagol.genesismod.events.world;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;


@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class AddCustomTrades {
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event)
    {
        Random random = new Random();

        int shardRewardAmount = random.nextInt(5) + 1;
        int shardSellingAmount = (shardRewardAmount * 5) + random.nextInt(15) + 4;

        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.BLUE_CRYSTAL_SHARD.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.CITRINE_SHARD.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.GREEN_AMBER.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.ICE_FLOWER_SHARD.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.LIGHTING_CRYSTAL_SHARD.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.RED_CRYSTAL_SHARD.get(), shardRewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, shardSellingAmount)
                , new ItemStack(GenesisItems.WIND_STONE.get(), shardRewardAmount),3,10,1));
        event.getRareTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 48), new ItemStack(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(), 1),4,24,1));
        event.getRareTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD_BLOCK, 24), new ItemStack(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 1),3,64,1));
    }

    @SubscribeEvent
    public static void addVillagerTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.CLERIC) {


            List<VillagerTrades.ItemListing> trades = event.getTrades().get(4);

            Random random = new Random();
            if (random.nextFloat() < 0.2f) {
                trades.add(new BasicItemListing(
                        new ItemStack(Items.EMERALD, 6),
                        new ItemStack(GenesisItems.HEAL_SCROLL_1.get(), 1),
                        3,
                        6,
                        0.05f
                ));
            }
        }
    }
}
