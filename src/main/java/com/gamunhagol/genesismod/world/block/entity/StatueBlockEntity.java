package com.gamunhagol.genesismod.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StatueBlockEntity extends BlockEntity {
    public StatueBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.STATUE_BE.get(), pos, state);
    }
}
