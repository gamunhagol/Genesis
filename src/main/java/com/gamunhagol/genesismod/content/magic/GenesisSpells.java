package com.gamunhagol.genesismod.content.magic;


import com.gamunhagol.genesismod.content.magic.miracles.HealMiracle;
import com.gamunhagol.genesismod.content.magic.spells.nature.*;
import com.gamunhagol.genesismod.content.magic.spells.summon.*;

import java.util.HashMap;
import java.util.Map;

public class GenesisSpells {
    private static final Map<String, AbstractSpell> SPELLS = new HashMap<>();

    public static final AbstractSpell FIREBALL = register(new FireballSpell());
    public static final AbstractSpell LARGE_FIREBALL = register(new LargeFireballSpell());
    public static final AbstractSpell SHULKER_BULLET = register(new ShulkerBulletSpell());
    public static final AbstractSpell GUARDIAN_BEAM = register(new GuardianBeamSpell());
    public static final AbstractSpell HEAVY_GUARDIAN_BEAM = register(new HeavyGuardianBeamSpell());

    public static final AbstractSpell SUMMON_ZOMBIE = register(new SummonZombieSpell());
    public static final AbstractSpell SUMMON_ARMORED_ZOMBIE = register(new SummonAZombieSpell());
    public static final AbstractSpell SUMMON_SKELETON_SLAVE = register(new SummonSkeletonSlaveSpell());
    public static final AbstractSpell SUMMON_SKELETON = register(new SummonSkeletonSpell());
    public static final AbstractSpell SUMMON_GREAT_BOW_SKELETON = register(new SummonGBSkeletonSpell());
    public static final AbstractSpell SUMMON_ARMORED_SKELETON = register(new SummonASkeletonSpell());
    public static final AbstractSpell SUMMON_WARDEN = register(new SummonWardenSpell());

    public static final AbstractSpell SUMMON_BLAZE = register(new SummonBlazeSpell());

    public static final AbstractSpell LITTLE_HEAL = register(new HealMiracle());

    private static AbstractSpell register(AbstractSpell spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static AbstractSpell get(String id) {
        return SPELLS.get(id);
    }
}