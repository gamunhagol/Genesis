package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.client.gui.LevelUpScreen;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider; // 임포트 확인
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiInjectionEvents {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // 바닐라 인벤토리에 버튼 주입
        if (event.getScreen() instanceof InventoryScreen invScreen) {

            // ★ 여기서 Capability를 열어서 해금 여부를 확인합니다.
            mc.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {

                // [조건] 해금된 상태(true)일 때만 버튼을 추가!
                if (stats.isLevelUpUnlocked()) {
                    int guiLeft = (invScreen.width - 176) / 2;
                    int guiTop = (invScreen.height - 166) / 2;

                    event.addListener(Button.builder(Component.literal("S"), button -> {
                                mc.setScreen(new LevelUpScreen(invScreen));
                            })
                            .bounds(guiLeft + 155, guiTop + 7, 18, 18)
                            .tooltip(Tooltip.create(Component.translatable("gui.genesis.level_up.title")))
                            .build());
                }
            });
        }
    }
}