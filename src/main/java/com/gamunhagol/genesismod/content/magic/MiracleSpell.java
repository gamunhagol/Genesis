package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Set;

public abstract class MiracleSpell extends AbstractSpell {
    public MiracleSpell(String id) { super(id); }

    @Override
    public boolean canCast(LivingEntity caster) {
        for (Map.Entry<StatType, Integer> entry : getRequiredStats().entrySet()) {
            StatType statType = entry.getKey();
            int requiredLevel = entry.getValue();

            int currentStat = WeaponRequirementHelper.getEntityStat(caster, statType);
            if (currentStat < requiredLevel) return false;
        }

        if (caster instanceof Player player) {
            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }
        return true;
    }

    public MiracleElement getElement() {
        return MiracleElement.NONE;
    }

    protected float getDedicationMultiplier(LivingEntity caster) {
        if (!(caster instanceof Player player)) return 1.0f;

        return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).map(stats -> {
            Set<String> activeGods = stats.getValidDedications();
            if (activeGods.isEmpty()) return 1.0f;

            MiracleElement element = this.getElement();
            if (element == MiracleElement.NONE) return 1.0f;

            float bonus = 0.0f;

            if (activeGods.contains("god_a") && stats.getUnlockedNodeCount("god_a") >= 1 && element == MiracleElement.EARTH) bonus += 0.25f;
            if (activeGods.contains("god_b") && stats.getUnlockedNodeCount("god_b") >= 1 && element == MiracleElement.FIRE) bonus += 0.25f;
            if (activeGods.contains("god_c") && stats.getUnlockedNodeCount("god_c") >= 1 && element == MiracleElement.WATER) bonus += 0.25f;
            if (activeGods.contains("god_d") && stats.getUnlockedNodeCount("god_d") >= 1 && element == MiracleElement.WIND) bonus += 0.25f;
            if (activeGods.contains("god_e") && stats.getUnlockedNodeCount("god_e") >= 1 && element == MiracleElement.LIGHTNING) bonus += 0.25f;
            if (activeGods.contains("god_f") && stats.getUnlockedNodeCount("god_f") >= 1 && element == MiracleElement.FOREST) bonus += 0.25f;

            if (element == MiracleElement.COLD) {
                if (activeGods.contains("god_g") && stats.getUnlockedNodeCount("god_g") >= 1) bonus += 0.15f;
                if (activeGods.contains("god_h") && stats.getUnlockedNodeCount("god_h") >= 1) bonus += 0.15f;
            }

            return 1.0f + bonus;
        }).orElse(1.0f);
    }
}