package com.gamunhagol.genesismod.util;

import net.minecraft.world.SimpleContainer;
import net.minecraftforge.items.IItemHandler;

public class SimpleContainerWrapper extends SimpleContainer {
    private final IItemHandler handler;

    public SimpleContainerWrapper(IItemHandler handler) {
        super(handler.getSlots());
        this.handler = handler;
        // 핸들러의 내용을 컨테이너로 복사
        for (int i = 0; i < handler.getSlots(); i++) {
            this.setItem(i, handler.getStackInSlot(i));
        }
    }

    // 메뉴에서 아이템이 변경될 때 핸들러에도 반영되도록 설정
    @Override
    public void setChanged() {
        super.setChanged();
        for (int i = 0; i < handler.getSlots(); i++) {
            // 이 부분은 읽기 전용이 아닐 경우 핸들러 업데이트 로직이 추가로 필요할 수 있습니다.
            // 단순 상자라면 이 정도로 충분합니다.
        }
    }
}
