package com.gamunhagol.genesismod.world.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeaponStatsProvider implements ICapabilitySerializable<CompoundTag> {
    // 캡슐화(Capability) 등록을 위한 토큰
    public static final Capability<IGenesisWeaponStats> WEAPON_STATS = CapabilityManager.get(new CapabilityToken<>() {});

    private final GenesisWeaponStats backend = new GenesisWeaponStats();
    private final LazyOptional<IGenesisWeaponStats> optional = LazyOptional.of(() -> backend);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == WEAPON_STATS ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("HolyDamage", backend.getHolyDamage());
        nbt.putFloat("DestructionDamage", backend.getDestructionDamage());
        nbt.putInt("ReinforceLevel", backend.getReinforceLevel());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        backend.setHolyDamage(nbt.getFloat("HolyDamage"));
        backend.setDestructionDamage(nbt.getFloat("DestructionDamage"));
        backend.setReinforceLevel(nbt.getInt("ReinforceLevel"));
    }
}