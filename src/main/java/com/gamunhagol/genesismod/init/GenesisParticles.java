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

    public static final RegistryObject<SimpleParticleType> RESISTANCE_BLESSING =
            PARTICLE_TYPES.register("resistance_blessing", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> EVOLUTION_MIMICRY =
            PARTICLE_TYPES.register("evolution_mimicry", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> TWISTED_MIMICRY =
            PARTICLE_TYPES.register("twisted_mimicry", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> AMBIENT_MIMICRY =
            PARTICLE_TYPES.register("ambient_mimicry", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> NECROMANCY =
            PARTICLE_TYPES.register("necromancy", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ELEMENTALISM =
            PARTICLE_TYPES.register("elementalism", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> CREATION =
            PARTICLE_TYPES.register("creation", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> OTHERWORLD =
            PARTICLE_TYPES.register("otherworld", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> COMET =
            PARTICLE_TYPES.register("comet", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> METEOR =
            PARTICLE_TYPES.register("meteor", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> STAR =
            PARTICLE_TYPES.register("star", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> OATH =
            PARTICLE_TYPES.register("oath", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> GALAXY =
            PARTICLE_TYPES.register("galaxy", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> EARTH =
            PARTICLE_TYPES.register("earth", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WATER =
            PARTICLE_TYPES.register("water", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FIRE =
            PARTICLE_TYPES.register("fire", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WIND =
            PARTICLE_TYPES.register("wind", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> LIGHTNING =
            PARTICLE_TYPES.register("lightning", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FOREST =
            PARTICLE_TYPES.register("forest", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> ICE =
            PARTICLE_TYPES.register("ice", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> FROST =
            PARTICLE_TYPES.register("frost", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> LIFE =
            PARTICLE_TYPES.register("life", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
