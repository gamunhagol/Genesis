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
        this.displayHeight = Math.min((int)(this.height * 0.85), textureHeight);
        this.scale = (float) displayHeight / textureHeight;
        this.displayWidth = (int)(textureWidth * scale);
        int x = (this.width - displayWidth) / 2;
        int y = (this.height - displayHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.onClose();
        }).bounds(x + (int)(680 * scale), y + (int)(460 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        int btnSize = (int)(64 * scale);


        int cx = 385;
        int cy = 222;
        int w = 80;
        int h = 70;
        int halfBtn = 32;

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

        graphics.blit(BACKGROUND, x, y, 0, 0, displayWidth, displayHeight, displayWidth, displayHeight);

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