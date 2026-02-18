package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisProjectileEvents {

    // 삼지창의 protected 메서드에 접근하기 위한 리플렉션 캐싱
    private static final Method GET_PICKUP_ITEM = ObfuscationReflectionHelper.findMethod(ThrownTrident.class, "m_7941_"); // getPickupItem의 SRG 이름

    @SubscribeEvent
    public static void attachProjectileCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Projectile) {
            event.addCapability(new ResourceLocation(GenesisMod.MODID, "projectile_stats"), new ProjectileStatsProvider());
        }
    }

    @SubscribeEvent
    public static void onProjectileJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof Projectile projectile && projectile.getOwner() instanceof Player player) {
            ItemStack weaponStack = ItemStack.EMPTY;

            //  삼지창 처리 (리플렉션으로 protected 우회)
            if (projectile instanceof ThrownTrident trident) {
                try {
                    weaponStack = (ItemStack) GET_PICKUP_ITEM.invoke(trident);
                } catch (Exception e) {
                    // 리플렉션 실패 시 플레이어의 손에서 삼지창을 찾는 폴백(Fallback) 로직
                    if (player.getMainHandItem().getItem() instanceof TridentItem) weaponStack = player.getMainHandItem();
                    else if (player.getOffhandItem().getItem() instanceof TridentItem) weaponStack = player.getOffhandItem();
                }
            }
            //  활/석궁 처리 (차징 중인 아이템 혹은 손에 들린 무기 확인)
            else {
                ItemStack useItem = player.getUseItem();
                if (isBowOrCrossbow(useItem)) {
                    weaponStack = useItem;
                } else if (isBowOrCrossbow(player.getMainHandItem())) {
                    weaponStack = player.getMainHandItem();
                } else if (isBowOrCrossbow(player.getOffhandItem())) {
                    weaponStack = player.getOffhandItem();
                }
            }

            // 무기 데이터가 있고, 우리 모드 관리 대상인 경우
            if (!weaponStack.isEmpty() && WeaponDataManager.hasData(weaponStack.getItem())) {

                // [대미지 배율 계산]
                // 마인크래프트 화살의 최대 속도는 약 3.0이며, 이 때를 100% 대미지로 봅니다.
                double velocity = projectile.getDeltaMovement().length();
                float chargeMultiplier = (float) (velocity / 3.0);

                // 너무 낮거나 높지 않게 제한 (0.1 ~ 1.2배)
                chargeMultiplier = Math.min(Math.max(chargeMultiplier, 0.1f), 1.2f);

                // 기초 스탯 대미지 계산 (기본 바닐라뎀 인자는 0으로 전달하여 JSON 스탯 중심 계산)
                DamageSnapshot rawSnapshot = WeaponRequirementHelper.calculateTotalDamage(player, weaponStack, 0);

                // 모든 속성에 차징 배율 적용
                final float finalMul = chargeMultiplier;
                DamageSnapshot scaledSnapshot = new DamageSnapshot(
                        rawSnapshot.physical() * finalMul,
                        rawSnapshot.magic() * finalMul,
                        rawSnapshot.fire() * finalMul,
                        rawSnapshot.lightning() * finalMul,
                        rawSnapshot.frost() * finalMul,
                        rawSnapshot.holy() * finalMul,
                        rawSnapshot.destruction() * finalMul
                );

                projectile.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                    cap.setSnapshot(scaledSnapshot);
                });
            }
        }
    }

    // 활이나 석궁인지 판별하는 헬퍼 메서드
    private static boolean isBowOrCrossbow(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem);
    }
}