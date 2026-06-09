package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.Map;

public abstract class MagicSpell extends AbstractSpell {
    public MagicSpell(String id) { super(id); }

    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, getRequiredStatLevel());
    }

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
}