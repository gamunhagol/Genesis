package com.gamunhagol.genesismod.stats;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.world.capability.weapon.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WeaponRequirementHelper {

    /**
     * 모든 대미지를 계산하여 스냅샷으로 반환합니다.
     * 요구치 미달 시 물리/속성 대미지에 페널티(40%만 적용)가 가해집니다.
     */
    public static DamageSnapshot calculateTotalDamage(Player player, ItemStack stack, float baseVanillaDamage) {
        if (!WeaponDataManager.hasData(stack.getItem())) return DamageSnapshot.EMPTY;

        // 1. 요구치 체크 (장비 보너스 포함된 수치로 판정)
        boolean meetsReq = meetsRequirements(player, stack);
        float penalty = meetsReq ? 1.0f : 0.4f;

        // 2. 각 속성별 대미지 계산 및 페널티 적용
        float phys = calculatePhysical(player, stack, baseVanillaDamage) * penalty;
        float magic = calculateElemental(player, stack, "magic") * penalty;
        float fire = calculateElemental(player, stack, "fire") * penalty;
        float lightning = calculateElemental(player, stack, "lightning") * penalty;
        float frost = calculateElemental(player, stack, "frost") * penalty;
        float holy = calculateElemental(player, stack, "holy") * penalty;

        // 파괴(Destruction) 대미지는 페널티를 받지 않는 고정값입니다.
        float destruction = calculateElemental(player, stack, "destruction");

        return new DamageSnapshot(phys, magic, fire, lightning, frost, holy, destruction);
    }

    /**
     * 물리 대미지 계산 로직
     */
    private static float calculatePhysical(Player player, ItemStack stack, float vanillaDamage) {
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        int reinforceLevel = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);

        float base = data.basePhysical() > 0 ? data.basePhysical() : vanillaDamage;
        float reinforcedBase = base * (1.0f + (reinforceLevel * data.damageGrowth()));
        float bonus = 0.0f;

        // 보정치(Scaling) 계산
        Map<StatType, Float> currentScaling = getCurrentScaling(data, reinforceLevel);
        for (Map.Entry<StatType, Float> entry : currentScaling.entrySet()) {
            StatType statType = entry.getKey();
            // 물리 대미지는 근력, 기량, 신비 스탯의 영향을 받습니다.
            if (statType == StatType.STRENGTH || statType == StatType.DEXTERITY || statType == StatType.ARCANE) {
                int playerStat = getPlayerStat(player, statType);
                bonus += reinforcedBase * entry.getValue() * StatApplier.calculateScaling(playerStat);
            }
        }

        return reinforcedBase + bonus;
    }

    /**
     * 속성 대미지 계산 로직 (마법, 화염, 번개, 냉기, 신성, 파괴)
     */
    private static float calculateElemental(Player player, ItemStack stack, String type) {
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
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

        int reinforceLevel = stack.getCapability(WeaponStatsProvider.WEAPON_STATS).map(s -> s.getReinforceLevel()).orElse(0);
        float bonus = 0.0f;

        Map<StatType, Float> currentScaling = getCurrentScaling(data, reinforceLevel);
        for (Map.Entry<StatType, Float> entry : currentScaling.entrySet()) {
            StatType statType = entry.getKey();
            boolean scales = false;

            // 각 속성별 주력 보정 스탯 판정
            switch (type) {
                case "magic" -> scales = (statType == StatType.INTELLIGENCE);
                case "holy" -> scales = (statType == StatType.FAITH);
                case "fire", "lightning", "frost" -> scales = (statType == StatType.ARCANE);
            }
            // 신비(Arcane)는 모든 속성 대미지에 보조 보정치를 제공합니다.
            if (statType == StatType.ARCANE) scales = true;

            if (scales) {
                int playerStat = getPlayerStat(player, statType);
                bonus += base * entry.getValue() * StatApplier.calculateScaling(playerStat);
            }
        }

        return base + bonus;
    }

    /**
     * 무기 착용 요구치 충족 여부를 확인합니다.
     */
    public static boolean meetsRequirements(Player player, ItemStack stack) {
        if (!WeaponDataManager.hasData(stack.getItem())) return true;

        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        for (Map.Entry<StatType, Integer> entry : data.requirements().entrySet()) {
            // 장비 보너스가 포함된 최종 스탯으로 요구치 대조
            if (getPlayerStat(player, entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 강화 수치에 따른 보정 등급(Scaling) 변화를 적용합니다.
     */
    public static Map<StatType, Float> getCurrentScaling(WeaponStatData data, int level) {
        Map<StatType, Float> result = new java.util.HashMap<>(data.scaling());
        data.scalingOverrides().entrySet().stream()
                .filter(entry -> entry.getKey() <= level)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> result.putAll(entry.getValue()));
        return result;
    }

    /**
     * 플레이어의 최종 스탯 값을 가져오는 핵심 메서드입니다.
     * 1. 먼저 GenesisAttributes(장비 보너스 포함)를 조회합니다.
     * 2. 속성 시스템이 로드되지 않은 경우 Capability(순수 레벨)를 조회합니다.
     */
    public static int getPlayerStat(Player player, StatType type) {
        int baseLevel = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                .map(cap -> switch (type) {
                    case VIGOR -> cap.getVigor();
                    case MIND -> cap.getMind();
                    case ENDURANCE -> cap.getEndurance();
                    case STRENGTH -> cap.getStrength();
                    case DEXTERITY -> cap.getDexterity();
                    case INTELLIGENCE -> cap.getIntelligence();
                    case FAITH -> cap.getFaith();
                    case ARCANE -> cap.getArcane();
                }).orElse(10); // 데이터가 없으면 기본값 10

        Attribute statAttr = switch (type) {
            case VIGOR -> GenesisAttributes.VIGOR.get();
            case MIND -> GenesisAttributes.MIND.get();
            case ENDURANCE -> GenesisAttributes.ENDURANCE.get();
            case STRENGTH -> GenesisAttributes.STRENGTH.get();
            case DEXTERITY -> GenesisAttributes.DEXTERITY.get();
            case INTELLIGENCE -> GenesisAttributes.INTELLIGENCE.get();
            case FAITH -> GenesisAttributes.FAITH.get();
            case ARCANE -> GenesisAttributes.ARCANE.get();
        };

        int armorBonus = 0;
        if (statAttr != null && player.getAttribute(statAttr) != null) {
            var attrInst = player.getAttribute(statAttr);
            // (현재 속성의 총합) - (속성의 기본값) = 순수 장비/버프 보너스
            armorBonus = (int) (attrInst.getValue() - attrInst.getBaseValue());
        }

        return baseLevel + armorBonus;
    }
}