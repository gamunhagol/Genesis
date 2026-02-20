package com.gamunhagol.genesismod.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MistVaultBlockEntity extends BlockEntity {
    public MistVaultBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.MIST_VAULT_BE.get(), pos, state);
    }
}
