package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketChangeSpell;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SpellManageScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/spell_menu.png");
    private static final ResourceLocation BARS_TEXTURE = new ResourceLocation("textures/gui/bars.png");
    private final Screen lastScreen;

    private static final int TEXTURE_WIDTH = 768;
    private static final int TEXTURE_HEIGHT = 544;

    private int displayWidth;
    private int displayHeight;
    private float scale;
    private int leftPos;
    private int topPos;

    private int selectedEquipSlot = 0;
    private int currentPage = 0;

    private long lastClickTime = 0;
    private int lastClickedSlot = -1;

    private final int gridCols = 9;
    private final int gridRowsVisible = 3;
    private final int spellsPerPage = gridCols * gridRowsVisible;

    private final int rawUpperGridX = 32;
    private final int rawUpperGridY = 69;
    private final int rawBottomGridX = 27;
    private final int rawBottomGridY = 448;

    private final int rawSlotSize = 70;
    private final int rawSlotHeight = 92;
    private final int rawIconSize = 50;
    private final int rawIconOffset = 2;

    public SpellManageScreen(Screen lastScreen) {
        super(Component.translatable("gui.genesis.spell_menu.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();

        this.displayHeight = Math.min((int)(this.height * 0.85), TEXTURE_HEIGHT);
        this.scale = (float) this.displayHeight / TEXTURE_HEIGHT;
        this.displayWidth = (int)(TEXTURE_WIDTH * scale);

        this.leftPos = (this.width - this.displayWidth) / 2;
        this.topPos = (this.height - this.displayHeight) / 2;

        int buttonX = this.leftPos + (int)(685 * scale);

        this.addRenderableWidget(Button.builder(Component.literal("▲"), b -> {
            if (this.currentPage > 0) {
                this.currentPage--;
            }
        }).bounds(buttonX, this.topPos + (int)(75 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        this.addRenderableWidget(Button.builder(Component.literal("▼"), b -> {
            List<String> learnedSpells = getPlayerLearnedSpells();
            int maxPage = (int) Math.ceil((double) learnedSpells.size() / spellsPerPage) - 1;

            if (this.currentPage < maxPage) {
                this.currentPage++;
            }
        }).bounds(buttonX, this.topPos + (int)(120 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.onClose();
        }).bounds(this.leftPos + (int)(685 * scale), this.topPos + (int)(285 * scale), (int)(40 * scale), (int)(40 * scale)).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.displayWidth, this.displayHeight, this.displayWidth, this.displayHeight);

        if (this.minecraft.player == null) return;

        List<String> learnedSpells = getPlayerLearnedSpells();

        // 툴팁 대상 마법 ID를 캡처하기 위한 배열 (람다 내부에서 수정하기 위함)
        String[] hoveredSpellId = new String[]{null};

        this.minecraft.player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(spellSlot -> {

            int startIndex = this.currentPage * spellsPerPage;
            int endIndex = Math.min(startIndex + spellsPerPage, learnedSpells.size());

            // --- 상단 상자 (습득한 마법) ---
            for (int i = startIndex; i < endIndex; i++) {
                int indexOnPage = i - startIndex;
                int row = indexOnPage / gridCols;
                int col = indexOnPage % gridCols;

                int slotX = this.leftPos + (int)((rawUpperGridX + (col * rawSlotSize)) * scale);
                int slotY = this.topPos + (int)((rawUpperGridY + (row * rawSlotHeight)) * scale);

                int currentSlotSize = (int)(rawSlotSize * scale);
                int currentIconSize = (int)(rawIconSize * scale);
                int currentIconOffset = (int)(rawIconOffset * scale);

                String spellId = learnedSpells.get(i);
                ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/item/" + spellId + ".png");
                graphics.blit(iconTex, slotX + currentIconOffset, slotY + currentIconOffset, 0, 0, currentIconSize, currentIconSize, currentIconSize, currentIconSize);

                if (mouseX >= slotX && mouseX < slotX + currentSlotSize && mouseY >= slotY && mouseY < slotY + currentSlotSize) {
                    graphics.fill(slotX + currentIconOffset, slotY + currentIconOffset, slotX + currentIconOffset + currentIconSize, slotY + currentIconOffset + currentIconSize, 0x55FFFFFF);
                    // 마우스가 올라간 마법 ID 저장
                    hoveredSpellId[0] = spellId;
                }
            }

            // --- 하단 상자 (장착 슬롯 10칸) ---
            List<String> equipped = spellSlot.getEquippedSpells();
            int maxMemory = spellSlot.getMemoryCapacity();

            for (int i = 0; i < 10; i++) {
                int slotX = this.leftPos + (int)((rawBottomGridX + (i * rawSlotSize)) * scale);
                int slotY = this.topPos + (int)(rawBottomGridY * scale);

                int currentSlotSize = (int)(rawSlotSize * scale);
                int currentIconSize = (int)(rawIconSize * scale);
                int currentIconOffset = (int)(rawIconOffset * scale);

                if (i == this.selectedEquipSlot) {
                    graphics.fill(slotX + currentIconOffset, slotY + currentIconOffset, slotX + currentIconOffset + currentIconSize, slotY + currentIconOffset + currentIconSize, 0x44FFFF00);
                }

                if (i < equipped.size() && equipped.get(i) != null && !equipped.get(i).isEmpty()) {
                    String eqSpellId = equipped.get(i);
                    ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/item/" + eqSpellId + ".png");
                    graphics.blit(iconTex, slotX + currentIconOffset, slotY + currentIconOffset, 0, 0, currentIconSize, currentIconSize, currentIconSize, currentIconSize);

                    // 하단 슬롯 마우스 호버 감지
                    if (mouseX >= slotX && mouseX < slotX + currentSlotSize && mouseY >= slotY && mouseY < slotY + currentSlotSize) {
                        hoveredSpellId[0] = eqSpellId;
                    }
                }
            }

            // 용량 바 렌더링
            int currentCost = 0;
            for (String s : equipped) {
                if (s != null && !s.isEmpty()) {
                    AbstractSpell spell = GenesisSpells.get(s);
                    if (spell != null) {
                        currentCost += spell.getMemoryCost();
                    }
                }
            }
            float costRatio = maxMemory > 0 ? Math.min(1.0F, (float) currentCost / maxMemory) : 0.0F;
            int barX = this.leftPos + (int)(16 * scale);
            int barY = this.topPos + (int)(376 * scale);
            int barWidth = (int)(705 * scale);
            int barHeight = (int)(18 * scale);
            graphics.blit(BARS_TEXTURE, barX, barY, barWidth, barHeight, 0, 60, 182, 5, 256, 256);
            if (costRatio > 0) {
                graphics.blit(BARS_TEXTURE, barX, barY, (int)(barWidth * costRatio), barHeight, 0, 45, (int)(182 * costRatio), 5, 256, 256);
            }
        });

        // 위젯 출력
        super.render(graphics, mouseX, mouseY, partialTick);

        // 툴팁 렌더링 (화면의 맨 마지막에 그려야 다른 UI 뒤로 숨지 않음)
        if (hoveredSpellId[0] != null) {
            drawSpellTooltip(graphics, mouseX, mouseY, hoveredSpellId[0]);
        }
    }

    //  툴팁 생성 및 렌더링을 담당하는 새로운 헬퍼 메서드
    private void drawSpellTooltip(GuiGraphics graphics, int mouseX, int mouseY, String spellId) {
        AbstractSpell spell = GenesisSpells.get(spellId);
        if (spell == null) return;

        List<Component> tooltipLines = new ArrayList<>();
        // 주문 이름
        tooltipLines.add(Component.translatable("spell.genesis." + spellId).withStyle(ChatFormatting.GOLD, ChatFormatting.WHITE));
        tooltipLines.add(Component.empty());
        // 요구 스탯 (마술/기적 구분)
        if (spell instanceof MagicSpell) {
            tooltipLines.add(Component.translatable("tooltip.genesis.spell.req_int", spell.getRequiredStatLevel()).withStyle(ChatFormatting.GRAY));
        } else if (spell instanceof MiracleSpell) {
            tooltipLines.add(Component.translatable("tooltip.genesis.spell.req_faith", spell.getRequiredStatLevel()).withStyle(ChatFormatting.GRAY));
        }
        // 정신력(마나) 소모량
        tooltipLines.add(Component.translatable("tooltip.genesis.spell.mental_cost", String.format("%.1f", spell.getMentalCost())).withStyle(ChatFormatting.GRAY));
        // 슬롯 메모리 차지량
        tooltipLines.add(Component.translatable("tooltip.genesis.spell.memory_cost", spell.getMemoryCost()).withStyle(ChatFormatting.GRAY));
        // 상세 설명
        tooltipLines.add(Component.empty());
        tooltipLines.add(Component.translatable("spell.genesis." + spellId + ".desc").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        // 툴팁 화면에 렌더링
        graphics.renderComponentTooltip(this.font, tooltipLines, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.minecraft.player == null) return super.mouseClicked(mouseX, mouseY, button);

        int clickedRawX = (int)((mouseX - this.leftPos) / scale);
        int clickedRawY = (int)((mouseY - this.topPos) / scale);

        List<String> learnedSpells = getPlayerLearnedSpells();

        if (clickedRawY >= rawBottomGridY && clickedRawY < rawBottomGridY + rawSlotSize) {
            int clickedIdx = (clickedRawX - rawBottomGridX) / rawSlotSize;
            if (clickedIdx >= 0 && clickedIdx < 10) {
                long currentTime = net.minecraft.Util.getMillis();
                if (this.lastClickedSlot == clickedIdx && currentTime - this.lastClickTime < 250) {
                    GenesisNetwork.sendToServer(new PacketChangeSpell(clickedIdx, ""));
                    this.lastClickedSlot = -1;
                } else {
                    this.selectedEquipSlot = clickedIdx;
                    this.lastClickedSlot = clickedIdx;
                    this.lastClickTime = currentTime;
                }
                return true;
            }
        }

        if (clickedRawX >= rawUpperGridX && clickedRawX < rawUpperGridX + (gridCols * rawSlotSize) &&
                clickedRawY >= rawUpperGridY && clickedRawY < rawUpperGridY + (gridRowsVisible * rawSlotHeight)) {
            int col = (clickedRawX - rawUpperGridX) / rawSlotSize;
            int row = (clickedRawY - rawUpperGridY) / rawSlotHeight;
            int clickedSpellIdx = (this.currentPage * spellsPerPage) + (row * gridCols) + col;

            if (clickedSpellIdx >= 0 && clickedSpellIdx < learnedSpells.size()) {
                String targetSpellId = learnedSpells.get(clickedSpellIdx);
                GenesisNetwork.sendToServer(new PacketChangeSpell(this.selectedEquipSlot, targetSpellId));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private List<String> getPlayerLearnedSpells() {
        List<String> learned = new ArrayList<>();
        if (this.minecraft.player != null) {
            this.minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                learned.addAll(stats.getLearnedSpells());
            });
        }
        return learned;
    }

    @Override
    public void onClose() {
        if (this.lastScreen != null) {
            Minecraft.getInstance().setScreen(this.lastScreen);
        } else {
            super.onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}