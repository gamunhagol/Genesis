package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.util.SimpleContainerWrapper;
import com.gamunhagol.genesismod.world.block.FadedChestBlock;
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
    // 27칸 인벤토리
    private final ItemStackHandler inventory = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) { setChanged(); }
    };

    public FadedChestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GenesisBlockEntities.FADED_CHEST_BE.get(), pPos, pBlockState);
    }

    @Override
    public void activateElement() {
        if (this.level != null) {
            // 관문 신호를 받으면 LOCKED를 false로 변경
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(FadedChestBlock.LOCKED, false), 3);
        }
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            // 가루눈 부서지는/밟는 소리
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

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", inventory.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("inventory"));
    }
    public void startOpen(Player pPlayer) {
        if (!pPlayer.isSpectator() && this.level != null) {
            // 창을 열 때 OPEN을 true로 변경
            BlockState state = this.getBlockState();
            if (state.hasProperty(FadedChestBlock.OPEN)) {
                this.level.setBlock(this.worldPosition, state.setValue(FadedChestBlock.OPEN, true), 3);
            }
            this.level.blockEvent(this.worldPosition, state.getBlock(), 1, 1);
        }
    }

    public void stopOpen(Player pPlayer) {
        if (!pPlayer.isSpectator() && this.level != null) {
            // 창을 닫을 때 OPEN을 false로 변경
            BlockState state = this.getBlockState();
            if (state.hasProperty(FadedChestBlock.OPEN)) {
                this.level.setBlock(this.worldPosition, state.setValue(FadedChestBlock.OPEN, false), 3);
            }
            this.level.blockEvent(this.worldPosition, state.getBlock(), 1, 0);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int pId, Inventory pInv, Player pPlayer) {
        this.startOpen(pPlayer); // 열기 시작
        return ChestMenu.threeRows(pId, pInv, new SimpleContainerWrapper(this.inventory) {
            @Override
            public void stopOpen(Player player) {
                super.stopOpen(player);
                FadedChestBlockEntity.this.stopOpen(player); // 닫기 완료
            }
        });
    }
}