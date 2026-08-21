package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedBlazeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class SummonBlazeSpell extends AbstractSummonSpell {

    public SummonBlazeSpell() {
        super("summon_blaze");
    }

    @Override
    public int getCastTime() {
        return 40;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.ARCANE, 16,
                StatType.INTELLIGENCE, 14
        );
    }

    @Override
    public float getMentalCost() {
        return 7.0f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!super.canCast(caster)) {
            return false;
        }

        if (caster instanceof Player player) {
            List<SummonedBlazeEntity> existingBlazes = player.level().getEntitiesOfClass(
                    SummonedBlazeEntity.class,
                    player.getBoundingBox().inflate(128.0D),
                    blaze -> player.getUUID().equals(blaze.getOwnerUUID())
            );
            return existingBlazes.size() < 5;
        }

        return true;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 0.7D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        return new SummonedBlazeEntity(GenesisEntities.SUMMONED_BLAZE.get(), level);
    }
}
