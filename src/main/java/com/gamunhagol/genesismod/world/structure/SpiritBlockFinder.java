package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class SpiritBlockFinder {

    public static List<BlockPos> findNearestBlocksByChunk(ServerLevel level, List<String> blockKeys, BlockPos origin,
                                                          int chunkRadius, int minY, int maxY, int maxCount) {
        Set<Block> targetBlocks = new HashSet<>();
        blockKeys.stream()
                .map(String::trim)
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .map(ForgeRegistries.BLOCKS::getValue)
                .filter(block -> block != null && block != Blocks.AIR)
                .forEach(targetBlocks::add);

        if (targetBlocks.isEmpty()) return new ArrayList<>();

        Predicate<BlockState> matcher = state -> targetBlocks.contains(state.getBlock());

        int lowY = Math.max(minY, level.getMinBuildHeight());
        int highY = Math.min(maxY, level.getMaxBuildHeight() - 1);

        ChunkPos centerChunk = new ChunkPos(origin);

        for (int d = 0; d <= chunkRadius; d++) {
            List<BlockPos> foundInLayer = new ArrayList<>();

            for (int xOffset = -d; xOffset <= d; xOffset++) {
                for (int zOffset = -d; zOffset <= d; zOffset++) {
                    if (Math.abs(xOffset) != d && Math.abs(zOffset) != d) continue;

                    int chunkX = centerChunk.x + xOffset;
                    int chunkZ = centerChunk.z + zOffset;

                    if (!level.hasChunk(chunkX, chunkZ)) continue;

                    LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                    scanChunk(chunk, matcher, lowY, highY, foundInLayer);
                }
            }

            if (!foundInLayer.isEmpty()) {
                foundInLayer.sort(Comparator.comparingDouble(p -> p.distSqr(origin)));
                return foundInLayer.size() > maxCount
                        ? new ArrayList<>(foundInLayer.subList(0, maxCount))
                        : foundInLayer;
            }
        }

        return new ArrayList<>();
    }

    private static void scanChunk(LevelChunk chunk, Predicate<BlockState> matcher,
                                  int lowY, int highY, List<BlockPos> out) {
        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();
        LevelChunkSection[] sections = chunk.getSections();

        for (int i = 0; i < sections.length; i++) {
            LevelChunkSection section = sections[i];
            if (section == null || section.hasOnlyAir()) continue;

            int sectionMinY = chunk.getSectionYFromSectionIndex(i) << 4;
            int sectionMaxY = sectionMinY + 15;
            if (sectionMaxY < lowY || sectionMinY > highY) continue;

            if (!section.maybeHas(matcher)) continue;

            int yStart = Math.max(sectionMinY, lowY) - sectionMinY;
            int yEnd = Math.min(sectionMaxY, highY) - sectionMinY;

            for (int ly = yStart; ly <= yEnd; ly++) {
                for (int lz = 0; lz < 16; lz++) {
                    for (int lx = 0; lx < 16; lx++) {
                        if (matcher.test(section.getBlockState(lx, ly, lz))) {
                            out.add(new BlockPos(startX + lx, sectionMinY + ly, startZ + lz));
                        }
                    }
                }
            }
        }
    }
}
