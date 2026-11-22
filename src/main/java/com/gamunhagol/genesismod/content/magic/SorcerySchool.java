package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.init.GenesisParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public enum SorcerySchool {
    EVOLUTION_IMITATION(GenesisParticles.EVOLUTION_MIMICRY),
    DISTORTED_IMITATION(GenesisParticles.TWISTED_MIMICRY),
    BACKGROUND_IMITATION(GenesisParticles.AMBIENT_MIMICRY),

    CREATION(GenesisParticles.CREATION),

    NECROMANCY(GenesisParticles.NECROMANCY),
    ELEMENTALISM(GenesisParticles.ELEMENTALISM),
    CONSTRUCT_SUMMON(GenesisParticles.CREATION),
    OTHERWORLD_LINK(GenesisParticles.OTHERWORLD),

    STAR_SLING(GenesisParticles.COMET),
    METEOR(GenesisParticles.METEOR),
    STELLAR(GenesisParticles.STAR),
    STAR_BLADE_FAITH(GenesisParticles.OATH),
    GALAXY_ORIGIN(GenesisParticles.GALAXY),

    NONE(null);

    @Nullable
    private final RegistryObject<SimpleParticleType> emblemParticle;

    SorcerySchool(@Nullable RegistryObject<SimpleParticleType> emblemParticle) {
        this.emblemParticle = emblemParticle;
    }

    @Nullable
    public SimpleParticleType getEmblemParticle() {
        return this.emblemParticle != null ? this.emblemParticle.get() : null;
    }
}
