package com.gamunhagol.genesismod.world.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
            .nutrition(11) // 허기
            .saturationMod(1.2f) // 포만감
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
        //  기본 식사 로직 수행 (허기 회복, 버프 적용 등)
        ItemStack resultStack = super.finishUsingItem(stack.copy(), level, entity);

        if (entity instanceof Player player) {
            // 크리에이티브 모드가 아닐 때만 그릇 반환 로직 실행
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                //  만약 마지막 푸딩이었다면?
                if (stack.isEmpty()) {
                    return new ItemStack(Items.BOWL);
                }
                //  푸딩이 남아있다면 빈 그릇을 인벤토리에 추가
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
