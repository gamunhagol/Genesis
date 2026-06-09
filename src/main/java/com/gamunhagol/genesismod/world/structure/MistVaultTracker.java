package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class MistVaultTracker {
    public record VaultLocation(ResourceKey<Level> dimension, BlockPos pos) {}

    private static final List<VaultLocation> VAULT_LIST = new ArrayList<>();

    public static void register(Level level, BlockPos pos) {
        VaultLocation loc = new VaultLocation(level.dimension(), pos.immutable());
        if (!VAULT_LIST.contains(loc)) {
            VAULT_LIST.add(loc);
        }
    }

    public static void unregister(Level level, BlockPos pos) {
        VAULT_LIST.remove(new VaultLocation(level.dimension(), pos));
    }

    public static BlockPos findNearest(Level level, BlockPos playerPos) {
        ResourceKey<Level> currentDim = level.dimension();
        BlockPos closest = null;
        double closestDistSq = Double.MAX_VALUE;

        for (VaultLocation loc : VAULT_LIST) {
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