package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketChangeSpell;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
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
    private static final ResourceLocation LOCKED_SLOT_ICON = new ResourceLocation("textures/gui/widgets.png");
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

    // 더블 클릭 감지용 변수
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

        // 이전 페이지 버튼 (▲)
        this.addRenderableWidget(Button.builder(Component.literal("▲"), b -> {
            if (this.currentPage > 0) {
                this.currentPage--;
            }
        }).bounds(buttonX, this.topPos + (int)(75 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        // 다음 페이지 버튼 (▼)
        this.addRenderableWidget(Button.builder(Component.literal("▼"), b -> {
            List<String> learnedSpells = getPlayerLearnedSpells();
            int maxPage = (int) Math.ceil((double) learnedSpells.size() / spellsPerPage) - 1;

            if (this.currentPage < maxPage) {
                this.currentPage++;
            }
        }).bounds(buttonX, this.topPos + (int)(120 * scale), (int)(40 * scale), (int)(40 * scale)).build());

        // 나가기 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.onClose();
        }).bounds(this.leftPos + (int)(685 * scale), this.topPos + (int)(285 * scale), (int)(40 * scale), (int)(40 * scale)).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // 1. 배경 프레임 그리기
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.displayWidth, this.displayHeight, this.displayWidth, this.displayHeight);

        if (this.minecraft.player == null) return;

        List<String> learnedSpells = getPlayerLearnedSpells();

        this.minecraft.player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(spellSlot -> {

            int startIndex = this.currentPage * spellsPerPage;
            int endIndex = Math.min(startIndex + spellsPerPage, learnedSpells.size());

            // 2. 상단 9x3 마법 상자 영역 아이콘 그리기 (습득한 마법)
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
                ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/gui/spell/icon_" + spellId + ".png");
                graphics.blit(iconTex, slotX + currentIconOffset, slotY + currentIconOffset, 0, 0, currentIconSize, currentIconSize, currentIconSize, currentIconSize);

                if (mouseX >= slotX && mouseX < slotX + currentSlotSize && mouseY >= slotY && mouseY < slotY + currentSlotSize) {
                    graphics.fill(slotX, slotY, slotX + currentSlotSize, slotY + currentSlotSize, 0x55FFFFFF);
                }
            }

// 3. 하단 장착 슬롯 10칸 그리기
            List<String> equipped = spellSlot.getEquippedSpells();
            int maxSlots = spellSlot.getMaxSlots(); // 플레이어의 현재 최대 해금 슬롯 수

            for (int i = 0; i < 10; i++) {
                int slotX = this.leftPos + (int)((rawBottomGridX + (i * rawSlotSize)) * scale);
                int slotY = this.topPos + (int)(rawBottomGridY * scale);

                int currentSlotSize = (int)(rawSlotSize * scale);
                int currentIconSize = (int)(rawIconSize * scale);
                int currentIconOffset = (int)(rawIconOffset * scale);

                // [복구 핵심] 현재 슬롯이 해금된 범위(maxSlots 미만)인지 먼저 확인합니다.
                if (i < maxSlots) {
                    // 1. 현재 선택된 슬롯이라면 안쪽 핏에 맞춰 노란색 박스 강조
                    if (i == this.selectedEquipSlot) {
                        graphics.fill(
                                slotX + currentIconOffset,
                                slotY + currentIconOffset,
                                slotX + currentIconOffset + currentIconSize,
                                slotY + currentIconOffset + currentIconSize,
                                0x44FFFF00);
                    }

                    // 2. 마법이 장착되어 있다면 아이콘 출력 (선택 여부와 관계없이 해금된 칸이면 그려야 함)
                    if (i < equipped.size() && equipped.get(i) != null && !equipped.get(i).isEmpty()) {
                        String eqSpellId = equipped.get(i);
                        ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/gui/spell/icon_" + eqSpellId + ".png");
                        graphics.blit(iconTex, slotX + currentIconOffset, slotY + currentIconOffset, 0, 0, currentIconSize, currentIconSize, currentIconSize, currentIconSize);
                    }
                    // 장착된 마법이 없는 빈 슬롯은 아무것도 그리지 않아 배경 이미지(투명 빈칸)가 그대로 보입니다.
                }
                // [해금 안 됨] 최대 해금 수치를 넘어선 나머지 슬롯들만 자물쇠로 막기
                else {
                    graphics.blit(LOCKED_SLOT_ICON,
                            slotX + currentIconOffset, slotY + currentIconOffset,
                            currentIconSize, currentIconSize,
                            0F, 186F,
                            20, 20,
                            256, 256
                    );
                }
            }
        });

        // 4. 위젯 출력
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.minecraft.player == null) return super.mouseClicked(mouseX, mouseY, button);

        int clickedRawX = (int)((mouseX - this.leftPos) / scale);
        int clickedRawY = (int)((mouseY - this.topPos) / scale);

        List<String> learnedSpells = getPlayerLearnedSpells();

        // [클릭 처리 1] 하단 10칸 슬롯 클릭 시 (최대 슬롯 제한 및 더블 클릭 해제)
        if (clickedRawY >= rawBottomGridY && clickedRawY < rawBottomGridY + rawSlotSize) {
            int clickedIdx = (clickedRawX - rawBottomGridX) / rawSlotSize;

            int maxSlots = this.minecraft.player.getCapability(SpellSlotProvider.SPELL_SLOT)
                    .map(cap -> cap.getMaxSlots())
                    .orElse(0);

            // 자물쇠 칸은 클릭 무시
            if (clickedIdx >= 0 && clickedIdx < maxSlots) {
                long currentTime = net.minecraft.Util.getMillis();

                // 더블 클릭 감지 (250ms 이내) -> 패킷으로 빈 문자열을 보내 마법 해제
                if (this.lastClickedSlot == clickedIdx && currentTime - this.lastClickTime < 250) {
                    GenesisNetwork.sendToServer(new PacketChangeSpell(clickedIdx, ""));
                    this.lastClickedSlot = -1;
                } else {
                    // 일반 클릭 -> 슬롯 선택
                    this.selectedEquipSlot = clickedIdx;
                    this.lastClickedSlot = clickedIdx;
                    this.lastClickTime = currentTime;
                }
                return true;
            }
        }

        // [클릭 처리 2] 상단 9x3 상자 안의 주문 클릭 시 -> 아래 선택된 슬롯에 각인(패킷 전송)
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

    // 데이터 연동용 헬퍼 메서드 (실제 플레이어 스탯 연동 완료)
    private List<String> getPlayerLearnedSpells() {
        List<String> learned = new ArrayList<>();

        if (this.minecraft.player != null) {
            this.minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                // StatCapability에서 실제로 배운 마법 목록을 가져옵니다.
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