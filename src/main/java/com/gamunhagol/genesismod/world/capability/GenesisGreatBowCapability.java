package com.gamunhagol.genesismod.world.capability;

import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.RangedWeaponCapability;

public class GenesisGreatBowCapability extends RangedWeaponCapability {

    public GenesisGreatBowCapability(CapabilityItem.Builder builder) {
        super(builder);
    }

    // 예: 나중에 특정 상황에서 공격을 변경하거나 스태미나 소모량을 조절하고 싶다면
    // 여기서 관련 메서드를 오버라이드하면 됩니다.
}