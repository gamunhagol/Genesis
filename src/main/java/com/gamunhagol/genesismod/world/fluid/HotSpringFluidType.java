package com.gamunhagol.genesismod.world.fluid;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class HotSpringFluidType extends FluidType {

    public HotSpringFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            private static final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/water_still");
            private static final ResourceLocation FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
            private static final ResourceLocation OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

            @Override
            public ResourceLocation getStillTexture() { return STILL; }

            @Override
            public ResourceLocation getFlowingTexture() { return FLOW; }

            @Override
            public ResourceLocation getOverlayTexture() { return OVERLAY; }

            @Override
            public int getTintColor() {
                return 0xFF00E4E4; // 밝은 청록색 (온천수)
            }


            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // 시야 내부 색감 - 물속 느낌
                return new Vector3f(0.0F, 0.8F, 0.8F);
            }

            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount,
                                        FogType fogType) {
                // 내부 안개 범위
                RenderSystem.setShaderFogStart(0.5F);
                RenderSystem.setShaderFogEnd(5.0F);
            }
        });
    }
}
