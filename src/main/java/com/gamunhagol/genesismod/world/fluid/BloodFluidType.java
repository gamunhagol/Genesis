package com.gamunhagol.genesismod.world.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;
import java.util.function.Consumer;

public class BloodFluidType extends FluidType {
    private static final ResourceLocation WATER_STILL = new ResourceLocation("minecraft", "block/water_still");
    private static final ResourceLocation WATER_FLOW = new ResourceLocation("minecraft", "block/water_flow");
    private static final int BLOOD_TINT = 0xFF800000; // 진한 빨강

    public BloodFluidType(Properties properties) {
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
            public int getTintColor() { return BLOOD_TINT; }

            // 안개 색상만 지정 (로직 없이 상수 반환)
            @Override
            public Vector3f modifyFogColor(net.minecraft.client.Camera camera, float partialTick, net.minecraft.client.multiplayer.ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return new Vector3f(0.4F, 0.0F, 0.0F);
            }
        });
    }
}