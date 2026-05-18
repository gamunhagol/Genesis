package com.gamunhagol.genesismod.world.capability.spell;

import java.util.List;

public interface ISpellSlot {
    List<String> getEquippedSpells();
    void equipSpell(String spellId); // 아이템 사용 시 호출
    int getSelectedSlot();
    void setSelectedSlot(int slot);
    int getMaxSlots();
    void setMaxSlots(int max);

    void copyFrom(ISpellSlot source);
}
