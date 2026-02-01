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
        List<Collector> list = this.guard.level().getEntitiesOfClass(Collector.class, this.guard.getBoundingBox().inflate(16.0D));
        if (list.isEmpty()) return false;

        for (Collector collector : list) {
            int i = collector.getLastHurtByMobTimestamp();
            LivingEntity attacker = collector.getLastHurtByMob();

            // [수정] 공격자가 있고 + 타임스탬프가 다르고 + "최근 200틱(10초) 이내에 맞았을 때만" 반응
            if (i != this.lastTimestamp && attacker != null) {
                if (this.guard.tickCount - i < 200) { // <-- 시간 체크 추가 권장 (선택사항이지만 넣는 게 자연스러움)
                    this.attacker = attacker;
                    this.lastTimestamp = i;
                    return true;
                }
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
