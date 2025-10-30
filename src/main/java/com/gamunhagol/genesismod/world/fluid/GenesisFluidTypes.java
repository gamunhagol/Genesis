package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, GenesisMod.MODID);

    public static final RegistryObject<FluidType> HOT_SPRING_TYPE =
            FLUID_TYPES.register("hot_spring", () ->
                    new HotSpringFluidType(FluidType.Properties.create()
                            .density(1000)
                            .viscosity(1000)
                            .motionScale(0.01F)
                            .canPushEntity(true)
                            .supportsBoating(false)
                            .fallDistanceModifier(0.0F)
                            .canExtinguish(false)
                            .canHydrate(false)
                            .lightLevel(0)

                    ));

    public static final RegistryObject<FluidType> FLOWING_SAND_TYPE =
            FLUID_TYPES.register("flowing_sand", () ->
                    new FlowingSandFluidType(FluidType.Properties.create()
                            .density(2000)
                            .viscosity(3000)
                            .motionScale(0.002F)
                            .canPushEntity(true)
                            .supportsBoating(false)
                            .fallDistanceModifier(0.0F)
                            .canExtinguish(false)
                            .canHydrate(false)
                            .lightLevel(0)
                    ));
}

