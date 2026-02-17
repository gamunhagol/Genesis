package com.gamunhagol.genesismod.stats;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WeaponRequirementHelper {

    // 1. 요구치 충족 여부 확인 (기존 유지)
    public static boolean meetsRequirements(Player player, ItemStack stack) {
        if (!WeaponDataManager.hasData(stack.getItem())) return true;
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        var cap = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).orElse(null);
        if (cap == null) return true;

        for (Map.Entry<StatType, Integer> entry : data.requirements().entrySet()) {
            if (getPlayerStat(cap, entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    // 2. 최종 물리 대미지 계산 (강화 O, 특정 스탯 보정 O)
    public static float getPhysicalDamage(Player player, ItemStack stack, float vanillaDamage) {
        if (!WeaponDataManager.hasData(stack.getItem())) return vanillaDamage;
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        int level = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);

        // 기본뎀 + 강화 적용 (물리는 강화 수치가 핵심)
        float base = data.basePhysical() > 0 ? data.basePhysical() : vanillaDamage;
        float reinforcedBase = base * (1.0f + (level * data.damageGrowth()));

        float bonus = 0.0f;
        var cap = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).orElse(null);

        if (cap != null) {
            Map<StatType, Float> currentScaling = getCurrentScaling(data, level);
            for (var entry : currentScaling.entrySet()) {
                StatType statType = entry.getKey();
                float gradeValue = entry.getValue();

                // [기획 반영] 물리에 반응하는 스탯: 근력, 기량, 신비
                boolean scales = (statType == StatType.STRENGTH || statType == StatType.DEXTERITY || statType == StatType.ARCANE);

                if (scales) {
                    bonus += reinforcedBase * gradeValue * StatApplier.calculateScaling(getPlayerStat(cap, statType));
                }
            }
        }

        float total = reinforcedBase + bonus;
        // 요구치 미달 시 60% 삭감 패널티
        return meetsRequirements(player, stack) ? total : total * 0.4f;
    }

    // 3. 통합 속성 대미지 계산기 (강화 X, 특정 스탯 보정 O)
    public static float calculateElementalDamage(Player player, ItemStack stack, String type) {
        if (!WeaponDataManager.hasData(stack.getItem())) return 0;
        WeaponStatData data = WeaponDataManager.get(stack.getItem());

        // ★ 파괴(Destruction)는 어떤 변동도 없는 고정 피해
        if (type.equals("destruction")) return data.baseDestruction();

        float base = switch(type) {
            case "magic" -> data.baseMagic();
            case "fire" -> data.baseFire();
            case "lightning" -> data.baseLightning();
            case "frost" -> data.baseFrost();
            case "holy" -> data.baseHoly();
            default -> 0f;
        };

        if (base <= 0) return 0;

        // ★ [기획 반영] 속성 피해는 강화 수치(damageGrowth)를 적용하지 않음
        float effectiveBase = base;

        float bonus = 0.0f;
        var cap = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).orElse(null);

        if (cap != null) {
            int level = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);
            Map<StatType, Float> currentScaling = getCurrentScaling(data, level);

            for (var entry : currentScaling.entrySet()) {
                StatType statType = entry.getKey();
                float gradeValue = entry.getValue();
                boolean scales = false;

                // [기획 반영] 속성별 스탯 매핑 로직 동기화
                switch (type) {
                    case "magic" -> scales = (statType == StatType.INTELLIGENCE);
                    case "holy" -> scales = (statType == StatType.FAITH);
                    case "fire", "lightning", "frost" -> scales = (statType == StatType.ARCANE);
                }

                // 신비(ARCANE)는 모든 속성에 대해 보정치가 있다면 무조건 적용
                if (statType == StatType.ARCANE) scales = true;

                if (scales) {
                    bonus += effectiveBase * gradeValue * StatApplier.calculateScaling(getPlayerStat(cap, statType));
                }
            }
        }

        float total = effectiveBase + bonus;
        return meetsRequirements(player, stack) ? total : total * 0.4f;
    }

    // --- 헬퍼 메서드 (기존 유지) ---
    public static Map<StatType, Float> getCurrentScaling(WeaponStatData data, int level) {
        Map<StatType, Float> result = new java.util.HashMap<>(data.scaling());
        data.scalingOverrides().entrySet().stream()
                .filter(entry -> entry.getKey() <= level)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> result.putAll(entry.getValue()));
        return result;
    }

    private static int getPlayerStat(StatCapability cap, StatType type) {
        return switch (type) {
            case VIGOR -> cap.getVigor();
            case MIND -> cap.getMind();
            case ENDURANCE -> cap.getEndurance();
            case STRENGTH -> cap.getStrength();
            case DEXTERITY -> cap.getDexterity();
            case INTELLIGENCE -> cap.getIntelligence();
            case FAITH -> cap.getFaith();
            case ARCANE -> cap.getArcane();
        };
    }
}