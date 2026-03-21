package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.gui.LevelUpScreen;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
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

        // 화면이 새로 열릴 때마다 이전 버튼 참조를 일단 숨김 처리하거나 초기화
        if (statButton != null) {
            statButton.visible = false;
        }

        if (event.getScreen() instanceof InventoryScreen inv) {
            createPersistentButton(event, inv.getGuiLeft() + 155, inv.getGuiTop() + 7, inv);
        } else if (event.getScreen() instanceof CreativeModeInventoryScreen creative) {
            createPersistentButton(event, creative.getGuiLeft() + 159, creative.getGuiTop() + 28, creative);
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        if (statButton == null) return;

        // 1. 일반 서바이벌 인벤토리 화면인 경우
        if (event.getScreen() instanceof InventoryScreen inv) {
            statButton.visible = true;
            statButton.active = true;
            // 위치 고정 (서바이벌 인벤토리는 고정값이므로 init에서 잡아준 대로 가도 됨)
        }
        // 2. 크리에이티브 인벤토리 화면인 경우
        else if (event.getScreen() instanceof CreativeModeInventoryScreen creative) {
            // '가방' 모양의 서바이벌 탭이 선택되었는지 확인
            boolean isSurvivalTab = creative.isInventoryOpen();

            statButton.visible = isSurvivalTab;
            statButton.active = isSurvivalTab;

            if (isSurvivalTab) {
                // 탭을 옮겨 다닐 때 위치가 틀어질 수 있으므로 매 프레임 위치를 갱신
                statButton.setX(creative.getGuiLeft() + 159);
                statButton.setY(creative.getGuiTop() + 28);
            }
        }
        // 3. 그 외 모든 화면 (창고, 조합대, 화로, 메뉴 화면 등)
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
}