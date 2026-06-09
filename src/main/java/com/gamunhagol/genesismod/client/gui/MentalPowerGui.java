package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.config.ClientConfig;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MentalPowerGui {

    private static final ResourceLocation BATTLE_ICONS = new ResourceLocation(GenesisMod.MODID, "textures/gui/battle_icons.png");

    private static long lastActionTime = 0;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) return;

            LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
            boolean isBattleMode = (playerPatch != null && playerPatch.isEpicFightMode());

            minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                renderMentalBar(event.getGuiGraphics(), stats.getMental(), stats.getMaxMental(), isBattleMode);
            });
        }
    }

    private static void renderMentalBar(GuiGraphics graphics, float current, float max, boolean isBattleMode) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        long currentTime = System.currentTimeMillis();

        if (current < (max - 0.1f)) {
            lastActionTime = currentTime;
        }

        long timeDiff = currentTime - lastActionTime;
        float alpha = 1.0f;

        if (timeDiff > 1500) {
            alpha = (2500f - timeDiff) / 1000f;
            alpha = Mth.clamp(alpha, 0.0f, 1.0f);
        }

        if (alpha <= 0.0f) return;

        Vec2i staminaPos = ClientConfig.getStaminaPosition(width, height);
        int x = staminaPos.x;
        int y = staminaPos.y - 12;

        int texU = 2;
        int texV = 38;
        int texH = 8;

        RenderSystem.enableBlend();

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(1.0F, 0.5F, 1.0F);

        RenderSystem.setShaderColor(0.1F, 0.1F, 0.2F, alpha);
        graphics.blit(BATTLE_ICONS, 0, 0, texU, texV, 118, texH, 256, 256);

        if (current > 0) {
            float ratio = current / max;
            int filledWidth = (int) (118 * ratio);

            RenderSystem.setShaderColor(0.0F, 0.3F, 0.9F, alpha);
            graphics.blit(BATTLE_ICONS, 0, 0, 2, 47, filledWidth, texH, 256, 256);
        }

        graphics.pose().popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}