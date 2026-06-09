package com.gamunhagol.genesismod.events.client;

import com.gamunhagol.genesismod.client.gui.LevelUpScreen;
import com.gamunhagol.genesismod.init.ModKeyBindings;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiInjectionEvents {

    private static Button statButton;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (statButton != null) {
            statButton.visible = false;
        }

        if (event.getScreen() instanceof InventoryScreen inv) {
            createPersistentButton(event, inv.getGuiLeft() + 155, inv.getGuiTop() + 7, inv);
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        if (statButton == null) return;
        if (event.getScreen() instanceof InventoryScreen) {
            statButton.visible = true;
            statButton.active = true;
        }
        else {
            statButton.visible = false;
            statButton.active = false;
        }
    }

    private static void createPersistentButton(ScreenEvent.Init.Post event, int x, int y, net.minecraft.client.gui.screens.Screen parent) {
        Minecraft mc = Minecraft.getInstance();

        statButton = Button.builder(Component.literal("L"), b -> {
                    mc.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                        if (stats.isLevelUpUnlocked()) {
                            mc.setScreen(new LevelUpScreen(parent));
                        } else {
                            mc.player.displayClientMessage(
                                    Component.translatable("message.genesis.level_up.locked").withStyle(ChatFormatting.RED),
                                    true
                            );
                            mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
                        }
                    });
                })
                .bounds(x, y, 18, 18)
                .build();

        updateButtonTooltip(mc);
        event.addListener(statButton);
    }

    private static void updateButtonTooltip(Minecraft mc) {
        if (statButton == null || mc.player == null) return;

        mc.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            if (stats.isLevelUpUnlocked()) {
                statButton.setTooltip(Tooltip.create(Component.translatable("gui.genesis.level_up.title")));
            } else {
                statButton.setTooltip(Tooltip.create(
                        Component.translatable("gui.genesis.level_up.hint").withStyle(ChatFormatting.GRAY)
                ));
            }
        });
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (ModKeyBindings.LEVEL_UP_KEY.consumeClick()) {
            mc.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                if (stats.isLevelUpUnlocked()) {
                    mc.setScreen(new LevelUpScreen(null));
                } else {
                    mc.player.displayClientMessage(
                            Component.translatable("message.genesis.level_up.locked").withStyle(ChatFormatting.RED),
                            true
                    );
                    mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
                }
            });
        }
    }
}