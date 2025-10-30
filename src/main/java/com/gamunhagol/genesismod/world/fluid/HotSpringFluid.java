package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class HotSpringFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GenesisFluidTypes.HOT_SPRING_TYPE,
                    GenesisFluids.HOT_SPRING,
                    GenesisFluids.HOT_SPRING_FLOWING
            )
                    .bucket(GenesisItems.HOT_SPRING_BUCKET)
                    .block(GenesisBlocks.HOT_SPRING_BLOCK)
                    .slopeFindDistance(4)
                    .levelDecreasePerBlock(1)
                    .tickRate(5)
                    .explosionResistance(100f);
}

