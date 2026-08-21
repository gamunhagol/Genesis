package com.gamunhagol.genesismod.world.entity;

import com.gamunhagol.genesismod.world.entity.mob.*;
import com.gamunhagol.genesismod.world.entity.projectile.LargeArrowEntity;
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
    public static final RegistryObject<EntityType<SummonedSkeletonEntity>> SUMMONED_SKELETON;
    public static final RegistryObject<EntityType<SummonedWardenEntity>> SUMMONED_WARDEN;

    public static final RegistryObject<EntityType<SummonedBlazeEntity>> SUMMONED_BLAZE;

    public static final RegistryObject<EntityType<LargeArrowEntity>> LARGE_ARROW;

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "genesis");

        COLLECTOR = ENTITY_TYPES.register("collector", () -> EntityType.Builder.of(Collector::new, MobCategory.CREATURE)
                .sized(0.6f, 1.95f).build("collector"));

        COLLECTOR_GUARD = ENTITY_TYPES.register("collector_guard", () -> EntityType.Builder.of(CollectorGuard::new, MobCategory.CREATURE)
                .sized(0.6f, 1.95f).build("collector_guard"));

        SUMMONED_ZOMBIE = ENTITY_TYPES.register("summoned_zombie",
                () -> EntityType.Builder.of(SummonedZombieEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_zombie"));

        SUMMONED_SKELETON = ENTITY_TYPES.register("summoned_skeleton",
                () -> EntityType.Builder.of(SummonedSkeletonEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.95f).build("summoned_skeleton"));


        SUMMONED_BLAZE = ENTITY_TYPES.register("summon_blaze",
                () -> EntityType.Builder.of(SummonedBlazeEntity::new, MobCategory.CREATURE)
                        .sized(0.6f, 1.8f).clientTrackingRange(8)
                        .build("summon_blaze"));

        SUMMONED_WARDEN = ENTITY_TYPES.register("summon_warden",
                () -> EntityType.Builder.of(SummonedWardenEntity::new, MobCategory.CREATURE)
                        .sized(0.9f, 2.9f).build("summon_warden"));

        LARGE_ARROW = ENTITY_TYPES.register("large_arrow", () ->
                EntityType.Builder.<LargeArrowEntity>of(LargeArrowEntity::new, MobCategory.MISC)
                        .sized(0.7f, 0.7f).clientTrackingRange(4).updateInterval(20)
                        .build("large_arrow"));
    }
}