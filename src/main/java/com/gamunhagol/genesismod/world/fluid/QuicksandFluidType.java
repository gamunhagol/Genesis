package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class QuicksandFluidType extends FluidType {

    // [최적화] 상수화: 텍스처 경로와 안개 색상을 미리 정의
    private static final ResourceLocation QUICKSAND_STILL = new ResourceLocation(GenesisMod.MODID, "block/quicksand_still");
    private static final ResourceLocation QUICKSAND_FLOW = new ResourceLocation(GenesisMod.MODID, "block/quicksand_flow");

    private static final Vector3f FOG_COLOR = new Vector3f(0.85F, 0.81F, 0.63F);

    public QuicksandFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() { return QUICKSAND_STILL; }

            @Override
            public ResourceLocation getFlowingTexture() { return QUICKSAND_FLOW; }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // 캐싱된 객체 반환 (메모리 절약)
                return FOG_COLOR;
            }

            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount, FogType fogType) {
                RenderSystem.setShaderFogStart(-1.0F);
                RenderSystem.setShaderFogEnd(2.0F);
                // 상수의 값 사용
                RenderSystem.setShaderFogColor(FOG_COLOR.x(), FOG_COLOR.y(), FOG_COLOR.z());
            }
        });
    }
}