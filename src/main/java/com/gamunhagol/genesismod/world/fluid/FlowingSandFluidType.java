package com.gamunhagol.genesismod.world.fluid;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class FlowingSandFluidType extends FluidType {

    public static FlowingSandFluidType INSTANCE; // 외부 참조용

    public FlowingSandFluidType(Properties properties) {
        super(properties);
        INSTANCE = this;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            private static final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/sand");
            private static final ResourceLocation FLOW = ResourceLocation.withDefaultNamespace("block/sand");
            private static final ResourceLocation OVERLAY = ResourceLocation.withDefaultNamespace("block/lava_overlay");

            @Override
            public ResourceLocation getStillTexture() { return STILL; }

            @Override
            public ResourceLocation getFlowingTexture() { return FLOW; }

            @Override
            public ResourceLocation getOverlayTexture() { return OVERLAY; }

            @Override
            public int getTintColor() {
                return 0xFFE3DBB0; // 기본 모래색
            }


            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // 모래 안에서의 시야 색감 (불투명한 황토색)
                return new Vector3f(0.85F, 0.75F, 0.45F);
            }

            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount,
                                        FogType fogType) {
                RenderSystem.setShaderFogStart(0.3F);
                RenderSystem.setShaderFogEnd(2.5F); // 매우 가까운 거리까지 안개
            }


            public boolean renderOverlayTexture(net.minecraft.world.level.Level level,
                                                net.minecraft.core.BlockPos pos,
                                                net.minecraft.world.level.material.FluidState state) {
                return true; // 색 표현 유지
            }
        });
    }
}