package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

        if (level instanceof ServerLevel serverLevel) {
            Vec3 spawnPos = getCatalystHandPosition(caster, 0.7D, 0.35D, -0.25D);

            serverLevel.sendParticles(
                    GenesisParticles.RESISTANCE_BLESSING.get(),
                    spawnPos.x, spawnPos.y, spawnPos.z,
                    1,
                    0.0D, 0.0D, 0.0D,
                    0.0D
            );
        }
    }
}
