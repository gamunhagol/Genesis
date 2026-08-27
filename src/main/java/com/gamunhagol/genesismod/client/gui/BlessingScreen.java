package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.content.StatueRewardManager;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketStatueUnlockNode;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BlessingScreen extends Screen {
    private final Screen lastScreen;
    private final String statueId;

    private static final ResourceLocation NODE_LOCKED = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/blank_constellation.png");
    private static final ResourceLocation NODE_UNLOCKED = new ResourceLocation(GenesisMod.MODID, "textures/gui/screen/constellation.png");

    private static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation("textures/entity/end_portal.png");

    private static final int BG_WIDTH = 2000;
    private static final int BG_HEIGHT = 2000;

    private float panX = 0;
    private float panY = 0;
    private float zoom = 1.0f;

    private final List<StatueRewardManager.NodeInfo> nodes = new ArrayList<>();

    public BlessingScreen(Screen lastScreen, String statueId) {
        super(Component.literal("Statue Spec-Up"));
        this.lastScreen = lastScreen;
        this.statueId = statueId;

        this.nodes.addAll(StatueRewardManager.getNodesForStatue(statueId));
    }

    @Override
    protected void init() {
        super.init();
        clampPan();

        this.addRenderableWidget(Button.builder(Component.translatable("gui.genesis.button.exit"), b -> {
            this.minecraft.setScreen(this.lastScreen);
        }).bounds(this.width - 30, this.height - 25, 20, 20).build());
    }

    private void clampPan() {
        float maxPanX = Math.max(0, (BG_WIDTH * zoom - this.width) / 2.0F);
        float maxPanY = Math.max(0, (BG_HEIGHT * zoom - this.height) / 2.0F);

        this.panX = Mth.clamp(this.panX, -maxPanX, maxPanX);
        this.panY = Mth.clamp(this.panY, -maxPanY, maxPanY);
    }

    private void renderEndPortalBackground(GuiGraphics graphics) {
        float time = Util.getMillis() / 80000.0F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        Matrix4f matrix4f = graphics.pose().last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_SKY_LOCATION);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(0.1F, 0.1F, 0.1F, 1.0F);

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        float skyU = time;
        float skyV = time;
        float skyScale = 1.0f;

        bufferbuilder.vertex(matrix4f, 0, this.height, 0).uv(skyU, skyV + this.height / 256.0F * skyScale).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(matrix4f, this.width, this.height, 0).uv(skyU + this.width / 256.0F * skyScale, skyV + this.height / 256.0F * skyScale).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(matrix4f, this.width, 0, 0).uv(skyU + this.width / 256.0F * skyScale, skyV).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(matrix4f, 0, 0, 0).uv(skyU, skyV).color(255, 255, 255, 255).endVertex();
        tesselator.end();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_PORTAL_LOCATION);

        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        int passes = 15;
        for (int i = 1; i < passes; ++i) {
            graphics.pose().pushPose();

            float scale = 2.0F / (18 - i);
            float panOffsetX = (this.panX * scale) / 200.0F;
            float panOffsetY = (this.panY * scale) / 200.0F;
            float offsetU = (float)(-(time * i * 0.2F)) + panOffsetX;
            float offsetV = (float)(time * i * 0.2F) + panOffsetY;

            graphics.pose().translate(this.width / 2.0F, this.height / 2.0F, 0);
            graphics.pose().mulPose(Axis.ZP.rotationDegrees((i * i * 4321 + i * 9) * 2.0F));
            graphics.pose().translate(-this.width / 2.0F, -this.height / 2.0F, 0);

            Matrix4f portalMatrix = graphics.pose().last().pose();

            float r = (float)(Math.random() * 0.5D + 0.1D);
            float g = (float)(Math.random() * 0.5D + 0.4D);
            float b = (float)(Math.random() * 0.5D + 0.5D);
            float alpha = 1.0F / (passes - i);

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(portalMatrix, -this.width, this.height * 2, 0).uv(offsetU, offsetV + (this.height * 2) / 256.0F * scale).color(r, g, b, alpha).endVertex();
            bufferbuilder.vertex(portalMatrix, this.width * 2, this.height * 2, 0).uv(offsetU + (this.width * 2) / 256.0F * scale, offsetV + (this.height * 2) / 256.0F * scale).color(r, g, b, alpha).endVertex();
            bufferbuilder.vertex(portalMatrix, this.width * 2, -this.height, 0).uv(offsetU + (this.width * 2) / 256.0F * scale, offsetV).color(r, g, b, alpha).endVertex();
            bufferbuilder.vertex(portalMatrix, -this.width, -this.height, 0).uv(offsetU, offsetV).color(r, g, b, alpha).endVertex();

            tesselator.end();
            graphics.pose().popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderAstralLine(GuiGraphics graphics, float x1, float y1, float x2, float y2, boolean isUnlocked) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0.0F) return;

        float nx = -dy / len;
        float ny = dx / len;

        graphics.flush();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        float outerWidth = isUnlocked ? 5.0f : 2.5f;
        float rOuter = isUnlocked ? 0.75f : 0.3f;
        float gOuter = isUnlocked ? 0.75f : 0.3f;
        float bOuter = isUnlocked ? 0.75f : 0.3f;
        float aOuter = isUnlocked ? 0.5f : 0.4f;

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        drawQuadStrip(buffer, matrix, x1, y1, x2, y2, nx, ny, outerWidth, rOuter, gOuter, bOuter, aOuter);
        tesselator.end();

        float innerWidth = isUnlocked ? 2.0f : 1.0f;
        float rInner = isUnlocked ? 0.9f : 0.45f;
        float gInner = isUnlocked ? 0.9f : 0.45f;
        float bInner = isUnlocked ? 0.9f : 0.45f;
        float aInner = isUnlocked ? 0.85f : 0.6f;

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        drawQuadStrip(buffer, matrix, x1, y1, x2, y2, nx, ny, innerWidth, rInner, gInner, bInner, aInner);
        tesselator.end();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void drawQuadStrip(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float x2, float y2, float nx, float ny, float width, float r, float g, float b, float a) {
        float offX = nx * (width / 2.0f);
        float offY = ny * (width / 2.0f);

        buffer.vertex(matrix, x1 - offX, y1 - offY, 0).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2 - offX, y2 - offY, 0).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x2 + offX, y2 + offY, 0).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, x1 + offX, y1 + offY, 0).color(r, g, b, a).endVertex();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderEndPortalBackground(graphics);

        graphics.flush();

        graphics.pose().pushPose();
        graphics.pose().translate(this.width / 2.0F + panX, this.height / 2.0F + panY, 50.0F);
        graphics.pose().scale(zoom, zoom, 1.0f);

        int nodeSize = 16;
        int halfSize = 8;

        AtomicReference<StatueRewardManager.NodeInfo> hoveredNode = new AtomicReference<>();
        AtomicBoolean isHoveredUnlocked = new AtomicBoolean(false);

        double worldMouseX = ((mouseX - (this.width / 2.0) - this.panX) / zoom) + (BG_WIDTH / 2.0);
        double worldMouseY = ((mouseY - (this.height / 2.0) - this.panY) / zoom) + (BG_HEIGHT / 2.0);

        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {

                for (StatueRewardManager.NodeInfo node : nodes) {
                    for (int requiredId : node.requiredNodes) {
                        StatueRewardManager.NodeInfo parentNode = null;
                        for (StatueRewardManager.NodeInfo n : nodes) {
                            if (n.id == requiredId) {
                                parentNode = n;
                                break;
                            }
                        }

                        if (parentNode != null) {
                            boolean isLineActive = cap.isNodeUnlocked(this.statueId, node.id) && cap.isNodeUnlocked(this.statueId, parentNode.id);

                            int x1 = (-BG_WIDTH / 2) + node.x;
                            int y1 = (-BG_HEIGHT / 2) + node.y;
                            int x2 = (-BG_WIDTH / 2) + parentNode.x;
                            int y2 = (-BG_HEIGHT / 2) + parentNode.y;

                            renderAstralLine(graphics, x1, y1, x2, y2, isLineActive);
                        }
                    }
                }

                graphics.flush();

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
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 300);
            drawNodeTooltip(graphics, mouseX, mouseY, hoveredNode.get(), isHoveredUnlocked.get());
            graphics.pose().popPose();
        }
    }

    private void drawNodeTooltip(GuiGraphics graphics, int mouseX, int mouseY, StatueRewardManager.NodeInfo node, boolean isUnlocked) {
        List<Component> tooltipLines = new ArrayList<>();

        Component rewardName;
        if (node.isPassive()) {
            rewardName = Component.translatable("blessing.genesis." + this.statueId + "." + node.id);
        } else {
            rewardName = node.rewardItem.getHoverName();
        }

        Component costName = node.costItem.getDescription();
        tooltipLines.add(Component.translatable("gui.genesis.blessing.reward", rewardName).withStyle(ChatFormatting.GRAY));

        if (!isUnlocked) {
            tooltipLines.add(Component.translatable("gui.genesis.blessing.cost", costName).withStyle(ChatFormatting.GRAY));
        }

        graphics.renderComponentTooltip(this.font, tooltipLines, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 || button == 1) {
            this.panX += (float) dragX;
            this.panY += (float) dragY;
            clampPan();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        float zoomStep = 0.1f;
        if (delta > 0) {
            this.zoom += zoomStep;
        } else if (delta < 0) {
            this.zoom -= zoomStep;
        }

        float minZoom = Math.max((float) this.width / BG_WIDTH, (float) this.height / BG_HEIGHT);
        this.zoom = Mth.clamp(this.zoom, minZoom, 3.0f);

        clampPan();
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
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