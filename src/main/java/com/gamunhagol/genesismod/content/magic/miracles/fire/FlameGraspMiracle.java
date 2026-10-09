package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.etc.GrabHolderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class FlameGraspMiracle extends MiracleSpell {

    public FlameGraspMiracle() {
        super("flame_grasp");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 12,
                StatType.STRENGTH, 15
        );
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
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseFire = 18.0f;
        float finalFire = (baseFire + (catalyst.fire() * 0.8f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int searchTicks = 60;
        int holdTicks = 40;
        int requiredEscapes = 6;

        GrabHolderEntity holder = new GrabHolderEntity(
                level,
                caster,
                searchTicks,
                holdTicks,
                requiredEscapes
        );

        holder.setOnGrabbedListener(victim -> {
            GenesisDamageCalculator.applySnapshotDamage(victim, caster, spellSnapshot);
        });

        level.addFreshEntity(holder);
    }
}