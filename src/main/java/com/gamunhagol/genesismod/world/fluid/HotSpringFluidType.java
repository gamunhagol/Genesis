package com.gamunhagol.genesismod.world.fluid;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Consumer;

/**
 * 💧 HotSpringFluidType
 * 온천수 전용 FluidType 렌더 설정:
 * - 물 텍스처 기반
 * - 부드러운 하늘색 시야/안개 효과
 */
public class HotSpringFluidType extends FluidType {

    public HotSpringFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            private static final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/water_still");
            private static final ResourceLocation FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");

            @Override
            public ResourceLocation getStillTexture() {
                return STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOW;
            }

            @Override
            public int getTintColor() {
                return 0xFF00E4E4; // 밝은 청록색 (온천수)
            }

            // 시야 내부 색감 - 물속 느낌
            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return new Vector3f(0.55F, 0.95F, 0.95F); // 부드러운 하늘색
            }

            // 안개 거리 및 색상 (물속 표현)
            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount, FogType fogType) {
                RenderSystem.setShaderFogStart(0.5F);
                RenderSystem.setShaderFogEnd(8.0F);
                RenderSystem.setShaderFogColor(0.55F, 0.95F, 0.95F);
            }
        });
    }
}
