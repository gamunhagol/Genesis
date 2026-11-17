package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

import java.util.List;

public class RockEntity extends StoneProjectile {
    private static final EntityDataAccessor<Integer> DATA_HOLD_TICKS = SynchedEntityData.defineId(RockEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_HELD = SynchedEntityData.defineId(RockEntity.class, EntityDataSerializers.BOOLEAN);

    private Vec3 pendingMotion = Vec3.ZERO;
    private float lockedYaw = 0.0F;
    private float lockedPitch = 0.0F;

    public RockEntity(EntityType<? extends RockEntity> type, Level level) {
        super(type, level);
        this.gravity = 0.045D;
    }

    public RockEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.ROCK.get(), level, owner, snapshot);
        this.gravity = 0.045D;
    }

    public RockEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 launchMotion, int holdTicks) {
        super(GenesisEntities.ROCK.get(), level, owner, snapshot);
        this.gravity = 0.045D;
        this.pendingMotion = launchMotion;
        this.setHoldTicks(holdTicks);

        this.setDeltaMovement(launchMotion);
        this.updateRotation();
        this.lockedYaw = this.getYRot();
        this.lockedPitch = this.getXRot();

        if (holdTicks > 0) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.hasImpulse = true;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HOLD_TICKS, 0);
        this.entityData.define(DATA_HELD, false);
    }

    public int getHoldTicks() {
        return this.entityData.get(DATA_HOLD_TICKS);
    }

    public void setHoldTicks(int ticks) {
        this.entityData.set(DATA_HOLD_TICKS, ticks);
    }

    public boolean isHeld() {
        return this.entityData.get(DATA_HELD);
    }

    public void setHeld(boolean held) {
        this.entityData.set(DATA_HELD, held);
        if (held) {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    public void releaseRock(Vec3 motion) {
        this.setHeld(false);
        this.setHoldTicks(0);
        this.setDeltaMovement(motion);
        this.hasImpulse = true;
        this.updateRotation();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.getHoldTicks() > 0 || this.isHeld()) return false;
        return super.canHitEntity(entity);
    }

    @Override
    public void tick() {
        if (this.isHeld()) {
            this.setDeltaMovement(Vec3.ZERO);
            if (this.getOwner() instanceof LivingEntity owner) {
                if (!owner.isAlive()) {
                    if (!this.level().isClientSide) this.discard();
                    return;
                }
                Vec3 holdPos = calculateHeldPosition(owner);
                this.setPos(holdPos.x, holdPos.y, holdPos.z);
                this.setYRot(-owner.getYRot());
                this.setXRot(-owner.getXRot());
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }
            return;
        }

        int hold = this.getHoldTicks();
        if (hold > 0) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setYRot(this.lockedYaw);
            this.setXRot(this.lockedPitch);
            this.yRotO = this.lockedYaw;
            this.xRotO = this.lockedPitch;

            if (!this.level().isClientSide) {
                int nextHold = hold - 1;
                this.setHoldTicks(nextHold);
                if (nextHold <= 0) {
                    this.setDeltaMovement(this.pendingMotion);
                    this.hasImpulse = true;
                    this.updateRotation();
                }
            }
            return;
        }

        super.tick();
    }

    private Vec3 calculateHeldPosition(LivingEntity caster) {
        Vec3 forward = Vec3.directionFromRotation(0.0F, caster.getYRot());
        Vec3 right = Vec3.directionFromRotation(0.0F, caster.getYRot() + 90.0F);

        boolean isRightHand = true;
        if (caster instanceof Player player) {
            boolean isMainHand = caster.getUsedItemHand() == InteractionHand.MAIN_HAND;
            isRightHand = (player.getMainArm() == HumanoidArm.RIGHT) == isMainHand;
        }
        double sideMult = isRightHand ? 1.0D : -1.0D;

        return new Vec3(
                caster.getX() + forward.x * 0.4D + right.x * 0.7D * sideMult,
                caster.getY() + 1.8D,
                caster.getZ() + forward.z * 0.4D + right.z * 0.7D * sideMult
        );
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.getHoldTicks() > 0 || this.isHeld()) return;

        if (!this.level().isClientSide) {
            LivingEntity owner = (LivingEntity) this.getOwner();

            if (owner != null) {
                Vec3 hitPos = result.getLocation();
                Vec3 slamPos = new Vec3(hitPos.x, hitPos.y - 0.2D, hitPos.z);

                LevelUtil.circleSlamFracture(owner, this.level(), slamPos, 3.5D, false, false, false);

                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                    DamageSnapshot snapshot = cap.getSnapshot();

                    float shockwaveMultiplier = 0.33F;
                    float basePhysical = snapshot.physical() > 0 ? snapshot.physical() : 10.0F;
                    float finalDamage = basePhysical * shockwaveMultiplier;

                    EpicFightDamageSource shockwaveSource = EpicFightDamageSources.shockwave(owner);

                    List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                            new AABB(slamPos.add(-3.5, -1, -3.5), slamPos.add(3.5, 2, 3.5)));

                    for (LivingEntity target : targets) {
                        if (target != owner) {
                            target.hurt(shockwaveSource, finalDamage);
                        }
                    }
                });
            }

            spawnRockParticles(18);
        }

        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.getHoldTicks() > 0 || this.isHeld()) return;

        if (!this.level().isClientSide) {
            spawnRockParticles(18);
        }
        super.onHitEntity(result);
    }

    private void spawnRockParticles(int count) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState());
            serverLevel.sendParticles(particle, this.getX(), this.getY(), this.getZ(), count, 0.12D, 0.12D, 0.12D, 0.04D);
        }
    }
}
