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

public class LargeRockEntity extends StoneProjectile {
    private static final int MAX_PEBBLE_COUNT = 8;
    private static final int MAX_ROCK_COUNT = 4;
    private static final EntityDataAccessor<Boolean> DATA_HELD = SynchedEntityData.defineId(LargeRockEntity.class, EntityDataSerializers.BOOLEAN);

    protected double slamRadius = 5.5D;
    protected float shockwaveMultiplier = 0.45F;

    public LargeRockEntity(EntityType<? extends LargeRockEntity> type, Level level) {
        super(type, level);
        this.gravity = 0.06D;
    }

    public LargeRockEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.LARGE_ROCK.get(), level, owner, snapshot);
        this.gravity = 0.06D;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HELD, false);
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
        this.setDeltaMovement(motion);
        this.hasImpulse = true;
        this.updateRotation();
    }

    public void setSlamRadius(double radius) {
        this.slamRadius = radius;
    }

    public void setShockwaveMultiplier(float multiplier) {
        this.shockwaveMultiplier = multiplier;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.isHeld()) return false;
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
                caster.getX() + forward.x * 0.3D + right.x * 0.9D * sideMult,
                caster.getY() + 2.2D,
                caster.getZ() + forward.z * 0.3D + right.z * 0.9D * sideMult
        );
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.isHeld()) return;

        if (!this.level().isClientSide) {
            LivingEntity owner = (LivingEntity) this.getOwner();

            if (owner != null) {
                Vec3 hitPos = result.getLocation();
                Vec3 slamPos = new Vec3(hitPos.x, hitPos.y - 0.2D, hitPos.z);

                LevelUtil.circleSlamFracture(owner, this.level(), slamPos, this.slamRadius, false, false, false);

                this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                    DamageSnapshot snapshot = cap.getSnapshot();

                    float basePhysical = snapshot.physical() > 0 ? snapshot.physical() : 24.0F;
                    float finalDamage = basePhysical * this.shockwaveMultiplier;

                    EpicFightDamageSource shockwaveSource = EpicFightDamageSources.shockwave(owner);

                    List<LivingEntity> targets = this.level().getEntitiesOfClass(
                            LivingEntity.class,
                            new AABB(
                                    slamPos.add(-this.slamRadius, -1.5D, -this.slamRadius),
                                    slamPos.add(this.slamRadius, 2.5D, this.slamRadius)
                            )
                    );

                    for (LivingEntity target : targets) {
                        if (target != owner) {
                            target.hurt(shockwaveSource, finalDamage);
                        }
                    }
                });
            }

            shatter(result.getLocation());
        }

        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.isHeld()) return;

        if (!this.level().isClientSide) {
            shatter(result.getLocation());
        }
        super.onHitEntity(result);
    }

    private void shatter(Vec3 hitLocation) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState());
            serverLevel.sendParticles(particle, hitLocation.x, hitLocation.y, hitLocation.z, 35, 0.35D, 0.35D, 0.35D, 0.05D);
        }

        LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
        DamageSnapshot baseSnapshot = this.getCapability(ProjectileStatsProvider.CAPABILITY)
                .map(cap -> cap.getSnapshot())
                .orElse(this.damageSnapshot);

        float basePhysical = baseSnapshot != null ? baseSnapshot.physical() : 0f;
        DamageSnapshot pebbleSnapshot = new DamageSnapshot(basePhysical * 0.25f, 0, 0, 0, 0, 0, 0);
        DamageSnapshot rockSnapshot = new DamageSnapshot(basePhysical * 0.5f, 0, 0, 0, 0, 0, 0);

        int pebbleCount = this.random.nextInt(MAX_PEBBLE_COUNT + 1);
        for (int i = 0; i < pebbleCount; i++) {
            PebbleEntity pebble = new PebbleEntity(this.level(), owner, pebbleSnapshot);
            launchFragment(pebble, hitLocation, 0.35D, 0.25D);
            pebble.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(pebbleSnapshot));
            this.level().addFreshEntity(pebble);
        }

        int rockCount = this.random.nextInt(MAX_ROCK_COUNT + 1);
        for (int i = 0; i < rockCount; i++) {
            RockEntity rock = new RockEntity(this.level(), owner, rockSnapshot);
            launchFragment(rock, hitLocation, 0.25D, 0.20D);
            rock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(rockSnapshot));
            this.level().addFreshEntity(rock);
        }
    }

    private void launchFragment(StoneProjectile fragment, Vec3 origin, double maxHorizontalSpeed, double minUpwardSpeed) {
        fragment.setPos(origin.x, origin.y + 0.3D, origin.z);

        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double horizontalSpeed = 0.1D + this.random.nextDouble() * maxHorizontalSpeed;
        double vx = Math.cos(angle) * horizontalSpeed;
        double vz = Math.sin(angle) * horizontalSpeed;
        double vy = minUpwardSpeed + this.random.nextDouble() * 0.25D;

        fragment.setDeltaMovement(vx, vy, vz);
    }
}
