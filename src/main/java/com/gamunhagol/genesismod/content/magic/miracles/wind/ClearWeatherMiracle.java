package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ClearWeatherMiracle extends MiracleSpell {
    public ClearWeatherMiracle() {
        super("clear_weather");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 18);
    }

    @Override
    public float getMentalCost() {
        return 7.8f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            serverLevel.setWeatherParameters(6000, 0, false, false);
        }
    }
}