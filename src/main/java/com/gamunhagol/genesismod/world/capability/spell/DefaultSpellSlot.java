package com.gamunhagol.genesismod.world.capability.spell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class DefaultSpellSlot implements ISpellSlot {
    private static final String NBT_MAX_SLOTS = "MaxSlots";
    private static final String NBT_SELECTED_SLOT = "SelectedSlot";
    private static final String NBT_EQUIPPED_SPELLS = "EquippedSpells";

    private int maxSlots = 3; // 기본 슬롯 3개
    private int selectedSlot = 0;
    private final List<String> equippedSpells = new ArrayList<>();

    public DefaultSpellSlot() {
        // 초기 슬롯 세팅 (빈 슬롯은 빈 문자열로 처리)
        for (int i = 0; i < maxSlots; i++) {
            equippedSpells.add("");
        }
    }

    @Override
    public List<String> getEquippedSpells() {
        return this.equippedSpells;
    }

    @Override
    public void equipSpell(String spellId) {
        if (selectedSlot >= 0 && selectedSlot < maxSlots) {

            // 장착하려는 마법이 빈칸 지우기가 아니라 실제 마법일 경우
            if (spellId != null && !spellId.trim().isEmpty()) {
                for (int i = 0; i < maxSlots; i++) {
                    // 이미 다른 슬롯에 똑같은 마법이 있다면 그 자리를 비워줍니다.
                    if (spellId.equals(equippedSpells.get(i))) {
                        equippedSpells.set(i, "");
                    }
                }
            }

            // 선택된 슬롯에 마법 최종 장착
            equippedSpells.set(selectedSlot, spellId);
        }
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < maxSlots) {
            this.selectedSlot = slot;
        }
    }

    @Override
    public int getMaxSlots() {
        return this.maxSlots;
    }

    @Override
    public void setMaxSlots(int max) {
        this.maxSlots = max;
        // 슬롯이 늘어나면 빈 칸 추가
        while (equippedSpells.size() < maxSlots) {
            equippedSpells.add("");
        }
    }

    @Override
    public void copyFrom(ISpellSlot source) {
        this.maxSlots = source.getMaxSlots();
        this.selectedSlot = source.getSelectedSlot();
        this.equippedSpells.clear();
        this.equippedSpells.addAll(source.getEquippedSpells());
    }

    // --- NBT 저장 및 불러오기 ---
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(NBT_MAX_SLOTS, this.maxSlots);
        nbt.putInt(NBT_SELECTED_SLOT, this.selectedSlot);

        ListTag spellsList = new ListTag();
        for (String spell : this.equippedSpells) {
            spellsList.add(StringTag.valueOf(spell));
        }
        nbt.put(NBT_EQUIPPED_SPELLS, spellsList);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.maxSlots = nbt.getInt(NBT_MAX_SLOTS);
        this.selectedSlot = nbt.getInt(NBT_SELECTED_SLOT);

        ListTag spellsList = nbt.getList(NBT_EQUIPPED_SPELLS, Tag.TAG_STRING);
        this.equippedSpells.clear();
        for (int i = 0; i < spellsList.size(); i++) {
            this.equippedSpells.add(spellsList.getString(i));
        }
    }
}