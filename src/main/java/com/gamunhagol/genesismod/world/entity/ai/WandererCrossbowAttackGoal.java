package com.gamunhagol.genesismod.world.entity.ai;

import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.EnumSet;

public class WandererCrossbowAttackGoal<T extends PathfinderMob & CrossbowAttackMob> extends Goal {
    public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
    private final T mob;
    private CrossbowState state = CrossbowState.UNCHARGED;
    private final double speedModifier;
    private final float attackRadiusSqr;
    private int seeTime;
    private int updatePathDelay;

    public WandererCrossbowAttackGoal(T pMob, double pSpeedModifier, float pAttackRadius) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.hasCrossbow() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    private boolean hasCrossbow() {
        return this.mob.isHolding(Items.CROSSBOW);
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.mob.setTarget(null);
        this.seeTime = 0;
        if (this.mob.isUsingItem()) {
            this.mob.stopUsingItem();
            this.mob.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.mob.getUseItem(), false);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        boolean canSee = this.mob.getSensing().hasLineOfSight(target);
        if (canSee) this.seeTime++;
        else this.seeTime = 0;

        double distSqr = this.mob.distanceToSqr(target);
        if (distSqr <= (double)this.attackRadiusSqr && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            if (--this.updatePathDelay <= 0) {
                this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.getRandom());
                this.mob.getNavigation().moveTo(target, this.speedModifier);
            }
        }

        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // 석궁 장전 로직 제어
        if (this.state == CrossbowState.UNCHARGED) {
            if (canSee) {
                this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.CROSSBOW));
                this.state = CrossbowState.CHARGING;
                this.mob.setChargingCrossbow(true);
            }
        } else if (this.state == CrossbowState.CHARGING) {
            if (!this.mob.isUsingItem()) {
                this.state = CrossbowState.UNCHARGED;
            }
            int i = this.mob.getTicksUsingItem();
            ItemStack itemstack = this.mob.getUseItem();
            if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                this.mob.releaseUsingItem();
                this.state = CrossbowState.READY;
                this.mob.setChargingCrossbow(false);
            }
        } else if (this.state == CrossbowState.READY) {
            if (canSee) {
                this.mob.performRangedAttack(target, 1.0F);
                this.state = CrossbowState.UNCHARGED;
            }
        }
    }

    static enum CrossbowState { UNCHARGED, CHARGING, READY; }
}