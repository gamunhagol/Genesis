package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.SeaOfStarsEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SeaOfStarsSpell extends MagicSpell {
    public SeaOfStarsSpell() {
        super("sea_of_stars");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.INTELLIGENCE, 35);
    }

    @Override
    public float getMentalCost() {
        return 20.0f;
    }

    @Override
    public int getMemoryCost() {return 1;}

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        SeaOfStarsEntity seaOfStars = new SeaOfStarsEntity(level, caster, spellSnapshot);
        seaOfStars.setPos(caster.getX(), caster.getY(), caster.getZ());
        seaOfStars.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(seaOfStars);
    }
}