package com.gamunhagol.genesismod.stats;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;


import java.util.UUID;

public class StatApplier {
    public static final UUID VIGOR_MOD_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-1111-111111111111");
    public static final UUID ENDURANCE_MAX_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222222");
    public static final UUID ENDURANCE_REGEN_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222223");
    public static final UUID ENDURANCE_WEIGHT_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-2222-222222222224");
    public static final UUID LUCK_MOD_UUID = UUID.fromString("A1B2C3D4-E5F6-7890-3333-333333333333");

    public static void applyAll(Player player, StatCapability stats) {
        applyVigor(player, stats.getVigor());
        applyEndurance(player, stats.getEndurance());
        applyArcane(player, stats.getArcane());
        stats.updateMaxMental();
    }

    public static void applyVigor(Player player, int level) {
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            attr.removeModifier(VIGOR_MOD_UUID);

            float totalHealthGoal;
            if (level <= 10) {
                // 1~10레벨: 2에서 시작해 레벨당 2씩 증가
                totalHealthGoal = 2.0f + (level - 1) * 2.0f;
            } else if (level <= 50) {
                // 11~50레벨: 20에서 시작해 레벨당 2씩 증가
                totalHealthGoal = 20.0f + (level - 10) * 2.0f;
            } else if (level < 99) {
                // 51~98레벨: 100에서 시작해 레벨당 1씩 증가
                totalHealthGoal = 100.0f + (level - 50) * 1.0f;
            } else {
                // 99레벨: 최종 보너스 포함 150
                totalHealthGoal = 150.0f;
            }

            // 바닐라 기본값 20을 뺀 나머지를 Modifier로 주입
            float modifierValue = totalHealthGoal - 20.0f;
            attr.addPermanentModifier(new AttributeModifier(VIGOR_MOD_UUID, "Genesis Vigor", modifierValue, AttributeModifier.Operation.ADDITION));

            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    // [지구력 -> 에픽파이트 스태미나]
    public static void applyEndurance(Player player, int level) {
        //  최대 스태미나 보정
        AttributeInstance maxStam = player.getAttribute(EpicFightAttributes.MAX_STAMINA.get());
        if (maxStam != null) {
            maxStam.removeModifier(ENDURANCE_MAX_UUID);

            float staminaGoal;
            if (level <= 10) {
                // 1~10레벨: 2에서 15까지 성장
                staminaGoal = 2.0f + (level - 1) * 1.444f;
            } else if (level <= 20) {
                // 11~20레벨: 15에서 30까지 성장 (+1.5/L)
                staminaGoal = 15.0f + (level - 10) * 1.5f;
            } else if (level < 99) {
                // 21~98레벨: 30에서 108까지 성장 (+1.0/L)
                staminaGoal = 30.0f + (level - 20) * 1.0f;
            } else {
                // 99레벨: 최종 보너스 포함 110
                staminaGoal = 110.0f;
            }

            // 에픽파이트 기본값(15)과의 차이를 Modifier로 적용
            maxStam.addPermanentModifier(new AttributeModifier(ENDURANCE_MAX_UUID, "Genesis Endurance Max", staminaGoal - 15.0f, AttributeModifier.Operation.ADDITION));
        }

        // 2. 스태미나 재생 속도 (레벨당 0.01) [cite: 2026-02-15]
        AttributeInstance regenStam = player.getAttribute(EpicFightAttributes.STAMINA_REGEN.get());
        if (regenStam != null) {
            regenStam.removeModifier(ENDURANCE_REGEN_UUID);
            regenStam.addPermanentModifier(new AttributeModifier(ENDURANCE_REGEN_UUID, "Genesis Endurance Regen", level * 0.01f, AttributeModifier.Operation.ADDITION));
        }

        // 3. 무게 중량 경감 (레벨당 -0.5) [cite: 2026-02-15]
        AttributeInstance weight = player.getAttribute(EpicFightAttributes.WEIGHT.get());
        if (weight != null) {
            weight.removeModifier(ENDURANCE_WEIGHT_UUID);
            weight.addPermanentModifier(new AttributeModifier(ENDURANCE_WEIGHT_UUID, "Genesis Endurance Weight Mitigation", level * -0.5f, AttributeModifier.Operation.ADDITION));
        }
    }


    public static void applyArcane(Player player, int level) {
        AttributeInstance attr = player.getAttribute(Attributes.LUCK);
        if (attr != null) {
            attr.removeModifier(LUCK_MOD_UUID);
            // 최종 레벨 99에서 약 4.95의 운을 가짐
            attr.addPermanentModifier(new AttributeModifier(LUCK_MOD_UUID, "Genesis Arcane", level * 0.05f, AttributeModifier.Operation.ADDITION));
        }
    }

    // 소프트 캡 계산기
    public static float calculateScaling(int level) {
        // 1~20레벨: 초반 구간 (전체 효율의 약 35% 확보)
        if (level <= 20) {
            return level * 0.0175f; // Lv20 달성 시 0.35 (35%)
        }
        // 21~60레벨: 핵심 성장 구간 (추가 40% 확보, 총 75%)
        else if (level <= 60) {
            return 0.35f + (level - 20) * 0.01f; // Lv60 달성 시 0.75 (75%)
        }
        // 61~80레벨: 소프트 캡 구간 (효율 저하 시작, 추가 15% 확보, 총 90%)
        else if (level <= 80) {
            return 0.75f + (level - 60) * 0.0075f; // Lv80 달성 시 0.90 (90%)
        }
        // 81~99레벨: 하드 캡 구간 (최종 성장, 추가 10% 확보, 총 100%)
        else {
            // 80 이후는 성장이 매우 미미함 (Lv99 달성 시 약 0.995~1.0)
            return 0.90f + (Math.min(level, 99) - 80) * 0.005f;
        }
    }
}