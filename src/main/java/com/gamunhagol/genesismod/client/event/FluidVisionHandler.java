package com.gamunhagol.genesismod.client.event;

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
 * 🌫️ FluidVisionHandler — 온천수/모래함정의 시야색상 및 안개 효과 통합 제어
 * (오버레이 없이, HUD 가림 없이)
 */
@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidVisionHandler {

    // 🔹 카메라 기준으로 현재 블록/유체 상태 가져오기
    private static BlockState getEyeBlock(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        BlockPos eyePos = BlockPos.containing(camera.getPosition());
        return mc.level.getBlockState(eyePos);
    }

    private static FluidState getEyeFluid(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        BlockPos eyePos = BlockPos.containing(camera.getPosition());
        return mc.level.getFluidState(eyePos);
    }

    // 🟤 1️⃣ 안개 거리 및 색상 (모래함정 / 온천수 구분)
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        BlockState block = getEyeBlock(camera);
        FluidState fluid = getEyeFluid(camera);
        if (block == null && fluid == null) return;

        // 🟡 모래함정 안개
        if (block != null && block.is(GenesisBlocks.SAND_TRAP.get())) {
            float near = 0.25F;
            float far = 4.0F;
            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);
            RenderSystem.setShaderFogStart(near);
            RenderSystem.setShaderFogEnd(far);
            RenderSystem.setShaderFogColor(0.93F, 0.88F, 0.68F); // 밝은 모래색
        }

        // 💧 온천수 안개
        else if (fluid != null && (fluid.is(GenesisFluids.HOT_SPRING.get()) || fluid.is(GenesisFluids.HOT_SPRING_FLOWING.get()))) {
            float near = 0.5F;
            float far = 8.0F;
            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);
            RenderSystem.setShaderFogStart(near);
            RenderSystem.setShaderFogEnd(far);
            RenderSystem.setShaderFogColor(0.55F, 0.95F, 0.95F); // 하늘색 계열
        }
    }

    // 🔵 2️⃣ 시야 내부 색상 (FogColor)
    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        BlockState block = getEyeBlock(camera);
        FluidState fluid = getEyeFluid(camera);
        if (block == null && fluid == null) return;

        // 🟡 모래함정 색상
        if (block != null && block.is(GenesisBlocks.SAND_TRAP.get())) {
            event.setRed(0.89F);
            event.setGreen(0.86F);
            event.setBlue(0.69F);
        }

        // 💧 온천수 색상
        else if (fluid != null && (fluid.is(GenesisFluids.HOT_SPRING.get()) || fluid.is(GenesisFluids.HOT_SPRING_FLOWING.get()))) {
            event.setRed(0.55F);
            event.setGreen(0.95F);
            event.setBlue(0.95F);
        }
    }
}
