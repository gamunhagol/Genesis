package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.init.GenesisParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public enum MiracleElement {
    EARTH(GenesisParticles.EARTH),
    FIRE(GenesisParticles.FIRE),
    WATER(GenesisParticles.WATER),
    WIND(GenesisParticles.WIND),
    LIGHTNING(GenesisParticles.LIGHTNING),
    FOREST(GenesisParticles.FOREST),
    COLD(GenesisParticles.ICE),
    LIFE(GenesisParticles.LIFE),
    CREATION(GenesisParticles.CREATION),
    NONE(null);

    @Nullable
    private final RegistryObject<SimpleParticleType> emblemParticle;

    MiracleElement(@Nullable RegistryObject<SimpleParticleType> emblemParticle) {
        this.emblemParticle = emblemParticle;
    }

    @Nullable
    public SimpleParticleType getEmblemParticle() {
        return this.emblemParticle != null ? this.emblemParticle.get() : null;
    }
}
