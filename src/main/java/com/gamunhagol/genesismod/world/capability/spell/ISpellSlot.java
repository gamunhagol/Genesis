package com.gamunhagol.genesismod.world.capability.spell;

import java.util.List;

public interface ISpellSlot {
    List<String> getEquippedSpells();
    void equipSpell(String spellId);
    int getSelectedSlot();
    void setSelectedSlot(int slot);

    int getMemoryCapacity();
    void setMemoryCapacity(int capacity);

    void copyFrom(ISpellSlot source);
}
