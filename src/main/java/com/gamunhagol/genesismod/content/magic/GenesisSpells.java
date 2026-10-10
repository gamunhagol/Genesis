package com.gamunhagol.genesismod.content.magic;


import com.gamunhagol.genesismod.content.magic.miracles.cure.HealMiracle;
import com.gamunhagol.genesismod.content.magic.miracles.earth.*;
import com.gamunhagol.genesismod.content.magic.miracles.fire.*;
import com.gamunhagol.genesismod.content.magic.miracles.water.*;
import com.gamunhagol.genesismod.content.magic.miracles.wind.*;
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

    public static final AbstractSpell THROW_STONE = register(new ThrowStoneMiracle());
    public static final AbstractSpell PULL_ROCK = register(new PullRockMiracle());
    public static final AbstractSpell EARTH_EXTRACT = register(new EarthExtractMiracle());
    public static final AbstractSpell BURIAL = register(new BurialMiracle());

    public static final AbstractSpell PATAMU_WRATH = register(new PatamuWrathSpell());
    public static final AbstractSpell PATAMU_ROAR = register(new PatamuRoarSpell());

    public static final AbstractSpell FLOOD = register(new FloodMiracle());
    public static final AbstractSpell SEA_LUCK = register(new SeaLuckMiracle());
    public static final AbstractSpell SEA_SERPENT_FIN = register(new SeaSerpentFinMiracle());

    public static final AbstractSpell WATER_SPRAY = register(new WaterSprayMiracle());
    public static final AbstractSpell SEA_SERPENT_BREATH = register(new SeaSerpentBreathMiracle());
    public static final AbstractSpell SEA_SERPENT_SCALE = register(new SeaSerpentScaleMiracle());
    public static final AbstractSpell SEA_CURSE = register(new SeaCurseMiracle());

    public static final AbstractSpell AGFLO_JAWS = register(new AgfloJawsSpell());
    public static final AbstractSpell WATERSPOUT = register(new WaterspoutSpell());

    public static final AbstractSpell BIO_SMELTER = register(new BioSmelterMiracle());
    public static final AbstractSpell TINDER = register(new TinderMiracle());
    public static final AbstractSpell FIRE_GAZE = register(new FireGazeMiracle());
    public static final AbstractSpell FLAME_PROTECTION = register(new FlameProtectionMiracle());

    public static final AbstractSpell EMBER = register(new EmberMiracle());
    public static final AbstractSpell FLAME_GRASP = register(new FlameGraspMiracle());
    public static final AbstractSpell REALM_OF_FIRE = register(new RealmOfFireMiracle());
    public static final AbstractSpell FLAME_HAMMER = register(new FlameHammerMiracle());

    public static final AbstractSpell KAELO_CREATION = register(new KaeloCreationMiracle());
    public static final AbstractSpell FIRE_GOD_BLESSING = register(new FireGodBlessingMiracle());

    public static final AbstractSpell CLEAR_WEATHER = register(new ClearWeatherMiracle());
    public static final AbstractSpell HOME_MEAL = register(new HomeMealMiracle());
    public static final AbstractSpell STORM_PROTECTION = register(new StormProtectionMiracle());
    public static final AbstractSpell REFRESHING_BREEZE = register(new RefreshingBreezeMiracle());
    public static final AbstractSpell UPDRAFT = register(new UpdraftMiracle());

    public static final AbstractSpell WIND_FEATHER = register(new WindFeatherMiracle());
    public static final AbstractSpell REJECTION_STORM = register(new RejectionStormMiracle());
    public static final AbstractSpell STORM = register(new StormMiracle());
    public static final AbstractSpell BLADE_STORM = register(new BladeStormMiracle());

    public static final AbstractSpell LIEN_PROTECTION = register(new LienProtectionMiracle());
    public static final AbstractSpell GALE = register(new GaleMiracle());
    public static final AbstractSpell WIND_ANIMUS = register(new WindAnimusMiracle());

    private static AbstractSpell register(AbstractSpell spell) {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static AbstractSpell get(String id) {
        return SPELLS.get(id);
    }
}