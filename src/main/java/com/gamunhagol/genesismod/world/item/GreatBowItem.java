package com.gamunhagol.genesismod.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;


public class GreatBowItem extends BowItem {
    // [추가] 대형 활 전용 차징 시간 (22틱 = 약 1.1초)
    public static final int MAX_CHARGE_TIME = 22;

    public GreatBowItem(Properties pProperties) {
        super(pProperties);

        this.addDefaultAttributeModifiers();
    }

    // [추가] 차징 위력 계산 메서드
    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / (float)MAX_CHARGE_TIME;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            // [기존 코드 유지] 화살 체크 로직
            ItemStack projectileStack = player.getProjectile(pStack);
            if (!(projectileStack.getItem() instanceof LargeArrowItem)) {
                return;
            }

            // [추가] 여기서부터 커스텀 발사 로직 시작 (super 대신 실행됨)
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);

            if (!((double)f < 0.1D)) {
                if (!pLevel.isClientSide) {
                    LargeArrowItem arrowitem = (LargeArrowItem)projectileStack.getItem();
                    AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, projectileStack, player);

                    // [추가] 탄속 강화: 4.0F (바닐라 3.0F보다 빠름)
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 5.6F, 1.0F);

                    if (f == 1.0F) abstractarrow.setCritArrow(true);

                    // 내구도 감소 및 소리
                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    // 화살 소비 로직
                    if (!player.getAbilities().instabuild) {
                        projectileStack.shrink(1);
                        if (projectileStack.isEmpty()) player.getInventory().removeItem(projectileStack);
                    }

                    pLevel.addFreshEntity(abstractarrow);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
        // super.releaseUsing(pStack, pLevel, pEntityLiving, pTimeLeft); // <- 이 줄은 커스텀 로직이 대신하므로 지우거나 주석 처리하면 됩니다.
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return super.isValidRepairItem(pToRepair, pRepair);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    // 에픽 파이트 전용 속성(경직치, 스테미나)을 추가하는 메서드
    private void addDefaultAttributeModifiers() {
        // 여기에 나중에 에픽 파이트의 Attribute(IMPACT 등)를 추가하는 코드를 넣을 겁니다.
        // 지금은 개념만 잡으시고, 실제 등록은 에픽 파이트의 'ItemCapability' 시스템을 따르는 게 좋습니다.
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof LargeArrowItem;
    }
}
