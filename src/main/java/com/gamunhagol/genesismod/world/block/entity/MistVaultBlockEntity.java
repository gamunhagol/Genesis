package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import com.gamunhagol.genesismod.world.structure.MistVaultTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MistVaultBlockEntity extends BlockEntity {
    public MistVaultBlockEntity(BlockPos pos, BlockState state) {
        super(GenesisBlockEntities.MIST_VAULT_BE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide() && this.level instanceof ServerLevel serverLevel) {
            MistVaultTracker.get(serverLevel).register(this.worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        if (this.level != null && !this.level.isClientSide() && this.level instanceof ServerLevel serverLevel) {
            MistVaultTracker.get(serverLevel).unregister(this.worldPosition);
        }
        super.setRemoved();
    }
}
