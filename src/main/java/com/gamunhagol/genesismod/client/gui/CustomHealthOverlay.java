package com.gamunhagol.genesismod.client.gui;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomHealthOverlay {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private static final Random RANDOM = new Random();

    private static int lastHealth = 0;
    private static int displayHealth = 0;
    private static long lastHealthTime = 0;
    private static long healthBlinkTime = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderHealth(RenderGuiOverlayEvent.Pre event) {
        if (event.isCanceled()) return;

        if (event.getOverlay().id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player == null || mc.options.hideGui || player.isCreative() || player.isSpectator()) {
                return;
            }

            float maxHealth = player.getMaxHealth();

            if (maxHealth > 20.0F) {
                event.setCanceled(true);
                renderCompactHealthBar(event.getGuiGraphics(), player, mc, maxHealth);
            }
        }
    }

    private static void renderCompactHealthBar(GuiGraphics graphics, Player player, Minecraft mc, float maxHealth) {
        if (!(mc.gui instanceof ForgeGui forgeGui)) return;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int leftHeight = forgeGui.leftHeight;
        int xBase = width / 2 - 91;
        int yBase = height - leftHeight;

        int health = Mth.ceil(player.getHealth());
        int absorb = Mth.ceil(player.getAbsorptionAmount());
        boolean isHardcore = player.level().getLevelData().isHardcore();

        int currentRow = health > 0 ? ((health - 1) / 20) + 1 : 1;

        int ticks = mc.gui.getGuiTicks();
        boolean highlight = healthBlinkTime > (long) ticks && (healthBlinkTime - (long) ticks) / 3L % 2L == 1L;

        if (health < lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = Util.getMillis();
            healthBlinkTime = (long) (ticks + 20);
        } else if (health > lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = Util.getMillis();
            healthBlinkTime = (long) (ticks + 10);
        }

        if (Util.getMillis() - lastHealthTime > 1000L) {
            lastHealth = health;
            displayHealth = health;
            lastHealthTime = Util.getMillis();
        }

        lastHealth = health;
        RANDOM.setSeed((long) (ticks * 312871));

        int v = 9 * (isHardcore ? 5 : 0);
        int u = 16;
        if (player.hasEffect(MobEffects.POISON)) {
            u += 36;
        } else if (player.hasEffect(MobEffects.WITHER)) {
            u += 72;
        }

        String text = health + "/" + currentRow + "x";
        int textWidth = mc.font.width(text);
        int textX = xBase - textWidth - 2;
        int textY = yBase + 1;
        graphics.drawString(mc.font, text, textX, textY, 0xFF5555, true);

        for (int i = 0; i < 10; ++i) {
            float heartValueBase = (currentRow - 1) * 20 + i * 2;

            if (heartValueBase >= maxHealth) continue;

            int heartX = xBase + i * 8;
            int heartY = yBase;
            if (health <= 4) heartY += RANDOM.nextInt(2);

            graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, 16 + (highlight ? 9 : 0), v, 9, 9);

            if (highlight) {
                if (heartValueBase < displayHealth - 1) {
                    graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 54, v, 9, 9);
                } else if (heartValueBase == displayHealth - 1) {
                    graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 63, v, 9, 9);
                }
            }

            if (heartValueBase < health - 1) {
                graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 36, v, 9, 9);
            } else if (heartValueBase == health - 1) {
                graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 45, v, 9, 9);
            }
        }

        int absorbRows = 0;
        if (absorb > 0) {
            int absorbHearts = Mth.ceil(absorb / 2.0F);
            absorbRows = (absorbHearts - 1) / 10 + 1;

            for (int i = 0; i < absorbHearts; ++i) {
                int row = i / 10;
                int col = i % 10;

                int heartX = xBase + col * 8;
                int heartY = yBase - 10 - (row * 10);

                if (health <= 4) heartY += RANDOM.nextInt(2);

                int heartValue = i * 2;
                graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, 16, v, 9, 9);

                if (heartValue == absorb - 1) {
                    graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 153, v, 9, 9);
                } else if (heartValue < absorb) {
                    graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, u + 144, v, 9, 9);
                }
            }
        }

        forgeGui.leftHeight += 10 + (absorbRows * 10);
    }
}
