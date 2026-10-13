package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ResistanceBlessingMiracle extends MiracleSpell {

    public ResistanceBlessingMiracle() {
        super("resistance_blessing");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 18);
    }

    @Override
    public float getMentalCost() {
        return 7.6f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        int faith = WeaponRequirementHelper.getEntityStat(caster, StatType.FAITH);
        float faithBonusMultiplier = 1.0f + (faith * 0.01f);
        float dedicationMultiplier = getDedicationMultiplier(caster);
        float totalMultiplier = faithBonusMultiplier * dedicationMultiplier;

        int duration = Math.round(1200 * totalMultiplier);

        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 1));
    }
}
