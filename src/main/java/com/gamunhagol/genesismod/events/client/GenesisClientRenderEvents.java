package com.gamunhagol.genesismod.events.client;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GenesisClientRenderEvents {

    private static final ResourceLocation BEACON_BEAM_TEX = new ResourceLocation("textures/entity/beacon_beam.png");

    private static Vec3 activeTargetPos = null;
    private static int beamTicksRemaining = 0;

    public static void spawnBeam(Vec3 targetPos) {
        activeTargetPos = targetPos;
        beamTicksRemaining = 40;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && beamTicksRemaining > 0) {
            beamTicksRemaining--;
            if (beamTicksRemaining <= 0) {
                activeTargetPos = null;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (activeTargetPos == null || beamTicksRemaining <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        Vec3 cameraPos = event.getCamera().getPosition();

        Vec3 startPos = mc.player.getEyePosition(event.getPartialTick()).add(0.0D, -0.4D, 0.0D);

        poseStack.pushPose();

        poseStack.translate(startPos.x - cameraPos.x, startPos.y - cameraPos.y, startPos.z - cameraPos.z);

        double dx = activeTargetPos.x - startPos.x;
        double dy = activeTargetPos.y - startPos.y;
        double dz = activeTargetPos.z - startPos.z;
        float height = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dx, dz));
        float pitch = (float) Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-pitch + 90F));

        poseStack.translate(-0.5D, 0.0D, -0.5D);

        long gameTime = mc.level.getGameTime();
        float partialTicks = event.getPartialTick();
        float[] orangeColor = new float[]{1.0F, 0.35F, 0.0F};

        // 7. 빔 렌더링
        BeaconRenderer.renderBeaconBeam(
                poseStack, bufferSource, BEACON_BEAM_TEX, partialTicks,
                1.0F, gameTime, 0, (int) height,
                orangeColor, 0.15F, 0.25F
        );

        poseStack.popPose();
    }
}