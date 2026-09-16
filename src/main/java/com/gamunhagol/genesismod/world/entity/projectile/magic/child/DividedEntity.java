package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.HomingMeteorBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DividedEntity extends HomingMeteorBullet {
    private DamageSnapshot childSnapshot = DamageSnapshot.EMPTY;
    private int nextSpawnDelay = 0;

    public DividedEntity(EntityType<? extends DividedEntity> entityType, Level level) {
        super(entityType, level);
        initSettings();
    }

    public DividedEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, DamageSnapshot childSnapshot) {
        super(GenesisEntities.DIVIDED.get(), level, owner, snapshot);
        this.childSnapshot = childSnapshot;
        initSettings();
    }

    private void initSettings() {
        this.maxSpeed = 0.32D;
        this.homingStrength = 0.04f;
        this.homingDelayTicks = 10;
        this.weakGravity = 0.0005D;
        this.dotLimit = -1.0D;
        this.closeCutoffDistance = 0.0D;

        this.nextSpawnDelay = 5 + this.random.nextInt(8);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.tickCount > this.homingDelayTicks) {
                this.nextSpawnDelay--;

                if (this.nextSpawnDelay <= 0) {
                    int burstCount = 1 + this.random.nextInt(4);
                    for (int i = 0; i < burstCount; i++) {
                        ejectChildMeteor();
                    }

                    this.nextSpawnDelay = 3 + this.random.nextInt(24);
                }
            }
        }
    }

    private void ejectChildMeteor() {
        LivingEntity owner = (LivingEntity) this.getOwner();
        Vec3 forward = this.getDeltaMovement();
        if (forward.lengthSqr() < 1.0E-4D) {
            forward = this.getLookAngle();
        }
        forward = forward.normalize();

        // 최종 분열체인 SmallMagicMeteor를 직접 생성
        SmallMagicMeteor child = new SmallMagicMeteor(this.level(), owner, this.childSnapshot);
        child.setPos(this.getX(), this.getY(), this.getZ());

        Vec3 right = forward.cross(new Vec3(0, 1, 0));
        if (right.lengthSqr() < 1.0E-4D) {
            right = forward.cross(new Vec3(1, 0, 0));
        }
        right = right.normalize();
        Vec3 up = right.cross(forward).normalize();

        double spreadH = (this.random.nextDouble() - 0.5D) * 0.8D;
        double spreadV = (this.random.nextDouble() - 0.2D) * 0.6D;

        Vec3 ejectDir = forward.add(right.scale(spreadH)).add(up.scale(spreadV)).normalize();

        child.setDeltaMovement(ejectDir.scale(0.35D + (this.random.nextDouble() * 0.15D)));
        child.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.childSnapshot));

        this.level().addFreshEntity(child);
    }

    @Override
    protected void spawnFlightParticles() {
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.5D,
                    this.getY() + (this.random.nextDouble() - 0.5D) * 0.5D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.5D,
                    0, 0.01D, 0);
        }
    }
}