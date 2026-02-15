package com.gamunhagol.genesismod.world.capability;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class WeaponStatsRegistry {
    // 아이템과 해당 아이템의 스탯(신성, 파괴)을 연결하는 저장소
    private static final Map<Item, WeaponData> STATS_MAP = new HashMap<>();

    // 스탯 데이터를 담는 간단한 레코드
    public record WeaponData(float holy, float destruction) {}

    static {
        // 여기에 무기별 수치를 등록합니다.
        register(GenesisItems.PEWRIESE_TACHI.get(), 2.0f, 0f);
        register(GenesisItems.PEWRIESE_LONGSWORD.get(), 0.0f, 2.0f);
        // 새로운 무기가 생기면 여기 한 줄만 추가하면 끝납니다.
    }

    private static void register(Item item, float holy, float destruction) {
        STATS_MAP.put(item, new WeaponData(holy, destruction));
    }

    public static WeaponData getStats(Item item) {
        return STATS_MAP.getOrDefault(item, new WeaponData(0f, 0f));
    }
}
