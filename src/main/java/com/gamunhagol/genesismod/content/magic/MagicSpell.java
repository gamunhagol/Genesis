package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class MagicSpell extends AbstractSpell {
    public MagicSpell(String id) { super(id); }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (caster instanceof Player player) {
            int currentInt = WeaponRequirementHelper.getPlayerStat(player, StatType.INTELLIGENCE);
            if (currentInt < getRequiredStatLevel()) return false;

            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }
        return true;
    }
}