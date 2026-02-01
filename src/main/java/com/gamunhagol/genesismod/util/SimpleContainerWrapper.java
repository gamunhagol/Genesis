package com.gamunhagol.genesismod.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SimpleContainerWrapper extends SimpleContainer {
    private final IItemHandler handler;

    public SimpleContainerWrapper(IItemHandler handler) {
        // 부모 생성자에 슬롯 개수 전달
        super(handler.getSlots());
        this.handler = handler;

        // 초기화: 핸들러 -> 컨테이너 (복사)
        for (int i = 0; i < handler.getSlots(); i++) {
            // super.setItem을 써서 자체 setChanged() 트리거를 피함
            super.setItem(i, handler.getStackInSlot(i).copy());
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack); // 컨테이너(GUI용) 업데이트
        syncToHandler(slot);        // 즉시 핸들러(실제 저장소) 동기화
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        syncToHandler(slot);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = super.removeItemNoUpdate(slot);
        syncToHandler(slot);
        return stack;
    }

    // [핵심] 특정 슬롯만 딱 집어서 핸들러에 반영하는 메서드
    private void syncToHandler(int slot) {
        if (this.handler instanceof IItemHandlerModifiable modifiable) {
            // 컨테이너의 현재 상태를 핸들러의 해당 슬롯에 직접 꽂아줌
            modifiable.setStackInSlot(slot, this.getItem(slot));
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
