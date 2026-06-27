package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class UndeadShardBlockEntity extends BlockEntity {
    private static final List<EntityType<?>> UNDEAD_CACHE = new ArrayList<>();
    private static boolean cacheInitialized = false;

    private static final int MAX_NEARBY_ENTITIES = 15;
    private static final double DETECTION_RADIUS = 6.0;

    private int spawnCooldown = 800;

    public UndeadShardBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.UNDEAD_SHARD_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UndeadShardBlockEntity entity) {
        if (level.isClientSide) return;

        if (!cacheInitialized) {
            buildUndeadCache((ServerLevel) level);
        }

        entity.spawnCooldown--;

        if (entity.spawnCooldown <= 0) {
            spawnRandomUndead((ServerLevel) level, pos);
            entity.spawnCooldown = 500 + level.random.nextInt(500);
            entity.setChanged();
        }
    }

    private static void buildUndeadCache(ServerLevel level) {
        TagKey<EntityType<?>> bossTag = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", "bosses"));

        for (EntityType<?> type : ForgeRegistries.ENTITY_TYPES.getValues()) {
            if (ForgeRegistries.ENTITY_TYPES.tags().getTag(bossTag).contains(type)) {
                continue;
            }

            try {
                Entity dummy = type.create(level);

                if (dummy instanceof Mob mob) {
                    if (mob instanceof net.minecraft.world.entity.boss.wither.WitherBoss) {
                        dummy.discard();
                        continue;
                    }
                    if (mob.getMobType() == MobType.UNDEAD) {
                        UNDEAD_CACHE.add(type);
                    }
                }
                if (dummy != null) {
                    dummy.discard();
                }
            } catch (Exception e) {
                // 예외 무시
            }
        }
        cacheInitialized = true;
    }

    private static void spawnRandomUndead(ServerLevel level, BlockPos pos) {
        if (UNDEAD_CACHE.isEmpty()) return;

        AABB checkArea = new AABB(pos).inflate(DETECTION_RADIUS);
        int currentUndeadCount = level.getEntitiesOfClass(Mob.class, checkArea,
                mob -> mob.getMobType() == MobType.UNDEAD).size();

        if (currentUndeadCount >= MAX_NEARBY_ENTITIES) {
            return;
        }

        int spawnAmount = 1 + level.random.nextInt(5);

        for (int i = 0; i < spawnAmount; i++) {
            if (currentUndeadCount >= MAX_NEARBY_ENTITIES) break;

            EntityType<?> selectedType = UNDEAD_CACHE.get(level.random.nextInt(UNDEAD_CACHE.size()));
            Entity spawnEntity = selectedType.create(level);

            if (spawnEntity instanceof Mob mob) {
                double offsetX = (1.0 + level.random.nextDouble() * 3.0) * (level.random.nextBoolean() ? 1 : -1);
                double offsetZ = (1.0 + level.random.nextDouble() * 3.0) * (level.random.nextBoolean() ? 1 : -1);

                double x = pos.getX() + 0.5 + offsetX;
                double y = pos.getY() + 1.0;
                double z = pos.getZ() + 0.5 + offsetZ;

                mob.setPos(x, y, z);

                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
                level.addFreshEntity(mob);
                level.levelEvent(2004, pos, 0);

                currentUndeadCount++;
            }
        }
    }
}
