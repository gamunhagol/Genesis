package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketConfirmLevelUp;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.util.LevelCalcHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class LevelUpScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/level_edit.png");
    private final Screen lastScreen;

    private final int[] pendingIncreases = new int[8];
    private int totalPendingLevels = 0;

    private final int textureSize = 512;
    private int displaySize;
    private float scale;

    public LevelUpScreen(Screen lastScreen) {
        super(Component.translatable("gui.genesis.level_up.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();

        // [화면 크기 설정]
        this.displaySize = Math.min((int)(this.height * 0.85), 512);
        this.scale = (float) displaySize / textureSize;

        int x = (this.width - displaySize) / 2;
        int y = (this.height - displaySize) / 2;

        // 1. 나가기 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            Minecraft.getInstance().setScreen(this.lastScreen);
        }).bounds(x + (int)(456 * scale), y + (int)(450 * scale), (int)(45 * scale), (int)(45 * scale)).build());

        // 2. 취소 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.cancel"), b -> {
                    resetPending();
                    playClickSound();
                }).bounds(x + (int)(11 * scale), y + (int)(459 * scale), (int)(54 * scale), (int)(36 * scale))
                .tooltip(Tooltip.create(Component.translatable("gui.genesis.level_up.cancel"))).build());

        // 3. 완료 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.confirm"), b -> {
                    if (totalPendingLevels > 0) {
                        GenesisNetwork.sendToServer(new PacketConfirmLevelUp(pendingIncreases));
                        playClickSound();
                        Minecraft.getInstance().setScreen(null);
                    }
                }).bounds(x + (int)(99 * scale), y + (int)(459 * scale), (int)(54 * scale), (int)(36 * scale))
                .tooltip(Tooltip.create(Component.translatable("gui.genesis.level_up.confirm"))).build());

        // 4. 스탯 증감 버튼 루프
        int[] yCoords = {87, 126, 165, 204, 243, 282, 321, 360};

        for (int i = 0; i < 8; i++) {
            final int index = i;
            int rowY = y + (int)(yCoords[i] * scale);

            // [ > ] 증가 버튼 (키 값 통일: increase)
            this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.increase"), b -> {
                if (canAffordNextPendingLevel()) {
                    pendingIncreases[index]++;
                    totalPendingLevels++;
                    playClickSound();
                } else {
                    playErrorSound();
                    if (this.minecraft.player != null) {
                        this.minecraft.player.displayClientMessage(
                                Component.translatable("message.genesis.level_up.not_enough_xp"), true
                        );
                    }
                }
            }).bounds(x + (int)(117 * scale), rowY, (int)(21 * scale), (int)(21 * scale)).build());

            // [ < ] 감소 버튼 (키 값 통일: decrease)
            this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.decrease"), b -> {
                if (pendingIncreases[index] > 0) {
                    pendingIncreases[index]--;
                    totalPendingLevels--;
                    playClickSound();
                }
            }).bounds(x + (int)(85 * scale), rowY, (int)(21 * scale), (int)(21 * scale)).build());
        }
    }

    private boolean canAffordNextPendingLevel() {
        if (this.minecraft.player == null) return false;

        return this.minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).map(stats -> {
            int baseLevel = LevelCalcHelper.getCharacterLevel(stats);
            int currentTotalXp = LevelCalcHelper.getPlayerTotalXp(this.minecraft.player);

            int totalPendingCost = 0;
            for (int i = 0; i < totalPendingLevels; i++) {
                totalPendingCost += LevelCalcHelper.getXpCostForNextLevel(baseLevel + i);
            }

            int nextLevelCost = LevelCalcHelper.getXpCostForNextLevel(baseLevel + totalPendingLevels);
            return currentTotalXp >= (totalPendingCost + nextLevelCost);
        }).orElse(false);
    }

    private void playClickSound() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private void playErrorSound() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BASS, 1.0F));
    }

    private void resetPending() {
        for (int i = 0; i < 8; i++) pendingIncreases[i] = 0;
        totalPendingLevels = 0;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int x = (this.width - displaySize) / 2;
        int y = (this.height - displaySize) / 2;

        graphics.blit(BACKGROUND, x, y, 0, 0, displaySize, displaySize, displaySize, displaySize);

        renderStatsInfo(graphics, x, y);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderStatsInfo(GuiGraphics graphics, int x, int y) {
        if (this.minecraft.player == null) return;

        this.minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            int textColor = 0xFFFFFF;
            int baseLevel = LevelCalcHelper.getCharacterLevel(stats);
            int currentTotalXp = LevelCalcHelper.getPlayerTotalXp(this.minecraft.player);

            int totalCost = 0;
            for(int i = 0; i < totalPendingLevels; i++) {
                totalCost += LevelCalcHelper.getXpCostForNextLevel(baseLevel + i);
            }

            // [상단] 현재 레벨 (Lv. X)
            int levelTextY = y + (int)(15 * scale);
            graphics.drawString(this.font, "Lv. " + (baseLevel + totalPendingLevels), x + (int)(15 * scale), levelTextY, textColor, false);

            // 회수된(추가된) 레벨 표시
            // 현재 레벨 아래에 약 12픽셀 간격을 두고 표시합니다.
            if (totalPendingLevels > 0) {
                graphics.drawString(this.font,
                        Component.translatable("gui.genesis.level_up.recovered_levels", totalPendingLevels),
                        x + (int)(15 * scale), levelTextY + (int)(12 * scale), 0xFFFF00, false); // 노란색으로 강조
            }

            // [좌측] 스탯 렌더링
            int[] baseValues = {stats.getVigor(), stats.getMind(), stats.getEndurance(), stats.getStrength(), stats.getDexterity(), stats.getIntelligence(), stats.getFaith(), stats.getArcane()};
            int[] yCoords = {87, 126, 165, 204, 243, 282, 321, 360};

            for (int i = 0; i < 8; i++) {
                int rowY = y + (int)(yCoords[i] * scale) + 4;
                int totalStat = baseValues[i] + pendingIncreases[i];
                int color = (pendingIncreases[i] > 0) ? 0xFFFF00 : 0xFFFFFF;

                Component statName = Component.translatable("stat.genesis." + StatType.values()[i].getName());
                graphics.drawString(this.font, statName, x + (int)(15 * scale), rowY, textColor, false);
                graphics.drawString(this.font, totalStat + "/99", x + (int)(155 * scale), rowY, color, false);
            }

            // [우측 상단] 상태 정보
            int statusX = x + (int)(255 * scale);
            int statusY = y + (int)(25 * scale);

            graphics.drawString(this.font, Component.translatable("gui.genesis.level_up.hp", (int)this.minecraft.player.getMaxHealth()), statusX, statusY, textColor, false);
            double stamina = this.minecraft.player.getAttributeValue(EpicFightAttributes.MAX_STAMINA.get());
            graphics.drawString(this.font, Component.translatable("gui.genesis.level_up.stamina", (int)stamina), statusX, statusY + 15, textColor, false);
            graphics.drawString(this.font, Component.translatable("gui.genesis.level_up.mana", (int)stats.getMental(), (int)stats.getMaxMental()), statusX, statusY + 30, textColor, false);

            // [하단] 경험치 정보
            int xpInfoX = x + (int)(25 * scale);
            int xpInfoY = y + (int)(425 * scale);

            graphics.drawString(this.font, Component.translatable("gui.genesis.level_up.current_xp", currentTotalXp), xpInfoX, xpInfoY - 12, textColor, false);
            int costColor = (currentTotalXp >= totalCost) ? 0xFFFF00 : 0xFF5555;
            graphics.drawString(this.font, Component.translatable("gui.genesis.level_up.required_xp", totalCost), xpInfoX, xpInfoY, costColor, false);
        });
    }
}