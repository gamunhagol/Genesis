package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.Comparator;
import java.util.List;

public class BladeStormEntity extends AbstractArrow {

    protected float homingStrength = 0.09f;
    protected double homingRadius = 24.0D;
    protected int homingDelayTicks = 4;
    protected LivingEntity lockedTarget = null;

    public BladeStormEntity(EntityType<? extends BladeStormEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public BladeStormEntity(Level level, LivingEntity shooter) {
        super(GenesisEntities.BLADE_STORM.get(), shooter, level);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && !this.inGround && this.tickCount > this.homingDelayTicks) {
            Vec3 currentMotion = this.getDeltaMovement();
            Vec3 homingMotion = applyHoming(currentMotion);
            this.setDeltaMovement(homingMotion);
        }

        super.tick();

        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(
                    ParticleTypes.SWEEP_ATTACK,
                    this.getX(), this.getY(), this.getZ(),
                    0.0D, 0.0D, 0.0D
            );
        }
    }

    protected Vec3 applyHoming(Vec3 currentMotion) {
        if (this.lockedTarget == null || !this.lockedTarget.isAlive() || this.lockedTarget.isRemoved()) {
            List<LivingEntity> targets = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox().inflate(this.homingRadius),
                    e -> e != this.getOwner() && e.isAlive() && (e instanceof Enemy || e != this.getOwner())
            );

            this.lockedTarget = targets.stream()
                    .min(Comparator.comparingDouble(this::distanceToSqr))
                    .orElse(null);
        }

        if (this.lockedTarget != null) {
            Vec3 toTarget = this.lockedTarget.getBoundingBox().getCenter().subtract(this.position());
            double distance = toTarget.length();

            if (distance < 0.5D) {
                return currentMotion;
            }

            Vec3 motionDir = currentMotion.normalize();
            Vec3 targetDir = toTarget.normalize();
            double speed = currentMotion.length();

            if (motionDir.dot(targetDir) > 0.0D) {
                Vec3 newDir = motionDir.lerp(targetDir, this.homingStrength).normalize();
                return newDir.scale(speed);
            } else {
                this.lockedTarget = null;
            }
        }

        return currentMotion;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}