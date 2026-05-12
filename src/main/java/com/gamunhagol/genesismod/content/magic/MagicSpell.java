package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class MagicSpell extends AbstractSpell {
    public MagicSpell(String id) {
        super(id);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (caster instanceof Player player) {
            // 1. 지력 요구치 확인
            int currentInt = WeaponRequirementHelper.getPlayerStat(player, StatType.INTELLIGENCE);
            if (currentInt < getRequiredStatLevel()) return false;

            // 2. 멘탈(마나) 보유량 확인
            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }
        return true; // 몹이 쓰는 경우는 일단 true 처리 (나중에 AI 연동 시 수정)
    }
}
