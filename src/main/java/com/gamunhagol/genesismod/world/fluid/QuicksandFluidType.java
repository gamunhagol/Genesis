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

/**
 * 🏜️ QuicksandFluidType
 * 유사 전용 FluidType 렌더 설정:
 * - 용암 텍스처 기반 (더 꾸덕한 시각 효과)
 * - 짙은 갈색 안개/시야 (잠기면 앞이 안 보임)
 */
public class QuicksandFluidType extends FluidType {

    public QuicksandFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            // 텍스처를 lava로 바꾸면 water보다 훨씬 점도가 있어 보입니다.
            private static final ResourceLocation STILL = new ResourceLocation(GenesisMod.MODID, "block/quicksand_still");
            private static final ResourceLocation FLOW = new ResourceLocation(GenesisMod.MODID, "block/quicksand_flow");

            @Override
            public ResourceLocation getStillTexture() {
                return STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOW;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                           int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // 안개 색상: 진한 갈색 (R, G, B)
                return new Vector3f(0.85F, 0.81F, 0.63F);
            }

            public void modifyFogRender(Camera camera, float partialTick, ClientLevel level,
                                        int renderDistance, float darkenWorldAmount, FogType fogType) {
                // 유사 속에 잠기면 앞이 거의 안 보이도록 FogEnd를 짧게(2.0F) 설정
                RenderSystem.setShaderFogStart(-1.0F);
                RenderSystem.setShaderFogEnd(2.0F);
                RenderSystem.setShaderFogColor(0.85F, 0.81F, 0.63F);
            }
        });
    }
}