package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.util.SimpleContainerWrapper;
import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import com.gamunhagol.genesismod.world.block.custom.FadedChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class FadedChestBlockEntity extends BlockEntity implements MenuProvider, IFadedDungeonElement {
    private final ItemStackHandler inventory = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public FadedChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GenesisBlockEntities.FADED_CHEST_BE.get(), pPos, pBlockState);
    }

    @Override
    public void activateElement() {
        if (this.level != null) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(FadedChestBlock.LOCKED, false), 3);
        }
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.level.playSound(null, this.worldPosition,
                    SoundEvents.POWDER_SNOW_BREAK,
                    SoundSource.BLOCKS, 0.8F, 0.5F); // 피치를 낮추면 더 맥빠집니다.
            return true;
        }
        return super.triggerEvent(pId, pType);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.genesis.faded_chest");
    }

    public void startOpen(Player pPlayer) {
        if (!pPlayer.isSpectator() && this.level != null) {
            BlockState state = this.getBlockState();
            if (state.hasProperty(FadedChestBlock.OPEN)) {
                this.level.setBlock(this.worldPosition, state.setValue(FadedChestBlock.OPEN, true), 3);
            }
            this.level.blockEvent(this.worldPosition, state.getBlock(), 1, 1);
        }
    }

    public void stopOpen(Player pPlayer) {
        if (!pPlayer.isSpectator() && this.level != null) {
            BlockState state = this.getBlockState();
            if (state.hasProperty(FadedChestBlock.OPEN)) {
                this.level.setBlock(this.worldPosition, state.setValue(FadedChestBlock.OPEN, false), 3);
            }
            this.level.blockEvent(this.worldPosition, state.getBlock(), 1, 0);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("genesis_inventory", this.inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("genesis_inventory")) {
            this.inventory.deserializeNBT(pTag.getCompound("genesis_inventory"));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int pId, Inventory pInv, Player pPlayer) {
        this.startOpen(pPlayer);
        return ChestMenu.threeRows(pId, pInv, new SimpleContainerWrapper(this.inventory));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            load(tag);
        }
    }
}