package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.HomingMeteorBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MagicMeteor extends HomingMeteorBullet {
    private boolean canSplit = false;
    private boolean hasSplit = false;
    private int splitDelayTicks = 20;

    public MagicMeteor(EntityType<? extends MagicMeteor> entityType, Level level) {
        super(entityType, level);
        this.homingStrength = 0.18f;
        this.dotLimit = 0.15D;
        this.closeCutoffDistance = 0.8D;
    }

    public MagicMeteor(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_METEOR.get(), level, owner, snapshot);
        this.homingStrength = 0.18f;
        this.dotLimit = 0.15D;
        this.closeCutoffDistance = 0.8D;
    }

    public MagicMeteor setSplitting(boolean split, int delayTicks) {
        this.canSplit = split;
        this.splitDelayTicks = delayTicks;
        return this;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && this.canSplit && !this.hasSplit && this.tickCount >= this.splitDelayTicks) {
            splitIntoChildren();
        }
    }

    private void splitIntoChildren() {
        this.hasSplit = true;
        LivingEntity owner = (LivingEntity) this.getOwner();
        Vec3 forward = this.getDeltaMovement().normalize();

        DamageSnapshot childSnapshot = new DamageSnapshot(
                0, this.damageSnapshot.magic() * 0.35f, 0, 0, 0, 0, 0
        );

        Vec3[] spreadDirs = new Vec3[] {
                forward.add(0.35D, 0.2D, 0).normalize(),
                forward.add(-0.35D, 0.2D, 0).normalize(),
                forward.add(0, -0.2D, 0.35D).normalize(),
                forward.add(0, -0.2D, -0.35D).normalize()
        };

        for (Vec3 dir : spreadDirs) {
            SmallMagicMeteor child = new SmallMagicMeteor(this.level(), owner, childSnapshot);
            child.setPos(this.getX(), this.getY(), this.getZ());
            child.setDeltaMovement(dir.scale(0.25D));
            child.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(childSnapshot));
            this.level().addFreshEntity(child);
        }

        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide && this.canSplit && !this.hasSplit) {
            splitIntoChildren();
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide && this.canSplit && !this.hasSplit) {
            splitIntoChildren();
        }
        super.onHitBlock(result);
    }

    @Override
    protected void spawnFlightParticles() {
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.2D,
                    this.getY() + 0.1D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.2D,
                    0, 0.01D, 0);
        }
    }
}