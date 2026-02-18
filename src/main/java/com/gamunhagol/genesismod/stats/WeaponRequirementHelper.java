package com.gamunhagol.genesismod.stats;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.world.capability.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WeaponRequirementHelper {

    /**
     * [신규] 모든 대미지를 한 번에 계산하여 스냅샷으로 반환
     * 근접 공격과 투사체 발사 시 모두 이 메서드를 사용합니다.
     */
    public static DamageSnapshot calculateTotalDamage(Player player, ItemStack stack, float baseVanillaDamage) {
        if (!WeaponDataManager.hasData(stack.getItem())) return DamageSnapshot.EMPTY;

        // 요구치 미달 체크 (미달 시 대미지 60% 감소)
        boolean meetsReq = meetsRequirements(player, stack);
        float penalty = meetsReq ? 1.0f : 0.4f;

        // 1. 물리 계산
        float phys = calculatePhysical(player, stack, baseVanillaDamage) * penalty;

        // 2. 속성 계산 (파괴 속성은 페널티를 받지 않음)
        float magic = calculateElemental(player, stack, "magic") * penalty;
        float fire = calculateElemental(player, stack, "fire") * penalty;
        float lightning = calculateElemental(player, stack, "lightning") * penalty;
        float frost = calculateElemental(player, stack, "frost") * penalty;
        float holy = calculateElemental(player, stack, "holy") * penalty;
        float destruction = calculateElemental(player, stack, "destruction");

        return new DamageSnapshot(phys, magic, fire, lightning, frost, holy, destruction);
    }

    // 내부 계산 로직 (물리)
    private static float calculatePhysical(Player player, ItemStack stack, float vanillaDamage) {
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        int level = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);

        float base = data.basePhysical() > 0 ? data.basePhysical() : vanillaDamage;
        float reinforcedBase = base * (1.0f + (level * data.damageGrowth()));
        float bonus = 0.0f;

        var cap = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).orElse(null);
        if (cap != null) {
            Map<StatType, Float> currentScaling = getCurrentScaling(data, level);
            for (var entry : currentScaling.entrySet()) {
                StatType statType = entry.getKey();
                if (statType == StatType.STRENGTH || statType == StatType.DEXTERITY || statType == StatType.ARCANE) {
                    bonus += reinforcedBase * entry.getValue() * StatApplier.calculateScaling(getPlayerStat(cap, statType));
                }
            }
        }
        return reinforcedBase + bonus;
    }

    // 내부 계산 로직 (속성)
    private static float calculateElemental(Player player, ItemStack stack, String type) {
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        if (type.equals("destruction")) return data.baseDestruction(); // 파괴는 고정값

        float base = switch(type) {
            case "magic" -> data.baseMagic();
            case "fire" -> data.baseFire();
            case "lightning" -> data.baseLightning();
            case "frost" -> data.baseFrost();
            case "holy" -> data.baseHoly();
            default -> 0f;
        };
        if (base <= 0) return 0;

        float bonus = 0.0f;
        var cap = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).orElse(null);
        if (cap != null) {
            int level = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);
            Map<StatType, Float> currentScaling = getCurrentScaling(data, level);

            for (var entry : currentScaling.entrySet()) {
                StatType statType = entry.getKey();
                boolean scales = false;
                switch (type) {
                    case "magic" -> scales = (statType == StatType.INTELLIGENCE);
                    case "holy" -> scales = (statType == StatType.FAITH);
                    case "fire", "lightning", "frost" -> scales = (statType == StatType.ARCANE);
                }
                if (statType == StatType.ARCANE) scales = true; // 신비는 모든 속성 보정

                if (scales) {
                    bonus += base * entry.getValue() * StatApplier.calculateScaling(getPlayerStat(cap, statType));
                }
            }
        }
        return base + bonus;
    }

    // [기존 유지] 요구치 확인
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