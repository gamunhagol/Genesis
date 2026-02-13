package com.gamunhagol.genesismod.stats;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    // Capability 등록 토큰 (열쇠)
    public static final Capability<StatCapability> STAT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private StatCapability backend = null;
    private final LazyOptional<StatCapability> optional = LazyOptional.of(this::createStatCapability);

    private StatCapability createStatCapability() {
        if (this.backend == null) {
            this.backend = new StatCapability();
        }
        return this.backend;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == STAT_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.createStatCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.createStatCapability().deserializeNBT(nbt);
    }
}
