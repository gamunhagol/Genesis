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
        //  지력(INTELLIGENCE) 요구치 체크
        int currentInt = WeaponRequirementHelper.getEntityStat(caster, StatType.INTELLIGENCE);
        if (currentInt < getRequiredStatLevel()) return false;

        // 정신력(Mental/MP) 소모 체크
        if (caster instanceof Player player) {
            // 플레이어는 기존처럼 Capability에서 마나를 확인
            return player.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(stats -> stats.getMental() >= getMentalCost())
                    .orElse(false);
        }


        return true;
    }
}