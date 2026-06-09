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
                    .slopeFindDistance(3)
                    .levelDecreasePerBlock(1)
                    .tickRate(10)
                    .explosionResistance(100f);
}