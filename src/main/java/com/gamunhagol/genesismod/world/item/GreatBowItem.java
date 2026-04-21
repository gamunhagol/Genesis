package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Predicate;

public class GreatBowItem extends BowItem {
    public static final int MAX_CHARGE_TIME = 38;

    public GreatBowItem(Properties pProperties) {
        super(pProperties);
    }

    // 활을 당길 수 있는지 결정하는 로직
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
        if (pEntityLiving instanceof Player player) {
            boolean isCreative = player.getAbilities().instabuild;

            // 인벤토리에서 화살을 찾음
            ItemStack projectileStack = player.getProjectile(pStack);

            // [핵심 수정] 크리에이티브 모드인데 화살이 없거나, 엉뚱한 화살(일반 화살 등)이 잡힌 경우
            // 커스텀 화살로 교체.
            if (isCreative && (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem))) {
                projectileStack = new ItemStack(getCustomDefaultArrow());
            }

            // 최종 확인: 여전히 화살이 없거나 커스텀 화살이 아니면 발사 중단 (서바이벌용)
            if (projectileStack.isEmpty() || !(projectileStack.getItem() instanceof LargeArrowItem)) {
                return;
            }

            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);

            if (!((double)f < 0.1D)) {
                if (!pLevel.isClientSide) {
                    LargeArrowItem arrowitem = (LargeArrowItem)projectileStack.getItem();
                    AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, projectileStack, player);

                    if (i >= 100) {
                        if (abstractarrow instanceof LargeArrowEntity largeArrow) {
                            largeArrow.setEmpowered(true);
                        }

                        if (!pLevel.isClientSide) {
                            ServerLevel serverLevel = (ServerLevel) pLevel;

                            // 1. 위치 계산 (플레이어 앞 0.8블록, 가슴 높이)
                            Vec3 look = player.getLookAngle();
                            Vec3 horizontalForward = new Vec3(look.x, 0, look.z).normalize();

                            double px = player.getX() + horizontalForward.x * 0.8;
                            double py = player.getY() + 1.2;
                            double pz = player.getZ() + horizontalForward.z * 0.8;

                            // 2. 파티클 발사
                            // dx: 0.0F (세로로 세우기 위해 90도를 0도로 수정)
                            // dy: player.getYRot() (플레이어가 보는 방향에 맞게 회전)
                            // dz: -1.0F (Epic Fight 파티클 표준값)
                            serverLevel.sendParticles((ParticleOptions) EpicFightParticles.AIR_BURST.get(),
                                    px, py, pz,
                                    0,
                                    -90.0F, player.getYRot(), -1.0F, 1.0);

                            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                                    (SoundEvent) EpicFightSounds.NEUTRALIZE_MOBS.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }

                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 5.6F, 1.0F);

                    if (f == 1.0F) abstractarrow.setCritArrow(true);

                    // 크리에이티브 모드일 때 화살이 인벤토리에 다시 들어오지 않도록 설정
                    if (isCreative) {
                        abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!isCreative) {
                        projectileStack.shrink(1);
                        if (projectileStack.isEmpty()) {
                            player.getInventory().removeItem(projectileStack);
                        }
                    }

                    pLevel.addFreshEntity(abstractarrow);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / (float)MAX_CHARGE_TIME;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!level.isClientSide && entity instanceof Player player) {
            int chargeTicks = this.getUseDuration(stack) - count;

            if (chargeTicks == 100) {
                EpicFightCapabilities.getPlayerPatchAsOptional(player).ifPresent(patch -> {
                    float staminaCost = 15.0F; // 소모량
                    float currentStamina = patch.getStamina(); // 현재 스태미나 가져오기

                    if (currentStamina >= staminaCost) {
                        patch.setStamina(currentStamina - staminaCost);

                        // 강화 성공: 맑은 경험치 소리
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 0.5F);
                    } else {
                        // 스태미나 부족 시: 활 사용 중지 및 '치익' 하는 불 꺼지는 소리
                        player.stopUsingItem();
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                });
            }
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        // 이 활은 오직 LargeArrowItem만 쏠 수 있음
        return stack -> stack.getItem() instanceof LargeArrowItem;
    }

    private LargeArrowItem getCustomDefaultArrow() {
        // GenesisItems에서 등록한 화살 아이템을 반환
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