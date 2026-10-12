package com.gamunhagol.genesismod.content.magic.miracles.plant;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.RootStrikeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class RootStrikeMiracle extends MiracleSpell {
    public RootStrikeMiracle() {
        super("root_strike");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 22,
                StatType.STRENGTH, 25
        );
    }

    @Override
    public float getMentalCost() {
        return 8.9f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FOREST;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 20.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 1.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.position().add(look.x * 2.5D, caster.getBbHeight() + 3.0D, look.z * 2.5D);

        RootStrikeEntity rootStrike = new RootStrikeEntity(GenesisEntities.ROOT_STRIKE.get(), level);
        rootStrike.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        rootStrike.setup(caster, spellSnapshot);
        rootStrike.setDeltaMovement(0, -1.2D, 0);

        level.addFreshEntity(rootStrike);
    }
}