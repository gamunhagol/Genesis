package com.gamunhagol.genesismod.content.magic;


import com.gamunhagol.genesismod.content.magic.miracles.cure.HealMiracle;
import com.gamunhagol.genesismod.content.magic.miracles.earth.ConjureStoneMiracle;
import com.gamunhagol.genesismod.content.magic.miracles.earth.ConjureStoneWallMiracle;
import com.gamunhagol.genesismod.content.magic.miracles.earth.RockSphereMiracle;
import com.gamunhagol.genesismod.content.magic.spells.nature.*;
import com.gamunhagol.genesismod.content.magic.spells.operation.*;
import com.gamunhagol.genesismod.content.magic.spells.sorcery.*;
import com.gamunhagol.genesismod.content.magic.spells.summon.*;

import java.util.HashMap;
import java.util.Map;

public class GenesisSpells {
    private static final Map<String, AbstractSpell> SPELLS = new HashMap<>();

    public static final AbstractSpell FIREBALL = register(new FireballSpell());
    public static final AbstractSpell LARGE_FIREBALL = register(new LargeFireballSpell());
    public static final AbstractSpell SHULKER_BULLET = register(new ShulkerBulletSpell());
    public static final AbstractSpell EVOKER_FANGS_LINE = register(new EvokerFangsLineSpell());
    public static final AbstractSpell EVOKER_FANGS_CIRCLE = register(new EvokerFangsCircleSpell());
    public static final AbstractSpell GUARDIAN_BEAM = register(new GuardianBeamSpell());
    public static final AbstractSpell HEAVY_GUARDIAN_BEAM = register(new HeavyGuardianBeamSpell());
    public static final AbstractSpell DRAGON_BREATH = register(new DragonBreathSpell());
    public static final AbstractSpell WHITHER_SKULL = register(new WitherSkullSpell());
    public static final AbstractSpell WARDEN_SONIC_BOOM = register(new WardenSonicBoomSpell());

    public static final AbstractSpell MINING = register(new MiningSpell());
    public static final AbstractSpell BLINK = register(new BlinkSpell());
    public static final AbstractSpell EXPLOSION = register(new ExplosionSpell());
    public static final AbstractSpell THROW_BOMB = register(new ThrowBombSpell());
    public static final AbstractSpell PUSH_BLOCK = register(new PushBlockSpell());
    public static final AbstractSpell ARROW_RAIN = register(new ArrowRainSpell());
    public static final AbstractSpell ARROW_BOMBARDMENT = register(new ArrowBombardmentSpell());
    public static final AbstractSpell SPATIAL_RUPTURE = register(new SpatialRuptureSpell());


    public static final AbstractSpell MAGIC_GLINT = register(new MagicGlintSpell());
    public static final AbstractSpell MAGIC_MIST = register(new MagicMistSpell());
    public static final AbstractSpell MAGIC_PEBBLE = register(new MagicPebbleSpell());
    public static final AbstractSpell GREAT_MAGIC_PEBBLE = register(new GreatMagicPebbleSpell());
    public static final AbstractSpell MAGIC_COMET = register(new MagicCometSpell());
    public static final AbstractSpell SHOOTING_STAR = register(new ShootingStarSpell());

    public static final AbstractSpell MAGIC_METEOR = register(new MagicMeteorSpell());
    public static final AbstractSpell CLUSTER_METEOR = register(new ClusterMeteorSpell());
    public static final AbstractSpell METEOR_BARRAGE = register(new MeteorBarrageSpell());

    public static final AbstractSpell FAINT_STAR = register(new FaintStarSpell());
    public static final AbstractSpell FAILED_STAR = register(new FailedStarSpell());
    public static final AbstractSpell DIMENSIONAL_STAR = register(new DimensionalStarSpell());

    public static final AbstractSpell SUMMON_MAGIC_SWORD = register(new SMSSwordsSpell());
    public static final AbstractSpell SUMMON_LINKED_MAGIC_SWORD = register(new SLMSSwordsSpell());
    public static final AbstractSpell SUMMON_MAGIC_SWORD_PHALANX = register(new SMSPSwordsSpell());

    public static final AbstractSpell MAGIC_DOMAIN = register(new MagicDomainSpell());
    public static final AbstractSpell SEA_OF_STARS = register(new SeaOfStarsSpell());

    public static final AbstractSpell FALLING_STAR_SEA = register(new FallingStarSeaSpell());
    public static final AbstractSpell DIVIDED = register(new DividedSpell());
    public static final AbstractSpell STAR_GASP = register(new StarGaspSpell());
    public static final AbstractSpell SUPERNOVA = register(new SupernovaSpell());


    public static final AbstractSpell SUMMON_ZOMBIE = register(new SummonZombieSpell());
    public static final AbstractSpell SUMMON_HUSK = register(new SummonHuskSpell());
    public static final AbstractSpell SUMMON_DROWNED = register(new SummonDrownedSpell());
    public static final AbstractSpell SUMMON_ARMORED_ZOMBIE = register(new SummonAZombieSpell());
    public static final AbstractSpell SUMMON_SKELETON_SLAVE = register(new SummonSkeletonSlaveSpell());
    public static final AbstractSpell SUMMON_SKELETON = register(new SummonSkeletonSpell());
    public static final AbstractSpell SUMMON_WITHER_SKELETON = register(new SummonWitherSkeletonSpell());
    public static final AbstractSpell SUMMON_STRAY = register(new SummonStraySpell());
    public static final AbstractSpell SUMMON_GREAT_BOW_SKELETON = register(new SummonGBSkeletonSpell());
    public static final AbstractSpell SUMMON_ARMORED_SKELETON = register(new SummonASkeletonSpell());
    public static final AbstractSpell SUMMON_WARDEN = register(new SummonWardenSpell());

    public static final AbstractSpell SUMMON_BLAZE = register(new SummonBlazeSpell());

    public static final AbstractSpell SUMMON_VEX = register(new SummonVexSpell());
    public static final AbstractSpell SUMMON_MASS_VEX = register(new SummonMassVexSpell());

    public static final AbstractSpell LITTLE_HEAL = register(new HealMiracle());

    public static final AbstractSpell CONJURE_STONE = register(new ConjureStoneMiracle());
    public static final AbstractSpell CONJURE_STONE_WALL = register(new ConjureStoneWallMiracle());
    public static final AbstractSpell ROCK_SPHERE = register(new RockSphereMiracle());

    private static AbstractSpell register(AbstractSpell spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static AbstractSpell get(String id) {
        return SPELLS.get(id);
    }
}