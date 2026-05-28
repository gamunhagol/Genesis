package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class MistVaultTracker {
    // 디멘션(차원)과 좌표를 함께 저장하기 위한 간단한 기록용 레코드
    public record VaultLocation(ResourceKey<Level> dimension, BlockPos pos) {}

    // 전 세계에 설치된 모든 안개 보관소의 주소록 리스트
    private static final List<VaultLocation> VAULT_LIST = new ArrayList<>();

    // 블록이 설치될 때 주소록에 등록
    public static void register(Level level, BlockPos pos) {
        VaultLocation loc = new VaultLocation(level.dimension(), pos.immutable());
        if (!VAULT_LIST.contains(loc)) {
            VAULT_LIST.add(loc);
        }
    }

    // 블록이 파괴될 때 주소록에서 삭제
    public static void unregister(Level level, BlockPos pos) {
        VAULT_LIST.remove(new VaultLocation(level.dimension(), pos));
    }

    // 현재 플레이어와 가장 가까운 보관소 좌표 찾기 (거리 무제한, 렉 0%)
    public static BlockPos findNearest(Level level, BlockPos playerPos) {
        ResourceKey<Level> currentDim = level.dimension();
        BlockPos closest = null;
        double closestDistSq = Double.MAX_VALUE;

        for (VaultLocation loc : VAULT_LIST) {
            // 플레이어와 같은 차원(오버월드 등)에 있는 블록만 계산
            if (loc.dimension().equals(currentDim)) {
                double distSq = loc.pos().distSqr(playerPos);
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closest = loc.pos();
                }
            }
        }
        return closest;
    }
}