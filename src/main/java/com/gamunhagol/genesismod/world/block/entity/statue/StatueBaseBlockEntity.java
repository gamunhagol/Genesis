package com.gamunhagol.genesismod.world.block.entity.statue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StatueBaseBlockEntity extends BlockEntity {
    public StatueBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // "fire_god", "water_god" 등 신상 식별자
    public abstract String getStatueId();
}