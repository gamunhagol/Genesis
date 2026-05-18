package com.gamunhagol.genesismod.world.capability.spell;

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

public class SpellSlotProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ISpellSlot> SPELL_SLOT = CapabilityManager.get(new CapabilityToken<>() { });

    private DefaultSpellSlot spellSlot = null;
    private final LazyOptional<ISpellSlot> optional = LazyOptional.of(this::createSpellSlot);

    private DefaultSpellSlot createSpellSlot() {
        if (this.spellSlot == null) {
            this.spellSlot = new DefaultSpellSlot();
        }
        return this.spellSlot;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SPELL_SLOT) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSpellSlot().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSpellSlot().loadNBTData(nbt);
    }
}
