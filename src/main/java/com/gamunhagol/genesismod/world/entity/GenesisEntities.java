package com.gamunhagol.genesismod.world.entity;

import com.gamunhagol.genesismod.world.entity.etc.ArrowBarrageSpawnerEntity;
import com.gamunhagol.genesismod.world.entity.mob.*;
import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
import com.gamunhagol.genesismod.world.entity.projectile.magic.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GenesisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static final RegistryObject<EntityType<Collector>> COLLECTOR;

    public static final RegistryObject<EntityType<CollectorGuard>> COLLECTOR_GUARD;
    public static final RegistryObject<EntityType<SummonedZombieEntity>> SUMMONED_ZOMBIE;
    public static final RegistryObject<EntityType<SummonedHuskEntity>> SUMMONED_HUSK;
    public static final RegistryObject<EntityType<SummonedDrownedEntity>> SUMMONED_DROWNED;
    public static final RegistryObject<EntityType<SummonedSkeletonEntity>> SUMMONED_SKELETON;
    public static final RegistryObject<EntityType<SummonedWitherSkeletonEntity>> SUMMONED_WITHER_SKELETON;
    public static final RegistryObject<EntityType<SummonedStrayEntity>> SUMMONED_STRAY;
    public static final RegistryObject<EntityType<SummonedWardenEntity>> SUMMONED_WARDEN;

    public static final RegistryObject<EntityType<SummonedVexEntity>> SUMMONED_VEX;

    public static final RegistryObject<EntityType<SummonedBlazeEntity>> SUMMONED_BLAZE;

    public static final RegistryObject<EntityType<SpellGuardianEntity>> SPELL_GUARDIAN;
    public static final RegistryObject<EntityType<SpellElderGuardianEntity>> SPELL_ELDER_GUARDIAN;

    public static final RegistryObject<EntityType<ArrowBarrageSpawnerEntity>> ARROW_BARRAGE_SPAWNER;

    public static final RegistryObject<EntityType<LargeArrowEntity>> LARGE_ARROW;

    public static final RegistryObject<EntityType<MagicGlint>> MAGIC_GLINT;
    public static final RegistryObject<EntityType<MagicMist>> MAGIC_MIST;
    public static final RegistryObject<EntityType<MagicPebble>> MAGIC_PEBBLE;
    public static final RegistryObject<EntityType<GreatMagicPebble>> GREAT_MAGIC_PEBBLE;
    public static final RegistryObject<EntityType<MagicComet>> MAGIC_COMET;
    public static final RegistryObject<EntityType<ShootingStar>> SHOOTING_STAR;

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "genesis");

        COLLECTOR = ENTITY_TYPES.register("collector", () -> EntityType.Builder.of(Collector::new, MobCategory.CREATURE)
                .sized(0.6f, 1.95f).build("collector"));

        COLLECTOR_GUARD = ENTITY_TYPES.register("collector_guard", () -> EntityType.Builder.of(CollectorGuard::new, MobCategory.CREATURE)
                .sized(0.6f, 1.95f).build("collector_guard"));

        SUMMONED_ZOMBIE = ENTITY_TYPES.register("summoned_zombie",
                () -> EntityType.Builder.of(SummonedZombieEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_zombie"));

        SUMMONED_HUSK = ENTITY_TYPES.register("summoned_husk",
                () -> EntityType.Builder.of(SummonedHuskEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_husk"));

        SUMMONED_DROWNED = ENTITY_TYPES.register("summoned_drowned",
                () -> EntityType.Builder.of(SummonedDrownedEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_drowned"));

        SUMMONED_SKELETON = ENTITY_TYPES.register("summoned_skeleton",
                () -> EntityType.Builder.of(SummonedSkeletonEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_skeleton"));

        SUMMONED_WITHER_SKELETON = ENTITY_TYPES.register("summoned_wither_skeleton",
                () -> EntityType.Builder.of(SummonedWitherSkeletonEntity::new, MobCategory.CREATURE)
                        .fireImmune()
                        .sized(0.7f, 2.4f).build("summoned_wither_skeleton"));

        SUMMONED_STRAY = ENTITY_TYPES.register("summoned_stray",
                () -> EntityType.Builder.of(SummonedStrayEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.99f).build("summoned_stray"));

        SUMMONED_WARDEN = ENTITY_TYPES.register("summon_warden",
                () -> EntityType.Builder.of(SummonedWardenEntity::new, MobCategory.CREATURE)
                        .sized(0.9f, 2.9f).build("summon_warden"));


        SPELL_GUARDIAN = ENTITY_TYPES.register("spell_guardian",
                () -> EntityType.Builder.of(SpellGuardianEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.8f).clientTrackingRange(8)
                        .build("spell_guardian"));
        SPELL_ELDER_GUARDIAN = ENTITY_TYPES.register("spell_elder_guardian",
                () -> EntityType.Builder.of(SpellElderGuardianEntity::new, MobCategory.CREATURE)
                        .sized(1.9975f, 1.9975f).clientTrackingRange(8)
                        .build("spell_elder_guardian"));




        SUMMONED_BLAZE = ENTITY_TYPES.register("summon_blaze",
                () -> EntityType.Builder.of(SummonedBlazeEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.8f).clientTrackingRange(8)
                        .fireImmune()
                        .build("summon_blaze"));



        SUMMONED_VEX = ENTITY_TYPES.register("summoned_vex",
                () -> EntityType.Builder.of(SummonedVexEntity::new, MobCategory.CREATURE)
                        .sized(0.4f, 0.8f).clientTrackingRange(8)
                        .build("summoned_vex"));


        ARROW_BARRAGE_SPAWNER = ENTITY_TYPES.register("arrow_barrage_spawner", () ->
                EntityType.Builder.<ArrowBarrageSpawnerEntity>of(ArrowBarrageSpawnerEntity::new, MobCategory.MISC)
                        .sized(0.1f, 0.1f).clientTrackingRange(4).updateInterval(20)
                        .build("arrow_barrage_spawner"));


        LARGE_ARROW = ENTITY_TYPES.register("large_arrow", () ->
                EntityType.Builder.<LargeArrowEntity>of(LargeArrowEntity::new, MobCategory.MISC)
                        .sized(0.7f, 0.7f).clientTrackingRange(4).updateInterval(20)
                        .build("large_arrow"));


        MAGIC_GLINT = ENTITY_TYPES.register("magic_glint", () ->
                EntityType.Builder.<MagicGlint>of(MagicGlint::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                        .build("magic_glint"));

        MAGIC_MIST = ENTITY_TYPES.register("magic_mist", () ->
                EntityType.Builder.<MagicMist>of(MagicMist::new, MobCategory.MISC)
                        .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
                        .build("magic_mist"));

        MAGIC_PEBBLE = ENTITY_TYPES.register("magic_pebble", () ->
                EntityType.Builder.<MagicPebble>of(MagicPebble::new, MobCategory.MISC)
                        .sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10)
                        .build("magic_pebble"));

        GREAT_MAGIC_PEBBLE = ENTITY_TYPES.register("great_magic_pebble", () ->
                EntityType.Builder.<GreatMagicPebble>of(GreatMagicPebble::new, MobCategory.MISC)
                        .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
                        .build("great_magic_pebble"));

        MAGIC_COMET = ENTITY_TYPES.register("magic_comet", () ->
                EntityType.Builder.<MagicComet>of(MagicComet::new, MobCategory.MISC)
                        .sized(0.7F, 0.7F).clientTrackingRange(6).updateInterval(10)
                        .build("magic_comet"));

        SHOOTING_STAR = ENTITY_TYPES.register("shooting_star", () ->
                EntityType.Builder.<ShootingStar>of(ShootingStar::new, MobCategory.MISC)
                        .sized(1.0F, 1.0F).clientTrackingRange(8).updateInterval(10)
                        .build("shooting_star"));
    }
}