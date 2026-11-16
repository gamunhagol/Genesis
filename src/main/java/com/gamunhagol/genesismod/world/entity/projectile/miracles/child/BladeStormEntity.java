package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
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

    protected double maxTurnRate = 0.16D;
    protected double homingRadius = 28.0D;
    protected int homingDelayTicks = 2;
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
            double distSqr = toTarget.lengthSqr();

            if (distSqr < 0.04D) {
                return currentMotion;
            }

            Vec3 motionDir = currentMotion.normalize();
            Vec3 targetDir = toTarget.normalize();
            double speed = currentMotion.length();

            if (motionDir.dot(targetDir) < -0.25D) {
                this.lockedTarget = null;
                return currentMotion;
            }

            Vec3 newDir = rotateTowards(motionDir, targetDir, this.maxTurnRate);
            return newDir.scale(speed);
        }

        return currentMotion;
    }

    protected Vec3 rotateTowards(Vec3 from, Vec3 to, double maxAngleRadians) {
        double dot = Mth.clamp(from.dot(to), -1.0D, 1.0D);
        double angle = Math.acos(dot);

        if (angle <= maxAngleRadians) {
            return to;
        }

        return from.lerp(to, maxAngleRadians / angle).normalize();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
