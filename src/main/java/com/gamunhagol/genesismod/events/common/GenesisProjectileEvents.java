package com.gamunhagol.genesismod.events.common;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.item.weapon.GreatBowItem;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
            float enchantBonus = 0.0f;

            //  무기 스택 및 인챈트 보너스 판별
            if (projectile instanceof ThrownTrident trident) {
                try {
                    weaponStack = (ItemStack) GET_PICKUP_ITEM.invoke(trident);
                } catch (Exception e) {
                    // 리플렉션 실패 시 손에 든 무기로 폴백
                    if (player.getMainHandItem().getItem() instanceof TridentItem) weaponStack = player.getMainHandItem();
                    else if (player.getOffhandItem().getItem() instanceof TridentItem) weaponStack = player.getOffhandItem();
                }

                // 삼지창은 발사 시점에 누구를 맞출지 모르므로,
                // '찌르기'를 기본 보너스에 넣을지 말지 결정해야 합니다.
                // 보통은 '힘(Power)'처럼 항상 적용되게 하려면 아래 코드를 사용합니다.
                int impalingLevel = EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.IMPALING, weaponStack);
                if (impalingLevel > 0) {
                    enchantBonus = impalingLevel * 2.5f;
                }
            }
            else if (isBowOrCrossbow(player.getUseItem())) {
                weaponStack = player.getUseItem();
            }
            else if (isBowOrCrossbow(player.getMainHandItem())) {
                weaponStack = player.getMainHandItem();
            }
            else if (isBowOrCrossbow(player.getOffhandItem())) {
                weaponStack = player.getOffhandItem();
            }

            //  공통 계산 로직 (활, 삼지창 모두 여기서 처리)
            if (!weaponStack.isEmpty() && WeaponDataManager.hasData(weaponStack.getItem())) {

                // 활이라면 '힘' 인챈트 체크 (위에서 삼지창은 찌르기를 체크했으므로)
                if (weaponStack.getItem() instanceof BowItem) {
                    int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.POWER_ARROWS, weaponStack);
                    if (powerLevel > 0) enchantBonus = 0.5f * powerLevel + 0.5f;
                }

                // 스냅샷 생성
                DamageSnapshot rawSnapshot = WeaponRequirementHelper.calculateTotalDamage(player, weaponStack, enchantBonus);

                // 속도 배율 계산
                double velocity = projectile.getDeltaMovement().length();
                float chargeMultiplier = (float) (velocity / 3.0);

                // 대궁 배율 캡 적용
                float maxMultiplier = (weaponStack.getItem() instanceof GreatBowItem) ? 2.5f : 1.2f;
                chargeMultiplier = Math.min(Math.max(chargeMultiplier, 0.1f), maxMultiplier);

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