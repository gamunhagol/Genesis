package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisCombatEvents {

    // 파괴 효과 전용 UUID (고유값)
    private static final UUID DESTRUCTION_HP_MOD_UUID = UUID.fromString("AD1E5150-9000-0000-0000-000000090010");

    // NBT 키 상수화 (오타 방지)
    private static final String KEY_EXTRA_HOLY = "GenesisExtraHoly";
    private static final String KEY_EXTRA_DESTRUCTION = "GenesisExtraDestruction";

    // =================================================================
    // [1] 툴팁 표시: 아이템에 마우스 올렸을 때 스탯 보여주기
    // =================================================================
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        stack.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
            float holy = stats.getHolyDamage();
            float destruction = stats.getDestructionDamage();

            if (holy <= 0 && destruction <= 0) return;

            // 삽입할 위치 찾기 (기본값은 리스트 맨 뒤)
            int insertIndex = event.getToolTip().size();

            // "공격 피해" 수치 근처를 찾기 위해 역순으로 탐색
            for (int i = 0; i < event.getToolTip().size(); i++) {
                String content = event.getToolTip().get(i).getString();
                // 바닐라의 "공격 피해" 수치나 에픽파이트 관련 텍스트 뒤에 배치하고 싶을 때 기준점 설정
                if (content.contains("공격 피해") || content.contains("Attack Damage")) {
                    insertIndex = i + 1;
                    break;
                }
            }

            // 찾은 위치에 역순으로 삽입 (위에서 아래로 holy -> destruction 순서가 되도록)
            if (destruction > 0) {
                event.getToolTip().add(insertIndex, Component.translatable("tooltip.genesis.destruction_damage", String.format(" %.1f", destruction))
                        .withStyle(ChatFormatting.DARK_RED));
            }

            if (holy > 0) {
                event.getToolTip().add(insertIndex, Component.translatable("tooltip.genesis.holy_damage", String.format(" %.1f", holy))
                        .withStyle(ChatFormatting.YELLOW));
            }
        });
    }

    // =================================================================
    // [2] Hurt 단계: '무기'에 붙은 추가 속성 대미지만 분리하여 예약
    // =================================================================
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        var source = event.getSource();
        LivingEntity target = event.getEntity();

        if (source.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            weapon.getCapability(WeaponStatsProvider.WEAPON_STATS).ifPresent(stats -> {
                if (stats.getHolyDamage() > 0) target.getPersistentData().putFloat(KEY_EXTRA_HOLY, stats.getHolyDamage());
                if (stats.getDestructionDamage() > 0) target.getPersistentData().putFloat(KEY_EXTRA_DESTRUCTION, stats.getDestructionDamage());
            });
        }
    }
    // =================================================================
    // [3] Damage 단계: (원본 소스 대미지 + 예약된 무기 대미지) 처리 및 방어력 적용
    // =================================================================
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() <= 0) return;

        LivingEntity target = event.getEntity();
        var source = event.getSource();
        float originalDamage = event.getAmount();
        float finalDamage = originalDamage;

        // --- 1. 물리 방어력 캡 돌파 (기존 로직 이식) ---
        if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            float currentArmor = target.getArmorValue();
            if (currentArmor > 20) {
                float excessArmor = currentArmor - 20.0f;
                float reductionFactor = excessArmor / (excessArmor + 50.0f);
                finalDamage = originalDamage * (1.0f - reductionFactor);
            }
        }

        // --- 2. 신성(Holy) 방어력 처리 (Source 판정) ---
        if (source.is(GenesisDamageTypes.HOLY)) {
            finalDamage = calculateHolyDamage(target, finalDamage);
        }

        // --- 3. 마법(Magic) 방어력 처리 (기존 DamageTypes.MAGIC 이식) ---
        else if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            finalDamage = calculateMagicDamage(target, finalDamage);
        }

        // --- 4. 파괴(Destruction) 효과 처리 (Source 판정) ---
        if (source.is(GenesisDamageTypes.DESTRUCTION)) {
            applyDestructionEffect(target, originalDamage);
        }

        // --- 5. 무기에서 넘어온 추가 대미지(NBT) 합산 ---
        if (target.getPersistentData().contains(KEY_EXTRA_HOLY)) {
            float extraHoly = target.getPersistentData().getFloat(KEY_EXTRA_HOLY);
            finalDamage += calculateHolyDamage(target, extraHoly);
            target.getPersistentData().remove(KEY_EXTRA_HOLY);
        }

        if (target.getPersistentData().contains(KEY_EXTRA_DESTRUCTION)) {
            float extraDest = target.getPersistentData().getFloat(KEY_EXTRA_DESTRUCTION);
            finalDamage += extraDest;
            applyDestructionEffect(target, extraDest);
            target.getPersistentData().remove(KEY_EXTRA_DESTRUCTION);
        }

        // 최종 적용
        if (Math.abs(finalDamage - originalDamage) > 0.001f) {
            event.setAmount(finalDamage);
        }
    }

    // =================================================================
    // [4] 사망 시 효과 해제
    // =================================================================
    @SubscribeEvent
    public static void onTargetDeath(LivingDeathEvent event) {
        removeDestructionEffect(event.getEntity());
    }

    // =================================================================
    // [Helper] 기능 분리 메서드들
    // =================================================================

    /**
     * 파괴 효과(최대 체력 영구 감소)를 적용하는 로직
     * LivingHurt(무기)와 LivingDamage(투사체) 양쪽에서 호출하여 재사용함
     */
    private static float calculateHolyDamage(LivingEntity target, float damage) {
        AttributeInstance holyDef = target.getAttribute(GenesisAttributes.HOLY_DEFENSE.get());
        if (holyDef != null && holyDef.getValue() > 0) {
            float reductionMultiplier = (float) (1.0 - (holyDef.getValue() / (holyDef.getValue() + 30.0)));
            float result = damage * reductionMultiplier;
            return (result < 0.5f && damage > 0) ? 0.5f : result; // 최소 0.5f 보정 유지
        }
        return damage;
    }

    private static float calculateMagicDamage(LivingEntity target, float damage) {
        AttributeInstance magicDef = target.getAttribute(GenesisAttributes.MAGIC_DEFENSE.get());
        if (magicDef != null && magicDef.getValue() > 0) {
            float reductionMultiplier = (float) (1.0 - (magicDef.getValue() / (magicDef.getValue() + 30.0)));
            float result = damage * reductionMultiplier;
            return (result < 0.5f && damage > 0) ? 0.5f : result; // 최소 0.5f 보정 유지
        }
        return damage;
    }

    private static void applyDestructionEffect(LivingEntity entity, float damageAmount) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        double currentModifierValue = 0;
        AttributeModifier existingMod = maxHealthAttr.getModifier(DESTRUCTION_HP_MOD_UUID);
        if (existingMod != null) {
            currentModifierValue = existingMod.getAmount();
            maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        }

        double newReductionValue = currentModifierValue - damageAmount;
        if (maxHealthAttr.getBaseValue() + newReductionValue < 1.0D) {
            newReductionValue = -maxHealthAttr.getBaseValue() + 1.0D;
        }

        maxHealthAttr.addPermanentModifier(new AttributeModifier(DESTRUCTION_HP_MOD_UUID, "Destruction Max HP Reduction", newReductionValue, AttributeModifier.Operation.ADDITION));
        if (entity instanceof ServerPlayer sp) sp.getHealth();
        entity.getPersistentData().putLong("GenesisDestructionEndTick", entity.level().getGameTime() + 144000L);
    }

    public static void removeDestructionEffect(LivingEntity entity) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) maxHealthAttr.removeModifier(DESTRUCTION_HP_MOD_UUID);
        entity.getPersistentData().remove("GenesisDestructionEndTick");
    }
}