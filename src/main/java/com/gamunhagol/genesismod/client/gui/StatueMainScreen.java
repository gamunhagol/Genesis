package com.gamunhagol.genesismod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StatueMainScreen extends Screen {
    protected final String statueId;

    public StatueMainScreen(String statueId) {
        super(Component.translatable("gui.genesis.statue_main"));
        this.statueId = statueId;
    }

    @Override
    protected void init() {
        super.init();
        // [구조 설계]
        // 1. 회복 버튼 -> 서버 패킷 전송
        // 2. 주문 슬롯 버튼 -> SpellManageScreen(공통)으로 전환
        // 3. 스팩업 버튼 -> 이 statueId에 따른 세부 스팩업 Screen(비공통)으로 전환
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        // 여기서 statueId에 따라 배경 이미지를 다르게 렌더링할 수 있습니다.
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}