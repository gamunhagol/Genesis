package com.gamunhagol.genesismod.world.entity.ai;

import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;
import java.util.List;

public class DefendCollectorGoal extends TargetGoal {
    private final CollectorGuard guard;
    private LivingEntity attacker;
    private int lastTimestamp;

    public DefendCollectorGoal(CollectorGuard pGuard) {
        super(pGuard, false);
        this.guard = pGuard;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // 주변 16블록 내의 징수원을 찾습니다.
        List<Collector> list = this.guard.level().getEntitiesOfClass(Collector.class, this.guard.getBoundingBox().inflate(16.0D));
        if (list.isEmpty()) return false;

        for (Collector collector : list) {
            // 정확한 메서드 명칭은 getLastHurtByMobTimestamp입니다.
            int currentTimestamp = collector.getLastHurtByMobTimestamp();
            LivingEntity lastAttacker = collector.getLastHurtByMob();

            // 마지막 공격 시간이 갱신되었고, 공격자가 살아있다면 타겟으로 잡습니다.
            if (currentTimestamp != this.lastTimestamp && lastAttacker != null) {
                this.attacker = lastAttacker;
                this.lastTimestamp = currentTimestamp;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        // 타겟이 죽거나 멀어지면 타겟팅 해제
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive()) {
            return this.mob.distanceToSqr(target) < 256.0D; // 16블록 이내면 계속 추격
        }
        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        super.start();
    }
}
