package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.content.StatueRewardManager;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketStatueUnlockNode;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider; // 이거 임포트 되어있는지 확인해주세요!
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

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

    // 더미 StatueNode 클래스 완전 삭제

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
        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width - 30, this.height - 25, 20, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.pose().pushPose();

        graphics.pose().translate(this.width / 2.0F + panX, this.height / 2.0F + panY, 0);

        graphics.blit(backgroundTexture, -BG_WIDTH / 2, -BG_HEIGHT / 2, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        int nodeSize = 16;

        // 클라이언트 플레이어의 Capability를 가져와서 isUnlocked를 체크합니다.
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                for (StatueRewardManager.NodeInfo node : nodes) {

                    boolean isUnlocked = cap.isNodeUnlocked(this.statueId, node.id);
                    ResourceLocation icon = isUnlocked ? NODE_UNLOCKED : NODE_LOCKED;

                    int renderX = (-BG_WIDTH / 2) + node.x;
                    int renderY = (-BG_HEIGHT / 2) + node.y;

                    graphics.blit(icon, renderX - nodeSize / 2, renderY - nodeSize / 2, 0, 0, nodeSize, nodeSize, nodeSize, nodeSize);
                }
            });
        }

        graphics.pose().popPose();
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 || button == 1) {
            this.panX += (float) dragX;
            this.panY += (float) dragY;

            float maxPanX = Math.max(0, (BG_WIDTH - this.width) / 2.0F);
            float maxPanY = Math.max(0, (BG_HEIGHT - this.height) / 2.0F);

            this.panX = Mth.clamp(this.panX, -maxPanX, maxPanX);
            this.panY = Mth.clamp(this.panY, -maxPanY, maxPanY);

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double clickedWorldX = mouseX - (this.width / 2.0) - this.panX + (BG_WIDTH / 2.0);
            double clickedWorldY = mouseY - (this.height / 2.0) - this.panY + (BG_HEIGHT / 2.0);

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