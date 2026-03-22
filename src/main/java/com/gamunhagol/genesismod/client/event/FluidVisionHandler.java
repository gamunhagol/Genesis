package com.gamunhagol.genesismod.client.event;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.fluid.GenesisFluids;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 🌫️ FluidVisionHandler — 온천수 시야색상 및 안개 효과 통합 제어
 * (오버레이 없이, HUD 가림 없이)
 */
@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidVisionHandler {

    // 🔹 카메라 기준으로 현재 블록/유체 상태 가져오기

    private static FluidState getEyeFluid(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        BlockPos eyePos = BlockPos.containing(camera.getPosition());
        return mc.level.getFluidState(eyePos);
    }

    // 🟤 1️⃣ 안개 거리 및 색상
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        FluidState fluid = getEyeFluid(camera);
        if (fluid == null) return;

        if (fluid.is(GenesisFluids.QUICKSAND.get()) || fluid.is(GenesisFluids.QUICKSAND_FLOWING.get())) {
            // 1. 안개 계산 방식 강제 설정
            event.setFogShape(FogShape.CYLINDER); // 용암과 유사한 평면 안개 방식

            // 2. 안개 거리 설정 (용암처럼 매우 가깝게)
            // 시작 지점을 마이너스로 주면 화면 바로 앞부터 안개가 낍니다.
            float near = 0.0F;
            float far = 0.5F; // 0.5블록 앞까지만 보임 (거의 안 보임)

            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);

            RenderSystem.setShaderFogStart(near);
            RenderSystem.setShaderFogEnd(far);

            // 4. 이벤트 취소 (중요: 마인크래프트 기본 안개 설정을 무시하고 내 설정을 강제함)
            event.setCanceled(true);
        }
        if (fluid.is(GenesisFluids.BLOOD.get()) || fluid.is(GenesisFluids.BLOOD_FLOWING.get())) {
            float near = 0.0F;
            float far = 4.0F;

            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);

            RenderSystem.setShaderFogStart(near);
            RenderSystem.setShaderFogEnd(far);
            RenderSystem.setShaderFogColor(0.4F, 0.05F, 0.05F);
        }
    }

    // 🔵 2️⃣ 시야 내부 색상 (FogColor)
    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        FluidState fluid = getEyeFluid(camera);
        if (fluid == null) return;

        if (fluid.is(GenesisFluids.QUICKSAND.get()) || fluid.is(GenesisFluids.QUICKSAND_FLOWING.get())) {
            // 모래색 (DACFA3) 비율 설정
            event.setRed(0.85F);
            event.setGreen(0.81F);
            event.setBlue(0.63F);
        }
        if (fluid.is(GenesisFluids.BLOOD.get()) || fluid.is(GenesisFluids.BLOOD_FLOWING.get())) {
            event.setRed(0.4F);
            event.setGreen(0.05F);
            event.setBlue(0.05F);
        }
    }
}
