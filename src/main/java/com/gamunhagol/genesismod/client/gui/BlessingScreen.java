package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BlessingScreen extends Screen {
    // 배경 이미지 (가장 기본적인 256x256 사이즈 하나만 준비해서 돌려쓰셔도 됩니다)
    private static final ResourceLocation BACKGROUND = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/starry_path.png");

    private final Screen lastScreen;
    private final String statueId;

    public BlessingScreen(Screen lastScreen, String statueId) {
        super(Component.literal("Statue Spec-Up"));
        this.lastScreen = lastScreen;
        this.statueId = statueId;
    }

    @Override
    protected void init() {
        super.init();

        // 1. 뒤로 가기 버튼 (화면 하단 중앙)
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 배경 어둡게 덮기
        this.renderBackground(graphics);

        // 2. 배경 이미지 그리기 (화면 중앙에 256x256 고정 크기로 렌더링)
        int x = (this.width - 256) / 2;
        int y = (this.height - 256) / 2;
        graphics.blit(BACKGROUND, x, y, 0, 0, 256, 256);

        // 3. 현재 신상 ID 출력 (텍스트가 나오면 정상 연동된 것!)
        graphics.drawCenteredString(this.font, "Statue ID: " + this.statueId, this.width / 2, y + 20, 0xFFFFFF);
        graphics.drawCenteredString(this.font, " " + this.statueId , this.width / 2, y + 40, 0x00FF00);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
