package com.gamunhagol.genesismod.content;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;


public class ReinforceManager {

    // 일반 무기 재료 (최대 25강)
    public static Item getStandardMaterial(int nextLevel) {
        if (nextLevel <= 5) return GenesisItems.SCALE_FOSSIL_SHARD.get();       // 1~5
        if (nextLevel <= 10) return GenesisItems.SCALE_FOSSIL.get();      // 6~10
        if (nextLevel <= 15) return GenesisItems.SCALE_FOSSIL_CLUMP.get();         // 11~15
        if (nextLevel <= 20) return GenesisItems.WEATHERED_ANCIENT_DRAGON_ROCK.get();         // 16~20
        if (nextLevel <= 22) return GenesisItems.ANCIENT_DRAGON_ROCK.get(); // 21~22
        if (nextLevel <= 24) return GenesisItems.ANCIENT_DRAGON_SCALE.get();     // 23~24
        if (nextLevel == 25) return GenesisItems.DRAGON_KING_SCALE.get();         // 25
        return null; // 강화 불가
    }

    // 특수 무기 재료 (최대 10강)
    public static Item getSpecialMaterial(int nextLevel) {
        if (nextLevel <= 3) return GenesisItems.SHARD_OF_THE_MOUNTAIN.get();       // 1~3
        if (nextLevel <= 6) return GenesisItems.FRAGMENT_OF_THE_MOUNTAIN.get();       // 4~6
        if (nextLevel <= 8) return GenesisItems.CLUMP_OF_THE_MOUNTAIN.get();    // 7~8
        if (nextLevel == 9) return GenesisItems.TABLET_SHARD.get();  // 9
        if (nextLevel == 10) return GenesisItems.TABLET_OF_THE_RADIANT_MOUNTAIN.get();   // 10
        return null;
    }

    public static int getMaxLevel(boolean isSpecial) {
        return isSpecial ? 10 : 25;
    }
}