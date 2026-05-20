package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class AbstractSpell {
    private final String id;

    public AbstractSpell(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public abstract int getCastTime();

    // 기존의 단일 주력 스탯 요구치 (하위 호환성 및 단순함을 위해 유지)
    public abstract int getRequiredStatLevel();

    // 기본적으로는 빈 Map을 반환하지만, MagicSpell과 MiracleSpell에서 오버라이드하여 기본값을 채워줍니다.
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of();
    }

    public abstract float getMentalCost();
    public abstract boolean canCast(LivingEntity caster);
    public abstract int getMemoryCost();
    public void onCastingTick(LivingEntity caster, int remainingTicks) {}

    // ★ CatalystItem에서 호출하는 시전 진입점
    public void executeCast(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot) {
        if (!level.isClientSide) {
            // 촉매의 스탯을 바탕으로 이 주문만의 최종 스탯 스냅샷을 계산
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(catalystSnapshot);

            // 실제 주문 효과 발동
            onExecute(level, caster, spellSnapshot);

            consumeMental(caster);
        }
    }

    // ★ 자식 클래스(각 마법)가 구현할 데미지/효과량 계산식
    protected abstract DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalystSnapshot);

    // ★ 자식 클래스가 구현할 실제 실행 로직 (투사체 소환, 즉발 효과 등)
    protected abstract void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot);

    public void consumeMental(LivingEntity caster) {
        if (caster instanceof net.minecraft.world.entity.player.Player player) {
            player.getCapability(com.gamunhagol.genesismod.stats.StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(stats.getMental() - getMentalCost());
            });
        }
    }
}