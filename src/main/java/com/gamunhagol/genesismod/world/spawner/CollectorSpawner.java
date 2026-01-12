package com.gamunhagol.genesismod.world.spawner;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
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
    private void attemptSpawn(ServerLevel level){
        Player player = level.getRandomPlayer();
        if (player == null) return;

        BlockPos playerPos = player.blockPosition();
        int x = playerPos.getX() + level.random.nextInt(31) - 15;
        int z = playerPos.getZ() + level.random.nextInt(31) - 15;
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        BlockPos pos = new BlockPos(x, y, z);

        Collector collector = GenesisEntities.COLLECTOR.get().spawn(level, pos, MobSpawnType.EVENT);


        if (collector != null) {
            collector.setDespawnDelay(48000);


            // (호위병 로직)
            for (int i = 0; i < 4; i++) {
                double offsetX = (level.random.nextDouble() - 0.5D) * 4.0D;
                double offsetZ = (level.random.nextDouble() - 0.5D) * 4.0D;
                BlockPos guardPos = pos.offset((int)offsetX, 0, (int)offsetZ);

                //  소환
                CollectorGuard guard =
                        GenesisEntities.COLLECTOR_GUARD.get().spawn(level, guardPos, MobSpawnType.EVENT);

                if (guard != null) {
                }
            }
        }
    }
}
