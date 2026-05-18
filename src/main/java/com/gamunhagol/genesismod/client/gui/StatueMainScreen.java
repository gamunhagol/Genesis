package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketStatueHeal;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StatueMainScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/statue_of_god_main.png");
    private static final ResourceLocation BIG_CIRCLE = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/main_circle.png");

    private static final ResourceLocation BTN_HEAL = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/pray_circle.png");
    private static final ResourceLocation BTN_SPELL = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/spell_circle.png");
    private static final ResourceLocation BTN_SPECUP = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/star_circle.png");

    protected final String statueId;

    private final int textureWidth = 768;
    private final int textureHeight = 544;
    private int displayWidth;
    private int displayHeight;
    private float scale;

    public StatueMainScreen(String statueId) {
        super(Component.translatable("gui.genesis.statue_main.title"));
        this.statueId = statueId;
    }

    @Override
    protected void init() {
        super.init();

        // 유저 창 높이의 85%를 GUI 세로 크기로 잡되, 원본 크기(544)를 넘지 않도록 합니다.
        this.displayHeight = Math.min((int)(this.height * 0.85), textureHeight);
        // 세로 크기 기준으로 크기 비율(scale)을 구합니다.
        this.scale = (float) displayHeight / textureHeight;
        // 구한 비율을 원본 가로 크기에 곱해서 GUI 가로 크기를 결정합니다. (비율 유지)
        this.displayWidth = (int)(textureWidth * scale);
        // 화면 정중앙에 배치하기 위한 시작점(좌상단) X, Y 좌표 계산
        int x = (this.width - displayWidth) / 2;
        int y = (this.height - displayHeight) / 2;

        // 나가기 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.onClose();
        }).bounds(x + (int)(680 * scale), y + (int)(460 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        int btnSize = (int)(64 * scale);


        int cx = 385;       // 삼각형 구도의 가상 중심점 X 좌표 (우측 배치를 위해 550으로 임시 설정)
        int cy = 222;       // 가상 중심점 Y 좌표 (화면 세로 정중앙)
        int w = 80;         // 중심에서 좌우 버튼까지의 거리 (넓이)
        int h = 70;         // 중심에서 위아래 버튼까지의 거리 (높이)
        int halfBtn = 32;   // 버튼 크기(40)의 절반값 (중앙 정렬용 보정값)

        this.addRenderableWidget(new ImageButton(
                x + (int)((cx - halfBtn) * scale),
                y + (int)((cy - h) * scale),
                btnSize, btnSize,
                0, 0, 0,
                BTN_SPECUP, btnSize, btnSize,
                b -> {
                    openChildSpecUpScreen();
                },
                Component.translatable("gui.genesis.button.spec_up")
        ));

        this.addRenderableWidget(new ImageButton(
                x + (int)((cx - w - halfBtn) * scale),
                y + (int)((cy + h) * scale),
                btnSize, btnSize,
                0, 0, 0,
                BTN_SPELL, btnSize, btnSize,
                b -> {
                    Minecraft.getInstance().setScreen(new SpellManageScreen(this));
                },
                Component.translatable("gui.genesis.button.spell_manage")
        ));

        this.addRenderableWidget(new ImageButton(
                x + (int)((cx + w - halfBtn) * scale),
                y + (int)((cy + h) * scale),
                btnSize, btnSize,
                0, 0, 0,
                BTN_HEAL, btnSize, btnSize,
                b -> {
                    GenesisNetwork.sendToServer(new PacketStatueHeal());
                },
                Component.translatable("gui.genesis.button.heal")
        ));
    }

    private void openChildSpecUpScreen() {
        switch (this.statueId) {
            case "god_a":
            case "god_b":
            case "god_c":
            case "god_d":
            case "god_e":
            case "god_f":
            case "god_g":
            case "god_h":
            default:
                Minecraft.getInstance().setScreen(new BlessingScreen(this, this.statueId));
                break;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int x = (this.width - displayWidth) / 2;
        int y = (this.height - displayHeight) / 2;

        // 메인 배경 그리기
        graphics.blit(BACKGROUND, x, y, 0, 0, displayWidth, displayHeight, displayWidth, displayHeight);

        // 가운데 원 좌표 계산 (기존 유지)
        int circleSize = (int)(200 * scale);
        int circleX = x + (this.displayWidth - circleSize) / 2;
        int circleY = y + (this.displayHeight - circleSize) / 2;

        RenderSystem.enableBlend();

        float circleAlpha = 0.5F;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, circleAlpha);

        graphics.blit(BIG_CIRCLE, circleX, circleY, 0, 0, circleSize, circleSize, circleSize, circleSize);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}