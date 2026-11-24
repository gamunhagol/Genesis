package com.gamunhagol.genesismod.client.particle;

import com.gamunhagol.genesismod.init.GenesisParticles;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.main.EpicFightMod;

public class GenesisTrails {
    public static final TrailInfo LARGE_ARROW_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.18D, 0.0D, 0.75D))
            .endPos(new Vec3(0.18D, 0.0D, 0.75D))
            .interpolations(4)
            .lifetime(10)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo MAGIC_GLINT_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.03D, 0.1D, 0.09D))
            .endPos(new Vec3(0.03D, 0.1D, 0.0D))
            .interpolations(4)
            .lifetime(7)
            .updateInterval(1)
            .r(0.65F).g(0.93F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo MAGIC_PEBBLE_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.03D, 0.15D, 0.0D))
            .endPos(new Vec3(0.03D, 0.15D, 0.0D))
            .interpolations(4)
            .lifetime(8)
            .updateInterval(1)
            .r(0.2F).g(0.85F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo GREAT_MAGIC_PEBBLE_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.05D, 0.2D, 0.02D))
            .endPos(new Vec3(0.05D, 0.2D, 0.02D))
            .interpolations(4)
            .lifetime(9)
            .updateInterval(1)
            .r(0.08F).g(0.35F).b(0.95F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo MAGIC_COMET_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.06D, 0.25D, 0.07D))
            .endPos(new Vec3(0.06D, 0.25D, 0.07D))
            .interpolations(5)
            .lifetime(11)
            .updateInterval(1)
            .r(0.04F).g(0.18F).b(0.9F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo SHOOTING_STAR_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.07D, 0.4D, 0.07D))
            .endPos(new Vec3(0.07D, 0.4D, 0.07D))
            .interpolations(5)
            .lifetime(13)
            .updateInterval(1)
            .r(0.02F).g(0.08F).b(0.75F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();


    public static final TrailInfo MAGIC_METEOR_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.03D, 0.15D, 0.0D))
            .endPos(new Vec3(0.03D, 0.15D, 0.0D))
            .interpolations(4)
            .lifetime(10)
            .updateInterval(1)
            .r(0.28F).g(0.95F).b(0.82F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo SMALL_MAGIC_METEOR_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.03D, 0.1D, 0.09D))
            .endPos(new Vec3(0.03D, 0.1D, 0.0D))
            .interpolations(4)
            .lifetime(7)
            .updateInterval(1)
            .r(0.35F).g(0.88F).b(0.95F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo MAGIC_SWORD_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.12D, 0.2D, 0.4D))
            .endPos(new Vec3(0.12D, 0.2D, 0.4D))
            .interpolations(4)
            .lifetime(9)
            .updateInterval(1)
            .r(0.1F).g(0.5F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo GRAND_FALLING_STAR_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.35D, 0.45D, 0.4D))
            .endPos(new Vec3(0.35D, 0.45D, 0.4D))
            .interpolations(5)
            .lifetime(12)
            .updateInterval(1)
            .r(0.04F).g(0.18F).b(0.9F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo DIVIDED_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.35D, 0.45D, 0.4D))
            .endPos(new Vec3(0.35D, 0.45D, 0.4D))
            .interpolations(5)
            .lifetime(12)
            .updateInterval(1)
            .r(0.12F).g(1.0F).b(0.55F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();


    public static final TrailInfo PEBBLE_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.15D, 0.19D, 0.08D))
            .endPos(new Vec3(0.15D, 0.19D, 0.08D))
            .interpolations(4)
            .lifetime(8)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo ROCK_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.25D, 0.5D, 0.25D))
            .endPos(new Vec3(0.25D, 0.5D, 0.25D))
            .interpolations(4)
            .lifetime(10)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo LARGE_ROCK_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-1.0D, 1.25D, 0.0D))
            .endPos(new Vec3(1.0D, 1.25D, 0.0D))
            .interpolations(5)
            .lifetime(14)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo WIND_FEATHER_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.14D, 0.0D, 0.15D))
            .endPos(new Vec3(0.14D, 0.0D, 0.15D))
            .interpolations(4)
            .lifetime(8)
            .updateInterval(1)
            .r(1.0F).g(1.0F).b(1.0F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo LIGHTNING_ARROW_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.14D, 0.25D, 0.25D))
            .endPos(new Vec3(0.14D, 0.25D, 0.25D))
            .interpolations(4)
            .lifetime(8)
            .updateInterval(1)
            .r(1.0F).g(0.72F).b(0.15F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo LIGHTNING_SPEAR_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.2D, 0.3D, 0.3D))
            .endPos(new Vec3(0.2D, 0.3D, 0.3D))
            .interpolations(4)
            .lifetime(10)
            .updateInterval(1)
            .r(1.0F).g(0.72F).b(0.15F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();

    public static final TrailInfo VALA_ARROW_TRAIL = TrailInfo.builder()
            .type(GenesisParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.2D, 0.35D, 0.4D))
            .endPos(new Vec3(0.2D, 0.35D, 0.4D))
            .interpolations(5)
            .lifetime(12)
            .updateInterval(1)
            .r(1.0F).g(0.55F).b(0.08F)
            .texture(EpicFightMod.identifier("textures/particle/projectile_trail.png"))
            .create();
}
