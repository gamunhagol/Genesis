package com.gamunhagol.genesismod.world.fluid;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, GenesisMod.MODID);


    public static final RegistryObject<FluidType> QUICKSAND_TYPE =
            FLUID_TYPES.register("quicksand", () ->
                    new QuicksandFluidType(FluidType.Properties.create()
                            .density(3000)
                            .viscosity(6000)
                            .motionScale(0.007F)
                            .canPushEntity(true)
                            .supportsBoating(false)
                            .fallDistanceModifier(0.0F)
                            .canDrown(true)
                            .lightLevel(0)
                    ));

    public static final RegistryObject<FluidType> BLOOD_TYPE =
            FLUID_TYPES.register("blood", () ->
                    new BloodFluidType(FluidType.Properties.create()
                            .descriptionId("fluid.genesis.blood")
                            .density(1100)
                            .viscosity(1500)
                            .canPushEntity(true)
                            .canDrown(true)
                            .pathType(BlockPathTypes.WATER)
                            .adjacentPathType(BlockPathTypes.WATER)
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    ));
}

