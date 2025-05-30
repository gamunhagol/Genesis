package com.gamunhagol.genesismod.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class NightmareRelief {
    public static final FoodProperties DREAM_POWDER = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.2f).fast().alwaysEat().build();
    public static final FoodProperties DREAM_DANGO = (new FoodProperties.Builder())
            .nutrition(2).saturationMod(0.6f).fast().alwaysEat().build();

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
