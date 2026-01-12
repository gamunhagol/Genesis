package com.gamunhagol.genesismod.world.entity.ai;

import com.gamunhagol.genesismod.world.entity.mob.Collector;
import com.gamunhagol.genesismod.world.entity.mob.CollectorGuard;
import net.minecraft.world.entity.ai.goal.Goal;

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
        // 주변 16블록 내 징수원 찾기
        List<Collector> list = this.guard.level().getEntitiesOfClass(Collector.class, this.guard.getBoundingBox().inflate(16.0D));
        if (list.isEmpty()) return false;

        this.owner = list.get(0); // 가장 가까운 징수원 선정
        return this.guard.distanceToSqr(this.owner) > (double)(this.startDistance * this.startDistance);
    }

    @Override
    public boolean canContinueToUse() {
        return this.owner != null && this.guard.distanceToSqr(this.owner) > (double)(this.stopDistance * this.stopDistance);
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
