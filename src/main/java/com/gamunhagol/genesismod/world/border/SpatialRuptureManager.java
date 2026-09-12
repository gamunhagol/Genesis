package com.gamunhagol.genesismod.world.border;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpatialRuptureManager {
    public static final List<RuptureZone> ACTIVE_ZONES = new ArrayList<>();

    public static class RuptureZone {
        public final ResourceLocation dimension;
        public final double centerX, centerZ;
        public final double minX, maxX;
        public final double minZ, maxZ;
        public final long expireGameTime;
        private final VoxelShape wallShape;

        public RuptureZone(ResourceLocation dimension, double centerX, double centerZ, double radius, long expireGameTime) {
            this.dimension = dimension;
            this.centerX = centerX;
            this.centerZ = centerZ;
            this.minX = centerX - radius;
            this.maxX = centerX + radius;
            this.minZ = centerZ - radius;
            this.maxZ = centerZ + radius;
            this.expireGameTime = expireGameTime;

            VoxelShape outer = Shapes.create(new AABB(minX, -64.0D, minZ, maxX, 320.0D, maxZ));
            VoxelShape inner = Shapes.create(new AABB(minX + 0.2D, -64.0D, minZ + 0.2D, maxX - 0.2D, 320.0D, maxZ - 0.2D));
            this.wallShape = Shapes.join(outer, inner, BooleanOp.ONLY_FIRST);
        }

        public VoxelShape getWallShape() {
            return wallShape;
        }
    }

    public static void addZone(ResourceLocation dimension, double x, double z, double radius, long expireGameTime) {
        for (RuptureZone existing : ACTIVE_ZONES) {
            if (existing.dimension.equals(dimension)
                    && Math.abs(existing.centerX - x) < 0.001D
                    && Math.abs(existing.centerZ - z) < 0.001D
                    && existing.expireGameTime == expireGameTime) {
                return;
            }
        }

        ACTIVE_ZONES.add(new RuptureZone(dimension, x, z, radius, expireGameTime));
    }

    public static void serverTick(MinecraftServer server) {
        if (ACTIVE_ZONES.isEmpty()) return;

        long currentGameTime = server.overworld().getGameTime();
        Iterator<RuptureZone> it = ACTIVE_ZONES.iterator();

        while (it.hasNext()) {
            RuptureZone zone = it.next();

            if (currentGameTime >= zone.expireGameTime) {
                ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, zone.dimension);
                ServerLevel targetLevel = server.getLevel(dimKey);

                if (targetLevel != null) {
                    targetLevel.playSound(
                            null,
                            zone.centerX,
                            targetLevel.getSeaLevel(),
                            zone.centerZ,
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.PLAYERS,
                            0.4F,
                            1.2F
                    );
                }
                it.remove();
            }
        }
    }

    public static void clientTick(Level level) {
        if (ACTIVE_ZONES.isEmpty()) return;

        long currentGameTime = level.getGameTime();
        ResourceLocation currentDim = level.dimension().location();

        Iterator<RuptureZone> it = ACTIVE_ZONES.iterator();
        while (it.hasNext()) {
            RuptureZone zone = it.next();
            if (zone.dimension.equals(currentDim) && currentGameTime >= zone.expireGameTime) {
                it.remove();
            }
        }
    }

    public static void clear() {
        ACTIVE_ZONES.clear();
    }
}