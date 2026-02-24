package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class BloodFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GenesisFluidTypes.BLOOD_TYPE,
                    GenesisFluids.BLOOD,
                    GenesisFluids.BLOOD_FLOWING
            )
                    .bucket(GenesisItems.BLOOD_BUCKET)
                    .block(GenesisBlocks.BLOOD_BLOCK)
                    .slopeFindDistance(3) // 물(4)보다 조금 덜 흐르게
                    .levelDecreasePerBlock(1)
                    .tickRate(10)         // 흐르는 속도를 물보다 약간 느리게 (숫자가 높을수록 느림)
                    .explosionResistance(100f);
}