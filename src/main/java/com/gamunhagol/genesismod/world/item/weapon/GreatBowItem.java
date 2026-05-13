package com.gamunhagol.genesismod.world.item.weapon;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.function.Predicate;

public class GreatBowItem extends BowItem {
    public static final int MAX_CHARGE_TIME = 38;

    public GreatBowItem(Properties pProperties) {
        super(pProperties);
    }

    // [참고] Item의 use 메서드는 플레이어의 우클릭에만 반응하므로 Player를 그대로 둡니다.
    // 몹들은 AI Goal을 통해 직접 startUsingItem()을 호출하므로 이 메서드를 타지 않습니다.
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean hasAmmo = !pPlayer.getProjectile(itemstack).isEmpty();

        if (pPlayer.getAbilities().instabuild || hasAmmo) {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        // 무조건 Player로 캐스팅하지 않고 범용적으로 사용할 변수 선언
        boolean isPlayer = pEntityLiving instanceof Player;
        boolean hasInfiniteAmmo = false;
        ItemStack projectileStack = ItemStack.EMPTY;

        // 1. 탄약(화살) 판별 로직
        if (isPlayer) {
            Player player = (Player) pEntityLiving;
            hasInfiniteAmmo = player.getAbilities().instabuild;
            projectileStack = player.getProjectile(pStack);

            if (hasInfiniteAmmo && (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem))) {
                projectileStack = new ItemStack(getCustomDefaultArrow());
            }
        } else {
            // 몹(Mob)일 경우 기본적으로 무한 탄창 및 커스텀 화살 강제 지급
            hasInfiniteAmmo = true;
            projectileStack = new ItemStack(getCustomDefaultArrow());
        }

        // 최종 확인: 여전히 화살이 없거나 커스텀 화살이 아니면 발사 중단
        if (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem)) {
            return;
        }

        int i = this.getUseDuration(pStack) - pTimeLeft;
        float f = getPowerForTime(i);

        if (!((double) f < 0.1D)) {
            if (!pLevel.isClientSide) {
                LargeArrowItem arrowitem = (LargeArrowItem) projectileStack.getItem();
                AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, projectileStack, pEntityLiving);

                // 2. 강화 사격 로직 (플레이어 & 몹 공통)
                if (i >= 100) {
                    if (abstractarrow instanceof LargeArrowEntity largeArrow) {
                        largeArrow.setEmpowered(true);
                    }

                    ServerLevel serverLevel = (ServerLevel) pLevel;

                    // [수정됨] player 대신 pEntityLiving(주체)의 위치 사용
                    Vec3 look = pEntityLiving.getLookAngle();
                    Vec3 horizontalForward = new Vec3(look.x, 0, look.z).normalize();

                    double px = pEntityLiving.getX() + horizontalForward.x * 0.8;
                    double py = pEntityLiving.getY() + 1.2;
                    double pz = pEntityLiving.getZ() + horizontalForward.z * 0.8;

                    serverLevel.sendParticles((ParticleOptions) EpicFightParticles.AIR_BURST.get(),
                            px, py, pz,
                            0,
                            -90.0F, pEntityLiving.getYRot(), -1.0F, 1.0);

                    // [수정됨] 사운드 소스를 무기 사용자에 맞춰 설정 (플레이어면 PLAYERS, 몹이면 HOSTILE 등)
                    SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                    serverLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(),
                            (SoundEvent) EpicFightSounds.NEUTRALIZE_MOBS.get(), source, 1.0F, 1.0F);
                }

                // 화살 발사
                abstractarrow.shootFromRotation(pEntityLiving, pEntityLiving.getXRot(), pEntityLiving.getYRot(), 0.0F, f * 5.6F, 1.0F);

                int flameLevel = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack);
                if (flameLevel > 0) {
                    abstractarrow.setSecondsOnFire(100);
                }

                if (f == 1.0F) abstractarrow.setCritArrow(true);

                // 크리에이티브 모드이거나 몹인 경우 화살이 필드에 남아 무한 파밍되는 것을 방지
                if (hasInfiniteAmmo) {
                    abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                // 무기 내구도 감소 (공통)
                pStack.hurtAndBreak(1, pEntityLiving, (entity) -> entity.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));

                SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                pLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(),
                        SoundEvents.ARROW_SHOOT, source, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                // 3. 인벤토리 및 스탯 처리 (플레이어 전용)
                if (isPlayer) {
                    Player player = (Player) pEntityLiving;
                    if (!hasInfiniteAmmo) {
                        projectileStack.shrink(1);
                        if (projectileStack.isEmpty()) {
                            player.getInventory().removeItem(projectileStack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                }

                pLevel.addFreshEntity(abstractarrow);
            }
        }
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float) pCharge / (float) MAX_CHARGE_TIME;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide) {
            int chargeTicks = this.getUseDuration(stack) - count;

            if (chargeTicks == 100) {
                boolean isPlayer = entity instanceof Player;
                SoundSource source = isPlayer ? SoundSource.PLAYERS : SoundSource.HOSTILE;

                // [수정됨] 플레이어일 경우에만 Epic Fight 스태미나 소모 적용
                if (isPlayer) {
                    Player player = (Player) entity;
                    EpicFightCapabilities.getPlayerPatchAsOptional(player).ifPresent(patch -> {
                        float staminaCost = 20.0F;
                        float currentStamina = patch.getStamina();

                        if (currentStamina >= staminaCost) {
                            patch.setStamina(currentStamina - staminaCost);
                            // 강화 성공 사운드
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    SoundEvents.ANVIL_PLACE, source, 1.0F, 0.5F);
                        } else {
                            // 스태미나 부족 시 활 사용 취소
                            player.stopUsingItem();
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    SoundEvents.FIRE_EXTINGUISH, source, 1.0F, 1.0F);
                        }
                    });
                } else {
                    // [수정됨] 몹일 경우 스태미나 소모 없이 바로 강화 성공 처리 (원한다면 확률이나 다른 조건 추가 가능)
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.ANVIL_PLACE, source, 1.0F, 0.5F);
                }
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.INFINITY_ARROWS) {
            return false;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof LargeArrowItem;
    }

    private LargeArrowItem getCustomDefaultArrow() {
        return (LargeArrowItem) GenesisItems.LARGE_ARROW.get();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}