package com.gamunhagol.genesismod.world.entity.ai;

import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions; // 이거 임포트

import java.util.EnumSet;
import java.util.List;

public class FollowCollectorGoal extends Goal {
    private final CollectorGuard guard;
    private Collector owner;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private int timeToRecalcPath;

    public FollowCollectorGoal(CollectorGuard guard, double speed, float startDist, float stopDist) {
        this.guard = guard;
        this.speedModifier = speed;
        this.startDistance = startDist;
        this.stopDistance = stopDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // [최적화] 단순히 리스트를 가져오는 게 아니라, '가장 가까운' 녀석을 찾습니다.
        List<Collector> list = this.guard.level().getEntitiesOfClass(
                Collector.class,
                this.guard.getBoundingBox().inflate(16.0D),
                collector -> !collector.isDeadOrDying() // 살아있는 징수원만
        );

        if (list.isEmpty()) return false;

        // 거리순으로 정렬하여 가장 가까운 징수원을 주인으로 삼음
        list.sort((e1, e2) -> Double.compare(this.guard.distanceToSqr(e1), this.guard.distanceToSqr(e2)));

        this.owner = list.get(0);
        return this.guard.distanceToSqr(this.owner) > (double)(this.startDistance * this.startDistance);
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner != null && !this.owner.isDeadOrDying() && this.guard.distanceToSqr(this.owner) > (double)(this.stopDistance * this.stopDistance);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.guard.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            this.guard.getNavigation().moveTo(this.owner, this.speedModifier);
        }
    }
}
