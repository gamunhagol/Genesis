package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpiritBlockFinder {

    /**
     * 플레이어 주변 로드된 청크 반경 내에서 타겟 블록을 최대 maxCount 개수만큼 찾습니다.
     * @return 발견된 블록들의 위치 리스트 (발견하지 못하면 빈 리스트 반환)
     */
    public static List<BlockPos> findNearestBlocksByChunk(ServerLevel level, List<String> blockKeys, BlockPos origin, int chunkRadius, int minY, int maxY, int maxCount) {
        List<Block> targetBlocks = blockKeys.stream()
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .map(ForgeRegistries.BLOCKS::getValue)
                .filter(block -> block != null && block != Blocks.AIR)
                .toList();

        List<BlockPos> foundBlocks = new ArrayList<>();
        if (targetBlocks.isEmpty()) return foundBlocks;

        ChunkPos centerChunk = new ChunkPos(origin);

        for (int d = 0; d <= chunkRadius; d++) {
            boolean foundInLayer = false;

            for (int xOffset = -d; xOffset <= d; xOffset++) {
                for (int zOffset = -d; zOffset <= d; zOffset++) {
                    if (Math.abs(xOffset) == d || Math.abs(zOffset) == d) {
                        int chunkX = centerChunk.x + xOffset;
                        int chunkZ = centerChunk.z + zOffset;

                        if (!level.hasChunk(chunkX, chunkZ)) continue;

                        LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                        int startX = chunk.getPos().getMinBlockX();
                        int startZ = chunk.getPos().getMinBlockZ();

                        for (int bx = 0; bx < 16; bx++) {
                            for (int bz = 0; bz < 16; bz++) {
                                for (int by = minY; by <= maxY; by++) {
                                    BlockPos currentPos = new BlockPos(startX + bx, by, startZ + bz);
                                    BlockState state = chunk.getBlockState(currentPos);

                                    for (Block block : targetBlocks) {
                                        if (state.is(block)) {
                                            foundBlocks.add(currentPos);
                                            foundInLayer = true;

                                            // 목표 개수(5개)를 채우면 즉시 탐색 종료 후 반환
                                            if (foundBlocks.size() >= maxCount) {
                                                return foundBlocks;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (foundInLayer) {
                return foundBlocks;
            }
        }

        return foundBlocks;
    }
}