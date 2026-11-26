package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LightStoneBlockEntity extends BlockEntity {

    private static final Map<ResourceKey<Level>, Map<BlockPos, Integer>> ACTIVE_STONES = new ConcurrentHashMap<>();

    public static final Set<EntityType<?>> EXTRA_PURIFY_TARGETS = new HashSet<>(Set.of(
            EntityType.CREEPER
    ));

    private static final TagKey<Block> MC_STONE_TAG = BlockTags.create(new ResourceLocation("minecraft", "stone"));
    private static final TagKey<Block> FORGE_STONE_TAG = BlockTags.create(new ResourceLocation("forge", "stone"));

    private static final int SCAN_INTERVAL = 6000;
    private static final int PURIFY_INTERVAL = 1200;
    private static final double PLAYER_AWARENESS_RANGE = 128.0;

    private int tier = 0;
    private int scanTimer = 0;
    private int purifyTimer = 0;
    private boolean initialized = false;

    public LightStoneBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.LIGHT_STONE_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LightStoneBlockEntity be) {
        if (level.isClientSide()) return;

        if (!be.initialized) {
            be.updateStructureTier();
            be.initialized = true;
        }

        be.scanTimer++;
        if (be.scanTimer >= SCAN_INTERVAL) {
            be.scanTimer = 0;
            be.updateStructureTier();
        }

        if (be.tier > 0) {
            be.purifyTimer++;
            if (be.purifyTimer >= PURIFY_INTERVAL) {
                be.purifyTimer = 0;
                be.purifyNearbyMonsters();
            }
        }
    }

    public void updateStructureTier() {
        if (this.level == null || this.level.isClientSide()) return;

        int stoneCount1 = 0;
        int stoneCount2 = 0;
        int stoneCount3 = 0;

        for (BlockPos targetPos : BlockPos.betweenClosed(this.worldPosition.offset(-3, -3, -3), this.worldPosition.offset(3, 3, 3))) {
            if (targetPos.equals(this.worldPosition)) continue;

            BlockState targetState = this.level.getBlockState(targetPos);
            if (targetState.is(MC_STONE_TAG) || targetState.is(FORGE_STONE_TAG)) {
                int dx = Math.abs(targetPos.getX() - this.worldPosition.getX());
                int dy = Math.abs(targetPos.getY() - this.worldPosition.getY());
                int dz = Math.abs(targetPos.getZ() - this.worldPosition.getZ());
                int maxDist = Math.max(dx, Math.max(dy, dz));

                if (maxDist <= 1) stoneCount1++;
                if (maxDist <= 2) stoneCount2++;
                if (maxDist <= 3) stoneCount3++;
            }
        }

        int newTier = 0;
        if (stoneCount3 >= 150) {
            newTier = 3;
        } else if (stoneCount2 >= 75) {
            newTier = 2;
        } else if (stoneCount1 >= 18) {
            newTier = 1;
        }

        if (this.tier != newTier) {
            this.tier = newTier;
            setChanged();
        }

        updateCache();
    }

    private void purifyNearbyMonsters() {
        if (this.level == null || this.tier <= 0) return;

        double purifyRadius = getSpawnBlockRadius() * 0.5;
        int maxTargets = getMaxPurifyTargets();

        AABB searchBox = new AABB(this.worldPosition).inflate(purifyRadius);
        double radiusSq = purifyRadius * purifyRadius;

        List<Monster> candidates = this.level.getEntitiesOfClass(Monster.class, searchBox, monster -> {
            if (!monster.isAlive()) return false;
            if (monster.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) > radiusSq) {
                return false;
            }

            boolean isTargetType = (monster.getMobType() == MobType.UNDEAD) || EXTRA_PURIFY_TARGETS.contains(monster.getType());
            if (!isTargetType) return false;

            if (this.level.getBrightness(LightLayer.SKY, monster.blockPosition()) <= 0) {
                return false;
            }

            return this.level.hasNearbyAlivePlayer(monster.getX(), monster.getY(), monster.getZ(), PLAYER_AWARENESS_RANGE);
        });

        if (candidates.isEmpty()) return;

        candidates.sort(Comparator.comparingDouble(m -> m.distanceToSqr(
                this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5)));

        int count = 0;
        for (Monster target : candidates) {
            if (count >= maxTargets) break;
            target.setSecondsOnFire(8);
            count++;
        }
    }

    public int getSpawnBlockRadius() {
        return switch (this.tier) {
            case 1 -> 20;
            case 2 -> 120;
            case 3 -> 640;
            default -> 0;
        };
    }

    public int getMaxPurifyTargets() {
        return switch (this.tier) {
            case 1 -> 2;
            case 2 -> 6;
            case 3 -> 10;
            default -> 0;
        };
    }

    private void updateCache() {
        if (this.level == null || this.level.isClientSide()) return;
        ResourceKey<Level> dim = this.level.dimension();
        if (this.tier > 0) {
            ACTIVE_STONES.computeIfAbsent(dim, k -> new ConcurrentHashMap<>())
                    .put(this.worldPosition.immutable(), getSpawnBlockRadius());
        } else {
            removeFromCache();
        }
    }

    private void removeFromCache() {
        if (this.level == null) return;
        Map<BlockPos, Integer> map = ACTIVE_STONES.get(this.level.dimension());
        if (map != null) {
            map.remove(this.worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        removeFromCache();
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        removeFromCache();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Tier", this.tier);
        tag.putInt("ScanTimer", this.scanTimer);
        tag.putInt("PurifyTimer", this.purifyTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tier = tag.getInt("Tier");
        this.scanTimer = tag.getInt("ScanTimer");
        this.purifyTimer = tag.getInt("PurifyTimer");
        this.initialized = false;
    }

    public static boolean isSpawnBlocked(Level level, BlockPos spawnPos) {
        Map<BlockPos, Integer> map = ACTIVE_STONES.get(level.dimension());
        if (map == null || map.isEmpty()) return false;

        for (Map.Entry<BlockPos, Integer> entry : map.entrySet()) {
            BlockPos stonePos = entry.getKey();
            int radius = entry.getValue();
            if (stonePos.distSqr(spawnPos) <= (double) radius * radius) {
                return true;
            }
        }
        return false;
    }
}
