package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class MiracleSpell extends AbstractSpell {
    public MiracleSpell(String id) {
        super(id);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (caster instanceof Player player) {
            // 1. 신앙 요구치 확인
            int currentFaith = WeaponRequirementHelper.getPlayerStat(player, StatType.FAITH);
            if (currentFaith < getRequiredStatLevel()) return false;

            // 2. 멘탈(마나) 보유량 확인
            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }
        return true;
    }
}