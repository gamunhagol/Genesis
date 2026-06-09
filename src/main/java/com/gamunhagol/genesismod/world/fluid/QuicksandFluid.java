package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class QuicksandFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GenesisFluidTypes.QUICKSAND_TYPE,
                    GenesisFluids.QUICKSAND,
                    GenesisFluids.QUICKSAND_FLOWING
            )
                    .bucket(GenesisItems.QUICKSAND_BUCKET)
                    .block(GenesisBlocks.QUICKSAND_BLOCK)
                    .slopeFindDistance(2)
                    .levelDecreasePerBlock(2)
                    .tickRate(20)
                    .explosionResistance(100f);
}