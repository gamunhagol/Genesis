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

import java.util.Optional;

public class SpiritStructureFinder {

    @Nullable
    public static BlockPos findNearest(Level level, String structureKey, BlockPos origin, int radius) {
        if (!(level instanceof ServerLevel server)) return null;

        ResourceLocation rl = new ResourceLocation(structureKey);
        Optional<Holder.Reference<Structure>> h =
                server.registryAccess().registryOrThrow(Registries.STRUCTURE)
                        .getHolder(ResourceKey.create(Registries.STRUCTURE, rl));
        if (h.isEmpty()) return null;

        HolderSet<Structure> target = HolderSet.direct(h.get());
        ChunkGenerator gen = server.getChunkSource().getGenerator();

        Pair<BlockPos, Holder<Structure>> nearest =
                gen.findNearestMapStructure(server, target, origin, radius, false);


        return nearest != null ? nearest.getFirst() : null;


    }

}