package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.util.ZombieInfectionHandler;
import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UndeadShardBlockEntity extends BlockEntity {

    private static final double DETECTION_RADIUS = 32.0D;

    public UndeadShardBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.UNDEAD_SHARD_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UndeadShardBlockEntity entity) {
        if (level.isClientSide) return;

        if (level.getGameTime() % 20 == 0) {
            if (!isExposedToAir(level, pos)) {
                return;
            }

            AABB checkArea = new AABB(pos).inflate(DETECTION_RADIUS);
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    checkArea,
                    ZombieInfectionHandler::canBeInfected
            );

            for (LivingEntity target : targets) {
                ZombieInfectionHandler.infect(target);
            }
        }
    }

    private static boolean isExposedToAir(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                return true;
            }
        }
        return false;
    }
}
