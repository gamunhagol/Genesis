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

    // 서서히 사라지는 효과를 위해 마지막으로 전투/사용 했던 시간을 기록
    private static long lastActionTime = 0;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) return;

            // [추가] 에픽파이트 배틀 모드인지 확인
            LocalPlayerPatch playerPatch = ClientEngine.getInstance().getPlayerPatch();
            boolean isBattleMode = (playerPatch != null && playerPatch.isEpicFightMode());

            minecraft.player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                // 배틀 모드가 아니면서, 정신력이 꽉 차있다면 굳이 그리지 않음 (Fade Out 로직은 아래에서 처리)
                renderMentalBar(event.getGuiGraphics(), stats.getMentalPower(), stats.getMaxMentalPower(), isBattleMode);
            });
        }
    }

    private static void renderMentalBar(GuiGraphics graphics, float current, float max, boolean isBattleMode) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        // -----------------------------------------------------------
        // [로직] 투명도(Alpha) 계산: 꽉 차면 사라지기
        // -----------------------------------------------------------
        long currentTime = System.currentTimeMillis();

        // [수정 포인트] 0.01f 정도의 여유를 둡니다.
        // 현재 마력이 (최대치 - 0.01) 보다 작을 때만 시간을 갱신합니다.
        if (current < (max - 0.1f)) {
            lastActionTime = currentTime;
        }

        long timeDiff = currentTime - lastActionTime;
        float alpha = 1.0f;

        // 마력이 꽉 찬 상태에서 1.5초가 지나면 1초 동안 서서히 사라짐
        if (timeDiff > 1500) {
            alpha = (2500f - timeDiff) / 1000f;
            alpha = Mth.clamp(alpha, 0.0f, 1.0f);
        }

        // 투명도가 0이면 렌더링 스킵
        if (alpha <= 0.0f) return;

        // -----------------------------------------------------------
        // [렌더링] 위치 및 그리기
        // -----------------------------------------------------------
        Vec2i staminaPos = ClientConfig.getStaminaPosition(width, height);
        int x = staminaPos.x;
        int y = staminaPos.y - 12;

        int texU = 2;
        int texV = 38;
        int texH = 8;

        RenderSystem.enableBlend();

        // 스케일링 시작
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(1.0F, 0.5F, 1.0F);

        // [중요] 배경색 (Alpha 값 적용) -> 끝에 alpha 변수를 넣습니다.
        RenderSystem.setShaderColor(0.1F, 0.1F, 0.2F, alpha);
        graphics.blit(BATTLE_ICONS, 0, 0, texU, texV, 118, texH, 256, 256);

        // [중요] 내용물 (Alpha 값 적용)
        if (current > 0) {
            float ratio = current / max;
            int filledWidth = (int) (118 * ratio);

            // 파란색 + Alpha
            RenderSystem.setShaderColor(0.0F, 0.3F, 0.9F, alpha);
            // 파란색 바의 텍스처 좌표가 2, 47 이라고 가정 (이미지 확인 필요)
            graphics.blit(BATTLE_ICONS, 0, 0, 2, 47, filledWidth, texH, 256, 256);
        }

        graphics.pose().popPose();

        // 색상 초기화 (안 하면 다른 UI가 투명해짐)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}