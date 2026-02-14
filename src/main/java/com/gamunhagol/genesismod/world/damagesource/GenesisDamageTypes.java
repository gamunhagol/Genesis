package com.gamunhagol.genesismod.world.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GenesisDamageTypes {
    // 1. ResourceKey 정의
    public static final ResourceKey<DamageType> HOLY = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation("genesis", "holy"));

    public static final ResourceKey<DamageType> DESTRUCTION = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation("genesis", "destruction"));

    // 2. DamageSource를 쉽게 가져오기 위한 헬퍼 메서드
    public static DamageSource getSource(Level level, ResourceKey<DamageType> type) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type));
    }

    public static DamageSource getSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker);
    }
}

