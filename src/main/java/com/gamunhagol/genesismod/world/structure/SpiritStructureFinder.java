package com.gamunhagol.genesismod.world.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 🔍 SpiritStructureFinder
 * 최적화됨: HolderSet을 사용하여 한 번의 쿼리로 가장 가까운 구조물을 탐색함.
 */
public class SpiritStructureFinder {

    @Nullable
    public static BlockPos findNearest(Level level, List<String> keys, BlockPos origin, int radius) {
        if (!(level instanceof ServerLevel server)) return null;

        ChunkGenerator generator = server.getChunkSource().getGenerator();
        List<Holder<Structure>> targetStructures = new ArrayList<>();

        // 1. 문자열 키(String)들을 Holder<Structure>로 변환하여 리스트에 모음
        for (String key : keys) {
            // [안전장치] tryParse를 사용하여 잘못된 ID가 들어와도 튕기지 않게 함
            ResourceLocation rl = ResourceLocation.tryParse(key);
            if (rl == null) continue;

            Optional<Holder.Reference<Structure>> holderOpt =
                    server.registryAccess().registryOrThrow(Registries.STRUCTURE)
                            .getHolder(ResourceKey.create(Registries.STRUCTURE, rl));

            holderOpt.ifPresent(targetStructures::add);
        }

        if (targetStructures.isEmpty()) return null;

        // 2. 모은 구조물들을 하나의 집합(HolderSet)으로 만듦
        HolderSet<Structure> structureSet = HolderSet.direct(targetStructures);

        // 3. 엔진에게 "이 집합 안에 있는 것 중 제일 가까운 거 찾아줘"라고 한 번만 요청
        Pair<BlockPos, Holder<Structure>> found =
                generator.findNearestMapStructure(server, structureSet, origin, radius, false);

        // 결과 반환 (찾았으면 위치, 없으면 null)
        return found != null ? found.getFirst() : null;
    }

    /**
     * ✅ 단일 구조물 탐색 (호환용)
     */
    @Nullable
    public static BlockPos findNearest(Level level, String structureKey, BlockPos origin, int radius) {
        return findNearest(level, List.of(structureKey), origin, radius);
    }
}
