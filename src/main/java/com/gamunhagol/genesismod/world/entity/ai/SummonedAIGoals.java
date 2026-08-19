package com.gamunhagol.genesismod.world.entity.ai;

import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class SummonedAIGoals {

    public static class FollowOwnerGoal extends Goal {
        private final Mob mob;
        private final ISummonable summonable;
        private LivingEntity owner;
        private final double speedModifier;
        private final float startDistance;
        private final float stopDistance;
        private int timeToRecalcPath;

        public FollowOwnerGoal(Mob mob, double speed, float startDist, float stopDist) {
            this.mob = mob;
            this.summonable = (ISummonable) mob;
            this.speedModifier = speed;
            this.startDistance = startDist;
            this.stopDistance = stopDist;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.summonable.getOwnerUUID() == null) return false;

            Player player = this.mob.level().getPlayerByUUID(this.summonable.getOwnerUUID());
            if (player == null || player.isSpectator()) return false;

            this.owner = player;
            return this.mob.distanceToSqr(this.owner) > (double)(this.startDistance * this.startDistance);
        }

        @Override
        public boolean canContinueToUse() {
            return this.owner != null
                    && !this.owner.isDeadOrDying()
                    && this.mob.distanceToSqr(this.owner) > (double)(this.stopDistance * this.stopDistance);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                this.mob.getNavigation().moveTo(this.owner, this.speedModifier);
            }
        }
    }

    public static class OwnerHurtTargetGoal extends TargetGoal {
        private final ISummonable summonable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(Mob mob) {
            super(mob, false);
            this.summonable = (ISummonable) mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.summonable.getOwnerUUID() == null) return false;

            Player owner = this.mob.level().getPlayerByUUID(this.summonable.getOwnerUUID());
            if (owner == null) return false;

            this.attacker = owner.getLastHurtMob();
            int i = owner.getLastHurtMobTimestamp();

            return i != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.mob.setTarget(this.attacker);
            Player owner = this.mob.level().getPlayerByUUID(this.summonable.getOwnerUUID());
            if (owner != null) {
                this.timestamp = owner.getLastHurtMobTimestamp();
            }
            super.start();
        }
    }

    public static class OwnerHurtByTargetGoal extends TargetGoal {
        private final ISummonable summonable;
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(Mob mob) {
            super(mob, false);
            this.summonable = (ISummonable) mob;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.summonable.getOwnerUUID() == null) return false;

            Player owner = this.mob.level().getPlayerByUUID(this.summonable.getOwnerUUID());
            if (owner == null) return false;

            this.attacker = owner.getLastHurtByMob();
            int i = owner.getLastHurtByMobTimestamp();

            return i != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.mob.setTarget(this.attacker);
            Player owner = this.mob.level().getPlayerByUUID(this.summonable.getOwnerUUID());
            if (owner != null) {
                this.timestamp = owner.getLastHurtByMobTimestamp();
            }
            super.start();
        }
    }
}