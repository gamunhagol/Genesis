package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.mixin.GuardianInvoker;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class SpellGuardianEntity extends Guardian {
    private LivingEntity caster;
    private LivingEntity forcedTarget;
    private DamageSnapshot snapshot;
    private int customAttackDuration = 40;

    public SpellGuardianEntity(EntityType<? extends Guardian> type, Level level) {
        super(type, level);
        this.setInvisible(true);
        this.setInvulnerable(true);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SpellGuardianBeamGoal(this));
    }

    public void setBeamData(LivingEntity caster, LivingEntity target, DamageSnapshot snapshot, int duration) {
        this.caster = caster;
        this.forcedTarget = target;
        this.snapshot = snapshot;
        this.customAttackDuration = duration;

        this.setPos(caster.getX(), caster.getEyeY(), caster.getZ());
        this.setTarget(target);

        this.setInvisible(true);
        this.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.INVISIBILITY,
                duration + 20, 0, false, false, false
        ));
    }

    @Override
    public int getAttackDuration() {
        return this.customAttackDuration;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.caster != null && this.caster.isAlive()) {
            this.setPos(this.caster.getX(), this.caster.getEyeY(), this.caster.getZ());
        }
    }

    @Override protected SoundEvent getAmbientSound() { return null; }
    @Override protected SoundEvent getHurtSound(DamageSource pDamageSource) { return null; }
    @Override protected SoundEvent getDeathSound() { return null; }
    @Override protected SoundEvent getFlopSound() { return null; }

    class SpellGuardianBeamGoal extends Goal {
        private final SpellGuardianEntity guardian;
        private int attackTime;

        public SpellGuardianBeamGoal(SpellGuardianEntity guardian) {
            this.guardian = guardian;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.guardian.forcedTarget != null && this.guardian.forcedTarget.isAlive();
        }

        @Override
        public void start() {
            this.attackTime = -1;
            ((GuardianInvoker) this.guardian).invokeSetActiveAttackTarget(this.guardian.forcedTarget.getId());
        }

        @Override
        public void stop() {
            ((GuardianInvoker) this.guardian).invokeSetActiveAttackTarget(0);
            this.guardian.discard();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.guardian.forcedTarget;

            if (target == null || !target.isAlive() || !this.guardian.hasLineOfSight(target)) {
                this.guardian.discard();
                return;
            }

            this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
            this.attackTime++;

            if (this.attackTime == 0) {
                ((GuardianInvoker) this.guardian).invokeSetActiveAttackTarget(target.getId());
                this.guardian.level().broadcastEntityEvent(this.guardian, (byte)21);
            }
            else if (this.attackTime >= this.guardian.getAttackDuration()) {
                if (this.guardian.caster != null && this.guardian.snapshot != null) {

                    Snowball dummy = new Snowball(this.guardian.level(), this.guardian.getX(), this.guardian.getY(), this.guardian.getZ());
                    dummy.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.guardian.snapshot));

                    target.hurt(this.guardian.caster.damageSources().indirectMagic(dummy, this.guardian.caster), this.guardian.snapshot.magic());
                    dummy.discard();
                }
                this.guardian.discard();
            }
        }
    }
}