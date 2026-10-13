package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.etc.GrabHolderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class AtonementForSinMiracle extends MiracleSpell {

    public AtonementForSinMiracle() {
        super("atonement_for_sin");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 21,
                StatType.STRENGTH, 30
        );
    }

    @Override
    public float getMentalCost() {
        return 14.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float dedicationMul = getDedicationMultiplier(caster);

        float baseFrost = 40.0f;

        float finalFrost = (baseFrost + (catalyst.frost() * 1.5f)) * dedicationMul;

        return new DamageSnapshot(0, 0, 0, 0, finalFrost, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int searchTicks = 80;
        int holdTicks = 40;
        int requiredEscapes = 8;

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