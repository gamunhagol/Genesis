package com.gamunhagol.genesismod.world.item;

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
        // 체력 체크 없이, 횟수만 남아있다면 마시기 시작!
        if (getUses(stack) > 0) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player player) {
            // 1. 회복 적용
            player.heal(getHealAmount(pStack));

            // 2. 사용 횟수 차감
            setUses(pStack, getUses(pStack) - 1);

            // 3. 사운드 재생
            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return pStack; // 병이 사라지지 않고 그대로 반환됨
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
        float heal = getHealAmount(pStack);

        pTooltipComponents.add(Component.translatable("tooltip.genesis.grail.uses", uses, maxUses).withStyle(ChatFormatting.BLUE));

        if (uses == 0) {
            pTooltipComponents.add(Component.translatable("tooltip.genesis.grail.empty").withStyle(ChatFormatting.GRAY));
        }
    }

    // --- NBT 헬퍼 (전과 동일) ---
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
        // 야수의 유해 강화 수치를 포함한 최종 회복량 계산
        float baseHeal = 20.0f;
        int upgradeLevel = stack.hasTag() && stack.getTag().contains("HealLevel") ? stack.getTag().getInt("HealLevel") : 0;
        return baseHeal + (upgradeLevel * 20.0f); // 단계당 1씩 증가 예시
    }

    public void upgradeMaxUses(ItemStack stack) {
        int currentMax = getMaxUses(stack);
        // 최대 16번까지만 늘어나도록 제한
        if (currentMax < 16) {
            stack.getOrCreateTag().putInt("MaxUses", currentMax + 1);
        }
    }

    // 2. 회복량 증가 (야수의 유해 사용 시 호출)
    public void upgradeHealAmount(ItemStack stack) {
        // NBT 태그 가져오기
        net.minecraft.nbt.CompoundTag tag = stack.getOrCreateTag();

        // 현재 강화 레벨 확인 (없으면 0)
        int currentLevel = tag.contains("HealLevel") ? tag.getInt("HealLevel") : 0;

        // 최대 10강까지만 강화 가능하도록 제한
        if (currentLevel < 10) {
            tag.putInt("HealLevel", currentLevel + 1);
        }
    }

    public void refill(ItemStack stack) {
        setUses(stack, getMaxUses(stack));
    }
}