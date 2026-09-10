package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities; // 엔티티 레지스트리 경로에 맞게 수정
import com.gamunhagol.genesismod.world.entity.mob.SpellGuardianEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class GuardianBeamSpell extends MagicSpell {
    public GuardianBeamSpell() { super("guardian_beam"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 13);
    }

    @Override
    public float getMentalCost() { return 3.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 6.0f;
        float magicEfficiency = 0.4f;
        float finalMagic = baseMagic + (catalyst.magic() * magicEfficiency);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        LivingEntity target = ShulkerBulletSpell.getTargetInSight(caster, 15.0D);

        if (target != null && !level.isClientSide) {
            SpellGuardianEntity beamGuardian = new SpellGuardianEntity(GenesisEntities.SPELL_GUARDIAN.get(), level);

            beamGuardian.setBeamData(caster, target, spellSnapshot, 40);

            level.addFreshEntity(beamGuardian);
        }
    }
}