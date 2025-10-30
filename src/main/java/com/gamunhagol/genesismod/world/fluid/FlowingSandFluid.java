package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FlowingSandFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GenesisFluidTypes.FLOWING_SAND_TYPE,
                    GenesisFluids.FLOWING_SAND,           // Source
                    GenesisFluids.FLOWING_SAND_FLOWING    // Flowing
            )
                    .bucket(GenesisItems.FLOWING_SAND_BUCKET)
                    .block(GenesisBlocks.FLOWING_SAND_BLOCK)
                    .slopeFindDistance(2)
                    .levelDecreasePerBlock(2)
                    .tickRate(12)
                    .explosionResistance(100f);
}

