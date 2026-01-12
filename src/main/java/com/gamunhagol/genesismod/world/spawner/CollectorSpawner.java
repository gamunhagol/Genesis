package com.gamunhagol.genesismod.world.spawner;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;

public class CollectorSpawner {
    private int spawnDelay = 120000;

    public void tick(ServerLevel level) {
        if (--this.spawnDelay <= 0) {
            this.spawnDelay = 72000; //3일
            attemptSpawn(level);
        }
    }
    private void attemptSpawn(ServerLevel level) {
        Player player = level.getRandomPlayer();
        if (player == null) return;

        // 플레이어 주변 100청크(약 1600블록) 내에서 가장 가까운 마을을 찾습니다.
        // StructureTags.VILLAGE를 사용하면 모든 종류의 마을(평원, 사막 등)을 다 찾습니다.
        BlockPos villagePos = level.findNearestMapStructure(StructureTags.VILLAGE, player.blockPosition(), 10, false);

        if (villagePos != null) {
            // 마을 중심점에서 약간 떨어진 무작위 위치 계산 (마을 바로 한복판 방지)
            int x = villagePos.getX() + level.random.nextInt(21) - 10;
            int z = villagePos.getZ() + level.random.nextInt(21) - 10;
            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
            BlockPos spawnPos = new BlockPos(x, y, z);

            // 징수원 소환
            Collector collector = GenesisEntities.COLLECTOR.get().spawn(level, spawnPos, MobSpawnType.EVENT);

            if (collector != null) {
                collector.setDespawnDelay(48000);

                // 호위병 소환 로직
                for (int i = 0; i < 4; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5D) * 4.0D;
                    double offsetZ = (level.random.nextDouble() - 0.5D) * 4.0D;
                    BlockPos guardPos = spawnPos.offset((int) offsetX, 0, (int) offsetZ);

                    CollectorGuard guard = GenesisEntities.COLLECTOR_GUARD.get().spawn(level, guardPos, MobSpawnType.EVENT);
                }
            }
        }
    }
}
