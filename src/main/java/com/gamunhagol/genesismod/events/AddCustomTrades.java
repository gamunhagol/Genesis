package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.GenesisMod;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class AddCustomTrades {
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event)
    {
        event.getRareTrades().add(new BasicItemListing(new ItemStack(Items.EMERALD_BLOCK, 13), new ItemStack(GenesisItems.ISIS_UPGRADE_SMITHING_TEMPLATE.get(), 1),3,64,1));
    }

}
