package com.gamunhagol.genesismod.world.item.custom;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncMentalPower;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class EGHItem extends Item {
    private static final FoodProperties ENCHANTED_HEART_FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(1.0f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4500, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3000, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
            .build();

    public EGHItem(Properties pProperties) {
        super(pProperties.food(ENCHANTED_HEART_FOOD).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.heal(600.0f);

            if (entity instanceof Player player) {
                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    float recoveryAmount = 100.0f;
                    stats.setMental(Math.min(stats.getMaxMental(), stats.getMental() + recoveryAmount));

                    if (player instanceof ServerPlayer serverPlayer) {
                        GenesisNetwork.sendToPlayer(new PacketSyncMentalPower(stats.getMental()), serverPlayer);
                    }
                });
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }
}