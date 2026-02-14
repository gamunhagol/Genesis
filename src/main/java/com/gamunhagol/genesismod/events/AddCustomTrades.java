package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;


@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class AddCustomTrades {
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event)
    {
        Random random = new Random();

        int rewardAmount = random.nextInt(5) + 1;
        int sellingAmount = random.nextInt(13) + 14;

        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.BLUE_CRYSTAL_SHARD.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.CITRINE_SHARD.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.GREEN_AMBER.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.ICE_FLOWER_SHARD.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.LIGHTING_CRYSTAL_SHARD.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.RED_CRYSTAL_SHARD.get(), rewardAmount),3,10,1));
        event.getGenericTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, sellingAmount)
                , new ItemStack(GenesisItems.WIND_STONE.get(), rewardAmount),3,10,1));
        event.getRareTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD, 48), new ItemStack(GenesisItems.ELVENIA_UPGRADE_SMITHING_TEMPLATE.get(), 1),4,24,1));
        event.getRareTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD_BLOCK, 24), new ItemStack(GenesisItems.PEWRIESE_UPGRADE_SMITHING_TEMPLATE.get(), 1),3,64,1));
    }

}
