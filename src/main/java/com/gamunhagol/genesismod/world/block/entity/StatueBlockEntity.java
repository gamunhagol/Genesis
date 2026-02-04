package com.gamunhagol.genesismod.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StatueBlockEntity extends BlockEntity {
    public StatueBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public static class Sentinel extends StatueBlockEntity {
        public Sentinel(BlockPos pos, BlockState state) {
            super(GenesisBlockEntities.SENTINEL_STATUE_BE.get(), pos, state);
        }
    }

    public static class Herald extends StatueBlockEntity {
        public Herald(BlockPos pos, BlockState state) {
            super(GenesisBlockEntities.HERALD_STATUE_BE.get(), pos, state);
        }
    }

    public static class Guide extends StatueBlockEntity {
        public Guide(BlockPos pos, BlockState state) {
            super(GenesisBlockEntities.GUIDE_STATUE_BE.get(), pos, state);
        }
    }
}