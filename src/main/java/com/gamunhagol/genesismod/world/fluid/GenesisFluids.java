package com.gamunhagol.genesismod.world.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.gamunhagol.genesismod.main.GenesisMod;

public class GenesisFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, GenesisMod.MODID);

    public static final RegistryObject<FlowingFluid> HOT_SPRING =
            FLUIDS.register("hot_spring", () -> new ForgeFlowingFluid.Source(HotSpringFluid.PROPERTIES));
    public static final RegistryObject<FlowingFluid> HOT_SPRING_FLOWING =
            FLUIDS.register("hot_spring_flowing", () -> new ForgeFlowingFluid.Flowing(HotSpringFluid.PROPERTIES));
}