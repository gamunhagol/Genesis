package com.gamunhagol.genesismod.content.magic;


import com.gamunhagol.genesismod.content.magic.miracles.HealMiracle;
import com.gamunhagol.genesismod.content.magic.spells.*;

import java.util.HashMap;
import java.util.Map;

public class GenesisSpells {
    private static final Map<String, AbstractSpell> SPELLS = new HashMap<>();

    public static final AbstractSpell FIREBALL = register(new FireballSpell());
    public static final AbstractSpell HEAL = register(new HealMiracle());
    public static final AbstractSpell SUMMON_ZOMBIE = register(new SummonZombieSpell());
    public static final AbstractSpell SUMMON_ARMORED_ZOMBIE = register(new SummonAZombieSpell());
    public static final AbstractSpell SUMMON_SKELETON_SLAVE = register(new SummonSkeletonSlaveSpell());
    public static final AbstractSpell SUMMON_SKELETON = register(new SummonSkeletonSpell());
    public static final AbstractSpell SUMMON_GREAT_BOW_SKELETON = register(new SummonGBSkeletonSpell());
    public static final AbstractSpell SUMMON_ARMORED_SKELETON = register(new SummonASkeletonSpell());

    private static AbstractSpell register(AbstractSpell spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static AbstractSpell get(String id) {
        return SPELLS.get(id);
    }
}