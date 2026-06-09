package com.gamunhagol.genesismod.world.weapon;

import com.gamunhagol.genesismod.api.StatType;
import java.util.Map;

public record WeaponStatData(
        float basePhysical,
        float baseMagic,
        float baseFire,
        float baseLightning,
        float baseFrost,
        float baseHoly,
        float baseDestruction,

        Map<StatType, Integer> requirements,
        Map<StatType, Float> scaling,
        boolean isSpecial,
        float damageGrowth,
        Map<Integer, Map<StatType, Float>> scalingOverrides
) {
    public static final WeaponStatData EMPTY = new WeaponStatData(
            0, 0, 0, 0, 0, 0, 0,
            Map.of(), Map.of(), false, 0.0f, Map.of()
    );
}