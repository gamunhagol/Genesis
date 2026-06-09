package com.gamunhagol.genesismod.world.item.custom;


import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;


public class AmethystApplePuddingItem extends Item {
    private static final FoodProperties PUDDING_FOOD = new FoodProperties.Builder()
            .nutrition(11)
            .saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 1800, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 12000, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 4800, 0), 1.0f)
            .build();

    public AmethystApplePuddingItem(Properties pProperties) {
        super(pProperties.food(PUDDING_FOOD));
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack.copy(), level, entity);

        if (entity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new ItemStack(Items.BOWL);
                }
                if (!player.getInventory().add(new ItemStack(Items.BOWL))) {
                    player.drop(new ItemStack(Items.BOWL), false);
                }
            }
        }

        return stack;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

}
