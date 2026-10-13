package com.gamunhagol.genesismod.content.magic.miracles.cold;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.etc.BlizzardEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class BlizzardMiracle extends MiracleSpell {

    public BlizzardMiracle() {
        super("blizzard");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 24);
    }

    @Override
    public float getMentalCost() {
        return 10.3f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseFrost = 5.0f;
        float finalFrost = (baseFrost + (catalyst.holy() * 0.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, 0, 0, finalFrost, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        HitResult hitResult = caster.pick(16.0D, 1.0F, false);
        Vec3 spawnPos;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            spawnPos = hitResult.getLocation();
        } else {
            Vec3 look = caster.getLookAngle();
            spawnPos = caster.position().add(look.x * 8.0D, 0, look.z * 8.0D);
        }

        BlizzardEntity blizzard = new BlizzardEntity(level, spawnPos.x, spawnPos.y, spawnPos.z);
        blizzard.setup(caster, spellSnapshot, 600, 6.0F);
        level.addFreshEntity(blizzard);
    }
}
