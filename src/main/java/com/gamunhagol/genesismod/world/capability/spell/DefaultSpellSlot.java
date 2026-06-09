package com.gamunhagol.genesismod.world.capability.spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class DefaultSpellSlot implements ISpellSlot {
    private static final String NBT_MEMORY_CAPACITY = "MemoryCapacity";
    private static final String NBT_SELECTED_SLOT = "SelectedSlot";
    private static final String NBT_EQUIPPED_SPELLS = "EquippedSpells";

    private int memoryCapacity = 3;
    private int selectedSlot = 0;

    private final int UI_SLOTS = 10;
    private final List<String> equippedSpells = new ArrayList<>();

    public DefaultSpellSlot() {
        for (int i = 0; i < UI_SLOTS; i++) {
            equippedSpells.add("");
        }
    }

    @Override
    public List<String> getEquippedSpells() {
        return this.equippedSpells;
    }

    @Override
    public void equipSpell(String spellId) {
        if (selectedSlot >= 0 && selectedSlot < UI_SLOTS) {
            if (spellId != null && !spellId.trim().isEmpty()) {
                for (int i = 0; i < UI_SLOTS; i++) {
                    if (spellId.equals(equippedSpells.get(i))) {
                        equippedSpells.set(i, "");
                    }
                }
            }
            equippedSpells.set(selectedSlot, spellId);
        }
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < UI_SLOTS) {
            this.selectedSlot = slot;
        }
    }

    @Override
    public int getMemoryCapacity() {
        return this.memoryCapacity;
    }

    @Override
    public void setMemoryCapacity(int capacity) {
        this.memoryCapacity = capacity;
    }

    @Override
    public void copyFrom(ISpellSlot source) {
        this.memoryCapacity = source.getMemoryCapacity();
        this.selectedSlot = source.getSelectedSlot();

        this.equippedSpells.clear();
        this.equippedSpells.addAll(source.getEquippedSpells());
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(NBT_MEMORY_CAPACITY, this.memoryCapacity);
        nbt.putInt(NBT_SELECTED_SLOT, this.selectedSlot);

        ListTag spellsList = new ListTag();
        for (String spell : this.equippedSpells) {
            spellsList.add(StringTag.valueOf(spell));
        }
        nbt.put(NBT_EQUIPPED_SPELLS, spellsList);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.memoryCapacity = nbt.getInt(NBT_MEMORY_CAPACITY);
        this.selectedSlot = nbt.getInt(NBT_SELECTED_SLOT);

        ListTag spellsList = nbt.getList(NBT_EQUIPPED_SPELLS, Tag.TAG_STRING);
        this.equippedSpells.clear();
        for (int i = 0; i < spellsList.size(); i++) {
            this.equippedSpells.add(spellsList.getString(i));
        }

        while (this.equippedSpells.size() < UI_SLOTS) {
            this.equippedSpells.add("");
        }
    }
}