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

    // --- 유사 (Quicksand) 타입 ---
    public static final RegistryObject<FluidType> QUICKSAND_TYPE =
            FLUID_TYPES.register("quicksand", () ->
                    new QuicksandFluidType(FluidType.Properties.create()
                            .density(3000)      // 물보다 무겁게
                            .viscosity(6000)    // 용암보다 끈적하게
                            .motionScale(0.007F) // [중요] 0.01보다 낮춰서 더 꾸덕하게 이동
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
                            .density(1100)   // 물보다 살짝 무겁게
                            .viscosity(1500) // 살짝 끈적하게
                            .canPushEntity(true)
                            .canDrown(true)
                            .pathType(BlockPathTypes.WATER)
                            .adjacentPathType(BlockPathTypes.WATER)
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    ));
}

