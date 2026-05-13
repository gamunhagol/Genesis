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
        //  신앙(FAITH) 요구치 체크
        // 이제 Player뿐만 아니라 몹(LivingEntity)도 신앙 수치를 확인합니다.
        // 몹은 기본값이 10이므로, 요구치가 10 이하인 기초 기적은 바로 사용할 수 있습니다.
        int currentFaith = WeaponRequirementHelper.getEntityStat(caster, StatType.FAITH);
        if (currentFaith < getRequiredStatLevel()) return false;

        //  정신력(Mental/MP) 소모 체크
        if (caster instanceof Player player) {
            // 플레이어라면 기존처럼 Capability 시스템을 통해 마나 잔량을 확인합니다.
            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }

        return true;
    }
}