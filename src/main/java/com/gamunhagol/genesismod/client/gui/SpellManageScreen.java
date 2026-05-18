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
    private final Screen lastScreen;

    // 이미지 파일 원본 크기
    private final int imageWidth = 256;
    private final int imageHeight = 192;
    private int leftPos;
    private int topPos;

    // 제어 변수
    private int selectedEquipSlot = 0; // 하단 10칸 중 선택된 슬롯 (0~9)
    private int scrollRowOffset = 0;   // 위/아래 버튼으로 제어할 현재 줄 위치

    // 9x3 그리드 설정
    private final int gridCols = 9;
    private final int gridRowsVisible = 3; // 눈에 보이는 줄 수
    private final int slotSize = 22;       // 한 칸의 픽셀 크기

    public SpellManageScreen(Screen lastScreen) {
        super(Component.translatable("gui.genesis.spell_menu.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // [핵심 변경] 마인크래프트 순정 버튼 위젯으로 위/아래 내비게이션 구현
        // 버튼 위치는 우측 스크롤바가 들어갈 법한 자리에 배치합니다.
        int buttonX = this.leftPos + 215;

        // 위로 가기 버튼 (▲)
        this.addRenderableWidget(Button.builder(Component.literal("▲"), b -> {
            if (this.scrollRowOffset > 0) {
                this.scrollRowOffset--;
            }
        }).bounds(buttonX, this.topPos + 15, 20, 20).build());

        // 아래로 가기 버튼 (▼)
        this.addRenderableWidget(Button.builder(Component.literal("▼"), b -> {
            List<String> learnedSpells = getPlayerLearnedSpells();
            int totalRows = (int) Math.ceil((double) learnedSpells.size() / gridCols);

            // 보여줄 마법이 남았을 때만 아래로 한 줄 내려감
            if (this.scrollRowOffset < totalRows - gridRowsVisible) {
                this.scrollRowOffset++;
            }
        }).bounds(buttonX, this.topPos + 40, 20, 20).build());

        // 나가기 버튼
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.onClose();
        }).bounds(this.leftPos + 205, this.topPos + 165, 40, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        // 1. 배경 프레임 그리기
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (this.minecraft.player == null) return;

        List<String> learnedSpells = getPlayerLearnedSpells();

        this.minecraft.player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(spellSlot -> {

            // 2. 상단 9x3 마법 상자 영역 아이콘 그리기
            int upperGridX = this.leftPos + 12;
            int upperGridY = this.topPos + 15;

            // 버튼 방식이어도 경계면 침범 방지를 위해 시저 테스트(가두기)는 유지하는 것이 안전합니다.
            graphics.enableScissor(upperGridX, upperGridY, upperGridX + (gridCols * slotSize), upperGridY + (gridRowsVisible * slotSize));

            for (int i = 0; i < learnedSpells.size(); i++) {
                int row = (i / gridCols) - this.scrollRowOffset; // 버튼 입력으로 변한 offset 반영
                int col = i % gridCols;

                // 현재 3줄(0, 1, 2) 범위 안에 들어오는 마법들만 불러와서 렌더링
                if (row >= 0 && row < gridRowsVisible) {
                    int slotX = upperGridX + (col * slotSize);
                    int slotY = upperGridY + (row * slotSize);

                    String spellId = learnedSpells.get(i);
                    ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/gui/spell/icon_" + spellId + ".png");

                    // 주문 아이콘 렌더링
                    graphics.blit(iconTex, slotX + 2, slotY + 2, 0, 0, 18, 18, 18, 18);

                    // 마우스 호버 효과
                    if (mouseX >= slotX && mouseX < slotX + slotSize && mouseY >= slotY && mouseY < slotY + slotSize) {
                        graphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0x55FFFFFF);
                    }
                }
            }
            graphics.disableScissor();

            // 3. 하단 장착 슬롯 10칸 그리기
            int bottomGridX = this.leftPos + 12;
            int bottomGridY = this.topPos + 125; // 이미지 레이아웃에 맞춰 Y축 조정 필요
            List<String> equipped = spellSlot.getEquippedSpells();

            for (int i = 0; i < 10; i++) {
                int slotX = bottomGridX + (i * slotSize);

                // 현재 각인 대상으로 선택된 방 강조
                if (i == this.selectedEquipSlot) {
                    graphics.fill(slotX, bottomGridY, slotX + slotSize, bottomGridY + slotSize, 0x44FFFF00);
                }

                // 각인된 마법 아이콘 그리기 (최대 10칸)
                if (i < equipped.size() && !equipped.get(i).isEmpty()) {
                    String eqSpellId = equipped.get(i);
                    ResourceLocation iconTex = new ResourceLocation(GenesisMod.MODID, "textures/gui/spell/icon_" + eqSpellId + ".png");
                    graphics.blit(iconTex, slotX + 2, bottomGridY + 2, 0, 0, 18, 18, 18, 18);
                }
            }
        });

        // 4. 위젯들(위/아래 버튼, 나가기 버튼) 최종 출력
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.minecraft.player == null) return super.mouseClicked(mouseX, mouseY, button);

        int upperGridX = this.leftPos + 12;
        int upperGridY = this.topPos + 15;
        int bottomGridX = this.leftPos + 12;
        int bottomGridY = this.topPos + 125;

        List<String> learnedSpells = getPlayerLearnedSpells();

        // [클릭 처리 1] 하단 10칸 슬롯 클릭 시 -> 선택된 슬롯 변경
        if (mouseY >= bottomGridY && mouseY < bottomGridY + slotSize) {
            int clickedIdx = (int) ((mouseX - bottomGridX) / slotSize);
            if (clickedIdx >= 0 && clickedIdx < 10) {
                this.selectedEquipSlot = clickedIdx;
                return true;
            }
        }

        // [클릭 처리 2] 상단 9x3 상자 안의 주문 클릭 시 -> 아래에 각인(패킷 전송)
        if (mouseX >= upperGridX && mouseX < upperGridX + (gridCols * slotSize) &&
                mouseY >= upperGridY && mouseY < upperGridY + (gridRowsVisible * slotSize)) {

            int col = (int) ((mouseX - upperGridX) / slotSize);
            int row = (int) ((mouseY - upperGridY) / slotSize) + this.scrollRowOffset; // 현재 스크롤 줄 수 보정
            int clickedSpellIdx = (row * gridCols) + col;

            if (clickedSpellIdx >= 0 && clickedSpellIdx < learnedSpells.size()) {
                String targetSpellId = learnedSpells.get(clickedSpellIdx);

                // 서버로 각인 요청 패킷 송출!
                GenesisNetwork.sendToServer(new PacketChangeSpell(this.selectedEquipSlot, targetSpellId));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    // 데이터 연동용 헬퍼 메서드 (추후 Capability 변수명 확정 시 이 함수 내부만 수정하면 됨)
    private List<String> getPlayerLearnedSpells() {
        List<String> learned = new ArrayList<>();
        // TODO: 실제 적용 시 stats.getLearnedSpells() 형태로 연동
        // 현재는 테스트용 더미 데이터
        learned.add("fireball");
        learned.add("heal");
        for(int i = 0; i < 30; i++) {
            learned.add("fireball"); // 스크롤 작동 확인용 임시 더미
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