package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SpellElderGuardianEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class HeavyGuardianBeamSpell extends MagicSpell {
    public HeavyGuardianBeamSpell() { super("heavy_guardian_beam"); }

    @Override
    public int getCastTime() { return 50; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 19);
    }

    @Override
    public float getMentalCost() { return 6.0f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 16.0f;
        float magicEfficiency = 0.5f;
        float finalMagic = baseMagic + (catalyst.magic() * magicEfficiency);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        LivingEntity target = ShulkerBulletSpell.getTargetInSight(caster, 25.0D); // 사거리 증가

        if (target != null && !level.isClientSide) {
            SpellElderGuardianEntity beamGuardian = new SpellElderGuardianEntity(GenesisEntities.SPELL_ELDER_GUARDIAN.get(), level);

            beamGuardian.setBeamData(caster, target, spellSnapshot, 60);

            level.addFreshEntity(beamGuardian);
        }
    }
}