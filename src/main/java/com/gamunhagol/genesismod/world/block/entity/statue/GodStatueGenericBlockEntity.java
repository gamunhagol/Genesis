package com.gamunhagol.genesismod.world.block.entity.statue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GodStatueGenericBlockEntity extends StatueBaseBlockEntity {
    private final String statueId;

    public GodStatueGenericBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, String statueId) {
        super(type, pos, state);
        this.statueId = statueId;
    }

    @Override
    public String getStatueId() {
        return this.statueId;
    }
}