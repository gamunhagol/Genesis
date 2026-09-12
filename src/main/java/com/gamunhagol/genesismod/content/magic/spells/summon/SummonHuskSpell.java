package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedHuskEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class SummonHuskSpell extends AbstractSummonSpell {

    public SummonHuskSpell() {
        super("summon_husk");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() {
        return 8.8f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 0.5D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        return new SummonedHuskEntity(GenesisEntities.SUMMONED_HUSK.get(), level);
    }
}