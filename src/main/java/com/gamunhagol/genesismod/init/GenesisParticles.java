package com.gamunhagol.genesismod.init;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GenesisMod.MODID);

    public static final RegistryObject<SimpleParticleType> GREEN_FLAME =
            PARTICLE_TYPES.register("oblivion_fire_flame", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> MAGIC_MIST =
            PARTICLE_TYPES.register("magic_mist", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> LITTLE_STAR =
            PARTICLE_TYPES.register("little_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FAILED_STAR =
            PARTICLE_TYPES.register("failed_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> BLUE_STAR =
            PARTICLE_TYPES.register("blue_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> DIMENSION_STAR =
            PARTICLE_TYPES.register("dimension_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> MAGIC_LIGHTNING =
            PARTICLE_TYPES.register("magic_lightning", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> DIVINE_LIGHT =
            PARTICLE_TYPES.register("divine_light", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> MAGICAL_LIGHT =
            PARTICLE_TYPES.register("magical_light", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> TWINKLING_STAR =
            PARTICLE_TYPES.register("twinkling_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> LIGHTING_STAR =
            PARTICLE_TYPES.register("lighting_star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> DISRUPTION =
            PARTICLE_TYPES.register("disruption", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
