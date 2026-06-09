package com.gamunhagol.genesismod.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class MistVaultTracker extends SavedData {
    private final List<BlockPos> vaultPositions = new ArrayList<>();

    public static MistVaultTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MistVaultTracker::load,
                MistVaultTracker::new,
                "genesis_mist_vaults"
        );
    }

    public void register(BlockPos pos) {
        if (!vaultPositions.contains(pos)) {
            vaultPositions.add(pos.immutable());
            this.setDirty();
        }
    }

    public void unregister(BlockPos pos) {
        if (vaultPositions.remove(pos)) {
            this.setDirty();
        }
    }

    public BlockPos findNearest(BlockPos playerPos) {
        BlockPos closest = null;
        double closestDistSq = Double.MAX_VALUE;

        for (BlockPos pos : vaultPositions) {
            double distSq = pos.distSqr(playerPos);
            if (distSq < closestDistSq) {
                closestDistSq = distSq;
                closest = pos;
            }
        }
        return closest;
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (BlockPos pos : vaultPositions) {
            listTag.add(NbtUtils.writeBlockPos(pos));
        }
        tag.put("VaultPositions", listTag);
        return tag;
    }

    public static MistVaultTracker load(CompoundTag tag) {
        MistVaultTracker tracker = new MistVaultTracker();
        if (tag.contains("VaultPositions", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("VaultPositions", Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                tracker.vaultPositions.add(NbtUtils.readBlockPos(listTag.getCompound(i)));
            }
        }
        return tracker;
    }
}