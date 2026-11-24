package com.gamunhagol.genesismod.client.particle;

import com.gamunhagol.genesismod.init.GenesisParticles;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.main.EpicFightMod;

public class GenesisTrails {
    public static final TrailInfo LARGE_ARROW_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.15D, 0.0D, 0.8D))
            .endPos(new Vec3(0.15D, 0.0D, 0.8D))
            .interpolations(4)
            .lifetime(10)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo MAGIC_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.12D, 0.0D, 0.5D))
            .endPos(new Vec3(0.12D, 0.0D, 0.5D))
            .interpolations(4)
            .lifetime(9)
            .updateInterval(1)
            .r(0.2F).g(0.85F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo COMET_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.3D, 0.0D, 0.6D))
            .endPos(new Vec3(0.3D, 0.0D, 0.6D))
            .interpolations(5)
            .lifetime(14)
            .updateInterval(1)
            .r(0.45F).g(0.6F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo LIGHTNING_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.15D, 0.0D, 0.9D))
            .endPos(new Vec3(0.15D, 0.0D, 0.9D))
            .interpolations(4)
            .lifetime(8)
            .updateInterval(1)
            .r(1.0F).g(0.92F).b(0.25F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();
}
