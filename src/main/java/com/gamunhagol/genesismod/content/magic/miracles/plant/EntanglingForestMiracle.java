package com.gamunhagol.genesismod.content.magic.miracles.plant;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.etc.GrabHolderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class EntanglingForestMiracle extends MiracleSpell {

    public EntanglingForestMiracle() {
        super("entangling_forest");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 24);
    }

    @Override
    public float getMentalCost() {
        return 10.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FOREST;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 2.1f;

        float finalPhysical = (basePhysical + (catalyst.holy() * 0.05f)) * getDedicationMultiplier(caster);

        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int searchTicks = 60;
        int holdTicks = 60;
        int requiredEscapes = 7;

        GrabHolderEntity holder = new GrabHolderEntity(
                level,
                caster,
                searchTicks,
                holdTicks,
                requiredEscapes
        );

        holder.setOnHoldTickListener(victim -> {
            if (holder.tickCount % 4 == 0 && victim.isAlive()) {
                victim.invulnerableTime = 0;

                float healthBefore = victim.getHealth();

                GenesisDamageCalculator.applySnapshotDamage(victim, caster, spellSnapshot);

                float actualDamageTaken = Math.max(0.0F, healthBefore - victim.getHealth());
                if (actualDamageTaken > 0.0F && caster.isAlive()) {
                    caster.heal(actualDamageTaken);
                }
            }
        });

        level.addFreshEntity(holder);
    }
}
