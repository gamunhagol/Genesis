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


@Mod.EventBusSubscriber(modid = GenesisMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidVisionHandler {

    private static FluidState getEyeFluid(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        BlockPos eyePos = BlockPos.containing(camera.getPosition());
        return mc.level.getFluidState(eyePos);
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        FluidState fluid = getEyeFluid(camera);
        if (fluid == null) return;

        if (fluid.is(GenesisFluids.QUICKSAND.get()) || fluid.is(GenesisFluids.QUICKSAND_FLOWING.get())) {
            event.setFogShape(FogShape.CYLINDER);

            float near = 0.0F;
            float far = 0.5F;

            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);

            RenderSystem.setShaderFogStart(near);
            RenderSystem.setShaderFogEnd(far);

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

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        FluidState fluid = getEyeFluid(camera);
        if (fluid == null) return;

        if (fluid.is(GenesisFluids.QUICKSAND.get()) || fluid.is(GenesisFluids.QUICKSAND_FLOWING.get())) {
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
