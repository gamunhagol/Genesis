package com.gamunhagol.genesismod.stats;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.init.attributes.GenesisAttributes;
import com.gamunhagol.genesismod.world.capability.weapon.WeaponStatsProvider;
import com.gamunhagol.genesismod.world.weapon.WeaponDataManager;
import com.gamunhagol.genesismod.world.weapon.WeaponStatData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WeaponRequirementHelper {

    // [수정됨] Player -> LivingEntity로 인자 변경
    public static DamageSnapshot calculateTotalDamage(LivingEntity entity, ItemStack stack, float baseVanillaDamage) {
        if (!WeaponDataManager.hasData(stack.getItem())) return DamageSnapshot.EMPTY;

        // 1. 요구치 체크 (몹은 무조건 통과)
        boolean meetsReq = meetsRequirements(entity, stack);
        float penalty = meetsReq ? 1.0f : 0.4f;

        // 2. 각 속성별 대미지 계산 (LivingEntity 전달)
        float phys = calculatePhysical(entity, stack, baseVanillaDamage) * penalty;
        float magic = calculateElemental(entity, stack, "magic") * penalty;
        float fire = calculateElemental(entity, stack, "fire") * penalty;
        float lightning = calculateElemental(entity, stack, "lightning") * penalty;
        float frost = calculateElemental(entity, stack, "frost") * penalty;
        float holy = calculateElemental(entity, stack, "holy") * penalty;

        float destruction = calculateElemental(entity, stack, "destruction");

        return new DamageSnapshot(phys, magic, fire, lightning, frost, holy, destruction);
    }

    private static float calculatePhysical(LivingEntity entity, ItemStack stack, float vanillaBonus) {
        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        int reinforceLevel = stack.getCapability(WeaponStatsProvider.WEAPON_STATS)
                .map(s -> s.getReinforceLevel()).orElse(0);

        float baseWeaponDamage = data.basePhysical();
        float reinforcedBase = baseWeaponDamage * (1.0f + (reinforceLevel * data.damageGrowth()));

        float scalingBonus = 0.0f;
        Map<StatType, Float> currentScaling = getCurrentScaling(data, reinforceLevel);

        for (Map.Entry<StatType, Float> entry : currentScaling.entrySet()) {
            StatType statType = entry.getKey();
            if (statType == StatType.STRENGTH || statType == StatType.DEXTERITY || statType == StatType.ARCANE) {
                // [수정됨]getEntityStat 호출
                int entityStat = getEntityStat(entity, statType);
                scalingBonus += reinforcedBase * entry.getValue() * StatApplier.calculateScaling(entityStat);
            }
        }

        return reinforcedBase + scalingBonus + vanillaBonus;
    }

    private static float calculateElemental(LivingEntity entity, ItemStack stack, String type) {
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

            switch (type) {
                case "magic" -> scales = (statType == StatType.INTELLIGENCE);
                case "holy" -> scales = (statType == StatType.FAITH);
                case "fire", "lightning", "frost" -> scales = (statType == StatType.ARCANE);
            }
            if (statType == StatType.ARCANE) scales = true;

            if (scales) {
                // [수정됨]getEntityStat 호출
                int entityStat = getEntityStat(entity, statType);
                bonus += base * entry.getValue() * StatApplier.calculateScaling(entityStat);
            }
        }

        return base + bonus;
    }

    // [수정됨] 몹은 항상 요구치를 충족하는 것으로 처리
    public static boolean meetsRequirements(LivingEntity entity, ItemStack stack) {
        if (!(entity instanceof Player player)) return true; // 몹은 프리패스

        if (!WeaponDataManager.hasData(stack.getItem())) return true;

        WeaponStatData data = WeaponDataManager.get(stack.getItem());
        for (Map.Entry<StatType, Integer> entry : data.requirements().entrySet()) {
            if (getEntityStat(player, entry.getKey()) < entry.getValue()) {
                return false;
            }
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

    /**
     * [핵심 리팩토링] 플레이어와 몹 공통 스탯 획득 로직
     */
    public static int getEntityStat(LivingEntity entity, StatType type) {
        int baseLevel = 10; // 기본값

        // 1. 플레이어라면 Capability에서 실제 레벨을 가져옴
        if (entity instanceof Player player) {
            baseLevel = player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(cap -> switch (type) {
                        case VIGOR -> cap.getVigor();
                        case MIND -> cap.getMind();
                        case ENDURANCE -> cap.getEndurance();
                        case STRENGTH -> cap.getStrength();
                        case DEXTERITY -> cap.getDexterity();
                        case INTELLIGENCE -> cap.getIntelligence();
                        case FAITH -> cap.getFaith();
                        case ARCANE -> cap.getArcane();
                    }).orElse(10);
        } else {
            // 2. 몹이라면 기본 스탯을 10으로 설정 (필요 시 몹 종류별로 분기 가능)
            baseLevel = 10;
        }

        // 3. 공통: 장비 보너스(Attribute)는 몹에게도 적용될 수 있음 (네임드 몹 등)
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

        int bonus = 0;
        if (statAttr != null && entity.getAttribute(statAttr) != null) {
            var attrInst = entity.getAttribute(statAttr);
            bonus = (int) (attrInst.getValue() - attrInst.getBaseValue());
        }

        return baseLevel + bonus;
    }
}