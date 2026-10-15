package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class HealMiracle extends MiracleSpell {
    public HealMiracle() { super("little_heal"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override
    public float getMentalCost() { return 3.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float healAmount = 4.0f + catalyst.holy();
        float efficiency = 0.3f;

        float baseHeal = healAmount + (catalyst.holy() * efficiency);
        float multiplier = getDedicationMultiplier(caster);
        float finalHeal = baseHeal * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        float amount = spellSnapshot.holy();
        if (caster.isInvertedHealAndHarm()) {
            caster.hurt(GenesisDamageTypes.getSource(level, GenesisDamageTypes.HOLY, caster), amount);
        } else {
            caster.heal(amount);
        }
    }
}
