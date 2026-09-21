package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.PatamuWrathEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PatamuWrathSpell extends MiracleSpell {
    public PatamuWrathSpell() {
        super("patamu_wrath");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 26,
                StatType.STRENGTH, 30
        );
    }

    @Override
    public float getMentalCost() {
        return 9.3f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.EARTH;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 24.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 1.8f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.position().add(look.x * 2.0D, caster.getBbHeight() + 2.5D, look.z * 2.0D);

        PatamuWrathEntity wrath = new PatamuWrathEntity(GenesisEntities.PATAMU_WRATH.get(), level);
        wrath.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        wrath.setup(caster, spellSnapshot);
        wrath.setDeltaMovement(0, -1.0D, 0);

        level.addFreshEntity(wrath);
    }
}