package com.gamunhagol.genesismod.content.magic;


import com.gamunhagol.genesismod.content.magic.miracles.HealMiracle;
import com.gamunhagol.genesismod.content.magic.spells.FireballSpell;
import com.gamunhagol.genesismod.content.magic.spells.SummonZombieSpell;

import java.util.HashMap;
import java.util.Map;

public class GenesisSpells {
    private static final Map<String, AbstractSpell> SPELLS = new HashMap<>();

    public static final AbstractSpell FIREBALL = register(new FireballSpell());
    public static final AbstractSpell HEAL = register(new HealMiracle());
    public static final AbstractSpell SUMMON_ZOMBIE = register(new SummonZombieSpell());

    private static AbstractSpell register(AbstractSpell spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static AbstractSpell get(String id) {
        return SPELLS.get(id);
    }
}