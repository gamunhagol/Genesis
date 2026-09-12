package com.gamunhagol.genesismod.client.renderer;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.border.SpatialRuptureManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpatialRuptureRenderer {

    private static final ResourceLocation FORCEFIELD = new ResourceLocation("textures/misc/forcefield.png");

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (SpatialRuptureManager.ACTIVE_ZONES.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        ResourceLocation currentDim = mc.level.dimension().location();

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f mat = poseStack.last().pose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, FORCEFIELD);

        float timeScroll = (float)(Util.getMillis() % 4000L) / 4000.0F;

        double bottomY = -64.0D;
        double topY = 320.0D;

        for (SpatialRuptureManager.RuptureZone zone : SpatialRuptureManager.ACTIVE_ZONES) {
            if (!zone.dimension.equals(currentDim)) continue;

            double minX = zone.minX;
            double maxX = zone.maxX;
            double minZ = zone.minZ;
            double maxZ = zone.maxZ;

            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

            renderWall(buffer, mat, minX, maxX, topY, bottomY, minZ, minZ, timeScroll);
            renderWall(buffer, mat, maxX, minX, topY, bottomY, maxZ, maxZ, timeScroll);
            renderWall(buffer, mat, minX, minX, topY, bottomY, maxZ, minZ, timeScroll);
            renderWall(buffer, mat, maxX, maxX, topY, bottomY, minZ, maxZ, timeScroll);

            tesselator.end();
        }

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    private static void renderWall(BufferBuilder buf, Matrix4f mat, double x1, double x2, double topY, double bottomY, double z1, double z2, float scroll) {
        float u1 = scroll;
        float u2 = scroll + 1.0F;
        float v1 = 0.0F;
        float v2 = (float)((topY - bottomY) / 4.0D);

        buf.vertex(mat, (float)x1, (float)topY, (float)z1).uv(u1, v1).color(255, 255, 255, 180).endVertex();
        buf.vertex(mat, (float)x2, (float)topY, (float)z2).uv(u2, v1).color(255, 255, 255, 180).endVertex();
        buf.vertex(mat, (float)x2, (float)bottomY, (float)z2).uv(u2, v2).color(255, 255, 255, 180).endVertex();
        buf.vertex(mat, (float)x1, (float)bottomY, (float)z1).uv(u1, v2).color(255, 255, 255, 180).endVertex();
    }
}