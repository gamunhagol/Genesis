package com.gamunhagol.genesismod.world.block.entity.statue;

import com.gamunhagol.genesismod.world.block.entity.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GodAStatueBlockEntity extends StatueBaseBlockEntity {

    public GodAStatueBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.GOD_STATUE_A_BE.get(), pos, state);
    }

    @Override
    public String getStatueId() {
        // UI에서 구분할 고유 ID (예: "god_a")
        return "god_a";
    }
}