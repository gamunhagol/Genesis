package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedZombieEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class SummonZombieSpell extends AbstractSummonSpell {

    public SummonZombieSpell() {
        super("summon_zombie");
    }

    @Override
    public int getCastTime() {
        return 40;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 12);
    }

    @Override
    public float getMentalCost() {
        return 5.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        return new SummonedZombieEntity(GenesisEntities.SUMMONED_ZOMBIE.get(), level);
    }
}