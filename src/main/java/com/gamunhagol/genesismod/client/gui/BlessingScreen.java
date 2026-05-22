package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.content.StatueRewardManager;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketStatueUnlockNode;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BlessingScreen extends Screen {
    private final Screen lastScreen;
    private final String statueId;

    private final ResourceLocation backgroundTexture;

    private static final ResourceLocation NODE_LOCKED = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/blank_constellation.png");
    private static final ResourceLocation NODE_UNLOCKED = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/constellation.png");

    private static final int BG_WIDTH = 1280;
    private static final int BG_HEIGHT = 720;

    private float panX = 0;
    private float panY = 0;
    private float zoom = 1.0f;

    private final List<StatueRewardManager.NodeInfo> nodes = new ArrayList<>();

    public BlessingScreen(Screen lastScreen, String statueId) {
        super(Component.literal("Statue Spec-Up"));
        this.lastScreen = lastScreen;
        this.statueId = statueId;

        String bgName = "textures/gui/screen/starry_path_" + statueId.replace("god_", "") + ".png";
        this.backgroundTexture = new ResourceLocation(GenesisMod.MODID, bgName);

        this.nodes.addAll(StatueRewardManager.getNodesForStatue(statueId));
    }

    @Override
    protected void init() {
        super.init();

        // 화면이 켜질 때 화면 크기에 맞춰 초기 줌 상태와 이동 한계치 재조정
        clampPan();

        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width - 30, this.height - 25, 20, 20).build());
    }

    // 줌 배율을 고려하여 드래그(Pan)가 화면 밖으로 나가지 않게 막아주는 헬퍼 메서드
    private void clampPan() {
        // 줌이 적용되었을 때 화면 바깥으로 나갈 수 있는 최대 X, Y 거리를 계산
        float maxPanX = Math.max(0, (BG_WIDTH * zoom - this.width) / 2.0F);
        float maxPanY = Math.max(0, (BG_HEIGHT * zoom - this.height) / 2.0F);

        this.panX = Mth.clamp(this.panX, -maxPanX, maxPanX);
        this.panY = Mth.clamp(this.panY, -maxPanY, maxPanY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.pose().pushPose();

        // 화면 중앙으로 이동 + 드래그 위치 반영
        graphics.pose().translate(this.width / 2.0F + panX, this.height / 2.0F + panY, 0);
        // 줌 배율 적용 (반드시 위치를 잡은 뒤에 확대/축소해야 함)
        graphics.pose().scale(zoom, zoom, 1.0f);

        graphics.blit(backgroundTexture, -BG_WIDTH / 2, -BG_HEIGHT / 2, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        int nodeSize = 16;
        int halfSize = 8;

        AtomicReference<StatueRewardManager.NodeInfo> hoveredNode = new AtomicReference<>();
        AtomicBoolean isHoveredUnlocked = new AtomicBoolean(false);

        // 마우스 좌표 역계산: (현재 마우스 - 화면중앙 - 드래그위치) / 줌배율
        double worldMouseX = ((mouseX - (this.width / 2.0) - this.panX) / zoom) + (BG_WIDTH / 2.0);
        double worldMouseY = ((mouseY - (this.height / 2.0) - this.panY) / zoom) + (BG_HEIGHT / 2.0);

        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                for (StatueRewardManager.NodeInfo node : nodes) {

                    boolean isUnlocked = cap.isNodeUnlocked(this.statueId, node.id);
                    ResourceLocation icon = isUnlocked ? NODE_UNLOCKED : NODE_LOCKED;

                    int renderX = (-BG_WIDTH / 2) + node.x;
                    int renderY = (-BG_HEIGHT / 2) + node.y;

                    graphics.blit(icon, renderX - nodeSize / 2, renderY - nodeSize / 2, 0, 0, nodeSize, nodeSize, nodeSize, nodeSize);

                    if (worldMouseX >= node.x - halfSize && worldMouseX <= node.x + halfSize &&
                            worldMouseY >= node.y - halfSize && worldMouseY <= node.y + halfSize) {
                        hoveredNode.set(node);
                        isHoveredUnlocked.set(isUnlocked);
                    }
                }
            });
        }

        graphics.pose().popPose();
        super.render(graphics, mouseX, mouseY, partialTick);

        if (hoveredNode.get() != null) {
            drawNodeTooltip(graphics, mouseX, mouseY, hoveredNode.get(), isHoveredUnlocked.get());
        }
    }

    private void drawNodeTooltip(GuiGraphics graphics, int mouseX, int mouseY, StatueRewardManager.NodeInfo node, boolean isUnlocked) {
        List<Component> tooltipLines = new ArrayList<>();

        Component rewardName = node.rewardItem.getDescription();
        Component costName = node.costItem.getDescription();

        tooltipLines.add(Component.translatable("gui.genesis.blessing.reward", rewardName)
                .withStyle(ChatFormatting.GRAY));

        if (!isUnlocked) {
            tooltipLines.add(Component.translatable("gui.genesis.blessing.cost", costName)
                    .withStyle(ChatFormatting.GRAY));
        }

        graphics.renderComponentTooltip(this.font, tooltipLines, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 || button == 1) {
            this.panX += (float) dragX;
            this.panY += (float) dragY;

            // 드래그 후 화면 바깥으로 나갔는지 확인 후 보정
            clampPan();

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    // 마우스 휠 이벤트 오버라이드
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float zoomStep = 0.1f; // 한 칸 굴릴 때마다 변하는 줌 배율
        if (delta > 0) {
            this.zoom += zoomStep; // 마우스 휠 위로 (확대)
        } else if (delta < 0) {
            this.zoom -= zoomStep; // 마우스 휠 아래로 (축소)
        }

        // 축소 제한: 배경(1280x720)이 현재 마인크래프트 창 화면보다 작아져서 바깥 여백이 생기는 것을 방지
        float minZoomX = (float) this.width / BG_WIDTH;
        float minZoomY = (float) this.height / BG_HEIGHT;
        float minZoom = Math.max(minZoomX, minZoomY);

        // 배율 한계 설정 (최소: 화면에 딱 맞게, 최대: 3배)
        this.zoom = Mth.clamp(this.zoom, minZoom, 3.0f);

        // 줌이 바뀌면 배경 크기가 변하므로 한계선 재계산
        clampPan();
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            //  클릭 시에도 줌 배율을 동일하게 역산하여 월드 좌표를 잡아야 함
            double clickedWorldX = ((mouseX - (this.width / 2.0) - this.panX) / zoom) + (BG_WIDTH / 2.0);
            double clickedWorldY = ((mouseY - (this.height / 2.0) - this.panY) / zoom) + (BG_HEIGHT / 2.0);

            int halfSize = 8;

            for (StatueRewardManager.NodeInfo node : nodes) {
                if (clickedWorldX >= node.x - halfSize && clickedWorldX <= node.x + halfSize &&
                        clickedWorldY >= node.y - halfSize && clickedWorldY <= node.y + halfSize) {

                    GenesisNetwork.sendToServer(new PacketStatueUnlockNode(this.statueId, node.id));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
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