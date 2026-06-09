package com.gamunhagol.genesismod.world.item.tool;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DivineGrailItem extends Item {
    public DivineGrailItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (getUses(stack) > 0) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player player) {
            player.heal(getHealAmount(pStack));
            setUses(pStack, getUses(pStack) - 1);

            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return pStack;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int uses = getUses(pStack);
        int maxUses = getMaxUses(pStack);
        int upgradeLevel = pStack.hasTag() && pStack.getTag().contains("HealLevel") ? pStack.getTag().getInt("HealLevel") : 0;

        pTooltipComponents.add(Component.translatable("tooltip.genesis.grail.uses", uses, maxUses).withStyle(ChatFormatting.BLUE));

        if (upgradeLevel > 0) {
            pTooltipComponents.add(Component.translatable("tooltip.genesis.grail.enforce", upgradeLevel).withStyle(ChatFormatting.BLUE));
        }

        if (uses == 0) {
            pTooltipComponents.add(Component.translatable("tooltip.genesis.grail.empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public int getUses(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Uses") ? stack.getTag().getInt("Uses") : 4;
    }

    public void setUses(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt("Uses", Math.max(0, count));
    }

    public int getMaxUses(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("MaxUses") ? stack.getTag().getInt("MaxUses") : 4;
    }

    public float getHealAmount(ItemStack stack) {
        float baseHeal = 20.0f;
        int upgradeLevel = stack.hasTag() && stack.getTag().contains("HealLevel") ? stack.getTag().getInt("HealLevel") : 0;
        return baseHeal + (upgradeLevel * 20.0f); // 단계당 1씩 증가 예시
    }

    public void upgradeMaxUses(ItemStack stack) {
        int currentMax = getMaxUses(stack);
        if (currentMax < 16) {
            stack.getOrCreateTag().putInt("MaxUses", currentMax + 1);
        }
    }

    public void upgradeHealAmount(ItemStack stack) {
        net.minecraft.nbt.CompoundTag tag = stack.getOrCreateTag();
        int currentLevel = tag.contains("HealLevel") ? tag.getInt("HealLevel") : 0;
        if (currentLevel < 10) {
            tag.putInt("HealLevel", currentLevel + 1);
        }
    }

    public void refill(ItemStack stack) {
        setUses(stack, getMaxUses(stack));
    }
}