package com.gamunhagol.genesismod.world.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class NightmareAggravated {
    public static final FoodProperties REMNANTS_OF_A_DREAM = (new FoodProperties.Builder())
            .nutrition(0).saturationMod(0.0f).fast().alwaysEat().effect(new MobEffectInstance(MobEffects.HUNGER, 200,4),1.0F)
            .effect(new MobEffectInstance(MobEffects.CONFUSION,100,1),1.0F).build();
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pPlayer.awardStat(Stats.TIME_SINCE_REST, 216000);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
