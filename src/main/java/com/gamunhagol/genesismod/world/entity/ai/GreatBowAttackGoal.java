package com.gamunhagol.genesismod.world.entity.ai;

import com.gamunhagol.genesismod.world.item.weapon.GreatBowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GreatBowAttackGoal<T extends PathfinderMob & RangedAttackMob> extends Goal {
    private final T mob;
    private final double speedModifier;
    private final float attackRadiusSqr;

    private LivingEntity target;
    private int attackTime = -1;
    private int targetChargeTime = 38;

    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private int aimDelay = 0;

    private int consecutiveHitsTaken = 0;
    private int lastHurtTime = 0;
    private boolean isFleeing = false;
    private int fleeTicks = 0;

    public GreatBowAttackGoal(T mob, double speedModifier, float attackRadius) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            return this.mob.isHolding(is -> is.getItem() instanceof GreatBowItem);
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone() || this.isFleeing)
                && this.mob.isHolding(is -> is.getItem() instanceof GreatBowItem);
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        this.seeTime = 0;
        this.consecutiveHitsTaken = 0;
        this.isFleeing = false;
        this.fleeTicks = 0;
        this.lastHurtTime = this.mob.hurtTime;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.target = null;
        this.attackTime = -1;
        this.seeTime = 0;
        this.consecutiveHitsTaken = 0;
        this.isFleeing = false;
        this.fleeTicks = 0;
        if (this.mob.isUsingItem()) {
            this.mob.stopUsingItem();
        }
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity currentTarget = this.mob.getTarget();
        if (currentTarget == null && !this.isFleeing) return;

        if (this.mob.hurtTime > 0 && this.lastHurtTime == 0) {
            this.consecutiveHitsTaken++;

            if (this.mob.isUsingItem() && this.targetChargeTime >= 100) {
                this.targetChargeTime = 38;
                this.strafingBackwards = true;
            }
        }
        this.lastHurtTime = this.mob.hurtTime;

        if (this.consecutiveHitsTaken >= 3 && !this.isFleeing) {
            this.isFleeing = true;
            this.fleeTicks = 60;
            this.consecutiveHitsTaken = 0;
            if (this.mob.isUsingItem()) {
                this.mob.stopUsingItem();
            }
        }

        if (this.isFleeing) {
            this.fleeTicks--;

            if (currentTarget != null) {
                double distSqr = this.mob.distanceToSqr(currentTarget);

                if (distSqr > 144.0D && this.fleeTicks <= 0) {
                    this.isFleeing = false;
                    this.mob.getNavigation().stop();
                    return;
                }

                if (this.mob.getNavigation().isDone()) {
                    Vec3 fleePos = DefaultRandomPos.getPosAway(this.mob, 16, 7, currentTarget.position());
                    if (fleePos == null) {
                        fleePos = LandRandomPos.getPosAway(this.mob, 16, 7, currentTarget.position());
                    }

                    if (fleePos != null) {
                        this.mob.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, this.speedModifier * 1.35D);
                    } else {
                        if (this.fleeTicks <= 0) this.isFleeing = false;
                    }
                }
            } else {
                this.isFleeing = false;
            }
            return;
        }

        double distSqr = this.mob.distanceToSqr(currentTarget);
        boolean canSee = this.mob.getSensing().hasLineOfSight(currentTarget);

        if (canSee != this.seeTime > 0) {
            this.seeTime = 0;
        }
        if (canSee) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (distSqr <= (double) this.attackRadiusSqr && this.seeTime > 0) {
            this.mob.getNavigation().stop();
            ++this.strafingTime;
        } else {
            this.mob.getNavigation().moveTo(currentTarget, this.speedModifier);
            this.strafingTime = -1;
        }

        if (this.strafingTime >= 20) {
            if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if ((double) this.mob.getRandom().nextFloat() < 0.3D) {
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (distSqr > (double) (this.attackRadiusSqr * 0.75F)) {
                this.strafingBackwards = false;
            } else if (distSqr < (double) (this.attackRadiusSqr * 0.25F)) {
                this.strafingBackwards = true;
            }
            this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.mob.lookAt(currentTarget, 30.0F, 30.0F);
        } else {
            this.mob.getLookControl().setLookAt(currentTarget, 30.0F, 30.0F);
        }

        if (this.mob.isUsingItem()) {
            int useTicks = this.mob.getTicksUsingItem();

            if (useTicks >= this.targetChargeTime + this.aimDelay) {
                if (this.targetChargeTime >= 100) {
                    if (distSqr <= 25.0D) {
                        this.mob.setXRot(90.0F);
                    } else {
                        double dX = currentTarget.getX() - this.mob.getX();
                        double dY = currentTarget.getY(0.5D) - this.mob.getEyeY();
                        double dZ = currentTarget.getZ() - this.mob.getZ();

                        dX += this.mob.getRandom().nextGaussian() * 0.8D;
                        dY += this.mob.getRandom().nextGaussian() * 0.4D;
                        dZ += this.mob.getRandom().nextGaussian() * 0.8D;

                        double horizontalDist = Math.sqrt(dX * dX + dZ * dZ);
                        float xRot = (float) (-(Math.atan2(dY, horizontalDist) * (double) (180F / (float) Math.PI)));
                        float yRot = (float) (Math.atan2(dZ, dX) * (double) (180F / (float) Math.PI)) - 90.0F;

                        this.mob.setXRot(xRot);
                        this.mob.setYRot(yRot);
                        this.mob.yBodyRot = yRot;
                        this.mob.yHeadRot = yRot;
                    }
                } else {
                    this.mob.getLookControl().setLookAt(currentTarget, 30.0F, 30.0F);
                }

                this.mob.releaseUsingItem();
                this.attackTime = 20;
                this.consecutiveHitsTaken = 0;
            }
        } else {
            if (--this.attackTime <= 0 && canSee) {
                double dist = Math.sqrt(distSqr);

                if (dist <= 5.0D) {
                    this.targetChargeTime = this.mob.getRandom().nextFloat() < 0.75F ? 102 : 38;
                } else if (dist >= 20.0D) {
                    this.targetChargeTime = this.mob.getRandom().nextFloat() < 0.39F ? 102 : 38;
                } else {
                    this.targetChargeTime = 38;
                }

                this.aimDelay = this.mob.getRandom().nextInt(38);
                this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, this.mob.getMainHandItem().getItem()));
            }
        }
    }
}