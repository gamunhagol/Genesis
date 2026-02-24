package com.gamunhagol.genesismod.world.block;


import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

public class BloodFluidBlock extends LiquidBlock {
    public BloodFluidBlock(RegistryObject<FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
}