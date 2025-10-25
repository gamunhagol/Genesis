package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Optional;

/**
 * 🔍 SpiritStructureFinder
 * 여러 구조물 후보 중 가장 가까운 구조물을 탐색함.
 */
public class SpiritStructureFinder {

    /**
     * ✅ 여러 구조물 리스트를 받아 가장 가까운 구조물을 반환
     *
     * @param level   현재 레벨 (ServerLevel)
     * @param keys    구조물 ID 리스트 (예: ["minecraft:fortress", "minecraft:bastion_remnant"])
     * @param origin  탐색 시작 위치 (플레이어 위치)
     * @param radius  탐색 반경 (기본: 6400)
     * @return 가장 가까운 구조물의 위치, 없으면 null
     */
    @Nullable
    public static BlockPos findNearest(Level level, List<String> keys, BlockPos origin, int radius) {
        if (!(level instanceof ServerLevel server)) return null;

        ChunkGenerator generator = server.getChunkSource().getGenerator();

        BlockPos nearestPos = null;
        double nearestDist = Double.MAX_VALUE;

        for (String key : keys) {
            ResourceLocation rl = new ResourceLocation(key);
            Optional<Holder.Reference<Structure>> holderOpt =
                    server.registryAccess().registryOrThrow(Registries.STRUCTURE)
                            .getHolder(ResourceKey.create(Registries.STRUCTURE, rl));

            if (holderOpt.isEmpty()) continue;

            HolderSet<Structure> target = HolderSet.direct(holderOpt.get());

            // 구조물 탐색
            Pair<BlockPos, Holder<Structure>> found =
                    generator.findNearestMapStructure(server, target, origin, radius, false);

            if (found != null) {
                BlockPos pos = found.getFirst();
                double distSq = pos.distSqr(origin);

                if (distSq < nearestDist) {
                    nearestDist = distSq;
                    nearestPos = pos;
                }
            }
        }

        return nearestPos;
    }

    /**
     * ✅ 단일 구조물 탐색 (호환용)
     * 기존 코드와의 하위 호환 유지
     */
    @Nullable
    public static BlockPos findNearest(Level level, String structureKey, BlockPos origin, int radius) {
        return findNearest(level, List.of(structureKey), origin, radius);
    }
}
