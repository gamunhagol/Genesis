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

        // 화면이 바뀔 때마다 버튼 상태 초기화
        if (statButton != null) {
            statButton.visible = false;
        }

        // 오직 서바이벌 인벤토리(InventoryScreen)에서만 버튼 생성
        if (event.getScreen() instanceof InventoryScreen inv) {
            createPersistentButton(event, inv.getGuiLeft() + 155, inv.getGuiTop() + 7, inv);
        }
        // 크리에이티브 인벤토리 관련 else if 블록을 완전히 제거함
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        if (statButton == null) return;

        // 일반 서바이벌 인벤토리 화면인 경우에만 활성화
        if (event.getScreen() instanceof InventoryScreen) {
            statButton.visible = true;
            statButton.active = true;
        }
        // 그 외 모든 화면(크리에이티브 포함)에서는 버튼을 숨기고 비활성화
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
        event.addListener(statButton); // 해당 화면의 위젯 리스트에 추가
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

        // 1. 플레이어가 존재하고, 현재 열린 화면이 없을 때만 단축키 작동
        // (채팅창이나 다른 메뉴가 열려있을 때 중복으로 열리는 것 방지)
        if (mc.player == null || mc.screen != null) return;

        // 2. 단축키 입력 확인 (GLFW.GLFW_PRESS는 키를 누른 순간 한 번만 실행됨)
        if (ModKeyBindings.LEVEL_UP_KEY.consumeClick()) {
            mc.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                if (stats.isLevelUpUnlocked()) {
                    // 단축키로 열 때는 부모 화면이 없으므로 null 전달
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