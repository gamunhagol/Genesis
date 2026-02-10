package com.gamunhagol.genesismod.world.dimension;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class GenesisDimensionTypes {
    public static void bootstrap(BootstapContext<DimensionType> context) {
        context.register(GenesisDimensions.GENESIS_DIM_TYPE, new DimensionType(
                OptionalLong.of(18000), // fixedTime: 18000 = 항상 자정 (한밤중)
                false, // hasSkylight: 하늘의 빛이 있는가? (false = 없음)
                false, // hasCeiling: 네더처럼 천장이 막혀있는가?
                false, // ultraWarm: 물이 증발하는가? (네더)
                false, // natural: 나침반이 작동하고 침대를 쓸 수 있는가?
                1.0,   // coordinateScale: 좌표 비율 (네더는 8.0)
                true, // bedWorks: 침대 사용 가능? (false = 폭발)
                false, // respawnAnchorWorks: 리스폰 앵커 사용 가능?
                0,   // minY: 최소 높이
                320,   // height: 전체 높이 (-64 ~ 320)
                320,   // logicalHeight: 논리적 높이
                BlockTags.INFINIBURN_OVERWORLD, // 불이 영원히 타는 블록 태그
                new ResourceLocation("minecraft:the_end"), // effectsLocation: 하늘 렌더링 효과 (엔더 느낌)
                0.0f,  // ambientLight: 주변 밝기 (0.0f = 완전 암흑)
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }
}
