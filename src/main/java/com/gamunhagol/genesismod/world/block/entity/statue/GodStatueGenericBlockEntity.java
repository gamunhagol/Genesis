package com.gamunhagol.genesismod.world.block.entity.statue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GodStatueGenericBlockEntity extends StatueBaseBlockEntity {
    // 각각의 신상마다 다르게 가질 고유 ID ("god_a", "god_b" 등)
    private final String statueId;

    // 생성자에서 타입과 ID를 모두 받습니다.
    public GodStatueGenericBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, String statueId) {
        super(type, pos, state);
        this.statueId = statueId;
    }

    @Override
    public String getStatueId() {
        return this.statueId;
    }
}