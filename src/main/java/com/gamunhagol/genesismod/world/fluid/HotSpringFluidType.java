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

public class HotSpringFluidType extends FluidType {

    // [최적화 1] 텍스처 경로는 불변이므로 상수로 선언
    private static final ResourceLocation WATER_STILL = new ResourceLocation("minecraft", "block/water_still");
    private static final ResourceLocation WATER_FLOW = new ResourceLocation("minecraft", "block/water_flow");

    // [최적화 2] 안개 색상을 상수로 정의 (한 곳에서만 수정하면 됨)
    private static final Vector3f FOG_COLOR = new Vector3f(0.55F, 0.95F, 0.95F);
    private static final int TINT_COLOR = 0xFF00E4E4;

    public HotSpringFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() { return WATER_STILL; }

            @Override
            public ResourceLocation getFlowingTexture() { return WATER_FLOW; }

            @Override
            public int getTintColor() { return TINT_COLOR; }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // [최적화 3] 매 프레임 new Vector3f() 하지 않고, 미리 만들어둔 상수 반환
                return FOG_COLOR;
            }

            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount, FogType fogType) {
                RenderSystem.setShaderFogStart(0.5F);
                RenderSystem.setShaderFogEnd(8.0F);
                // [최적화 4] 위에서 정의한 상수 값을 재사용 (실수 방지)
                RenderSystem.setShaderFogColor(FOG_COLOR.x(), FOG_COLOR.y(), FOG_COLOR.z());
            }
        });
    }
}
