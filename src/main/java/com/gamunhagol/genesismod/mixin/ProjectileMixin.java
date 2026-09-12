package com.gamunhagol.genesismod.mixin;

import com.gamunhagol.genesismod.world.border.SpatialRuptureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {

    @Shadow protected abstract void onHit(HitResult result);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void genesis$interceptRuptureCollision(CallbackInfo ci) {
        Projectile self = (Projectile)(Object)this;

        if (self.getPersistentData().contains("GenesisRuptureHitTime")) {
            long hitTime = self.getPersistentData().getLong("GenesisRuptureHitTime");

            if (self.level().getGameTime() >= hitTime + 40L) {
                if (!self.level().isClientSide) {
                    self.level().playSound(
                            null,
                            self.getX(),
                            self.getY(),
                            self.getZ(),
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.PLAYERS,
                            0.4F,
                            1.2F
                    );
                }

                self.discard();
                ci.cancel();
            } else {
                self.setDeltaMovement(Vec3.ZERO);
            }
            return;
        }

        if (SpatialRuptureManager.ACTIVE_ZONES.isEmpty()) return;

        Vec3 start = self.position();
        Vec3 motion = self.getDeltaMovement();

        if (motion.lengthSqr() < 1.0E-5D) return;

        Vec3 end = start.add(motion);
        ResourceLocation currentDim = self.level().dimension().location();

        for (SpatialRuptureManager.RuptureZone zone : SpatialRuptureManager.ACTIVE_ZONES) {
            if (!zone.dimension.equals(currentDim)) continue;

            Vec3 hitVec = getIntersection(start, end, zone);
            if (hitVec != null) {
                Direction face = getHitDirection(start, zone);
                BlockHitResult hitResult = new BlockHitResult(hitVec, face, BlockPos.containing(hitVec), false);

                this.onHit(hitResult);

                if (self.isAlive()) {
                    self.setDeltaMovement(Vec3.ZERO);
                    self.setNoGravity(true);
                    self.getPersistentData().putLong("GenesisRuptureHitTime", self.level().getGameTime());
                }
                break;
            }
        }
    }

    private static Vec3 getIntersection(Vec3 from, Vec3 to, SpatialRuptureManager.RuptureZone zone) {
        boolean fromIn = from.x >= zone.minX && from.x <= zone.maxX && from.z >= zone.minZ && from.z <= zone.maxZ;
        boolean toIn = to.x >= zone.minX && to.x <= zone.maxX && to.z >= zone.minZ && to.z <= zone.maxZ;

        if (fromIn == toIn) return null;

        double tMin = 1.0D;
        Vec3 hit = null;

        double[] planesX = {zone.minX, zone.maxX};
        for (double px : planesX) {
            if ((from.x < px && to.x >= px) || (from.x > px && to.x <= px)) {
                double t = (px - from.x) / (to.x - from.x);
                double z = from.z + t * (to.z - from.z);
                if (z >= zone.minZ && z <= zone.maxZ && t < tMin) {
                    tMin = t;
                    hit = new Vec3(px, from.y + t * (to.y - from.y), z);
                }
            }
        }

        double[] planesZ = {zone.minZ, zone.maxZ};
        for (double pz : planesZ) {
            if ((from.z < pz && to.z >= pz) || (from.z > pz && to.z <= pz)) {
                double t = (pz - from.z) / (to.z - from.z);
                double x = from.x + t * (to.x - from.x);
                if (x >= zone.minX && x <= zone.maxX && t < tMin) {
                    tMin = t;
                    hit = new Vec3(x, from.y + t * (to.y - from.y), pz);
                }
            }
        }

        return hit;
    }

    private static Direction getHitDirection(Vec3 from, SpatialRuptureManager.RuptureZone zone) {
        if (from.x <= zone.minX) return Direction.WEST;
        if (from.x >= zone.maxX) return Direction.EAST;
        if (from.z <= zone.minZ) return Direction.NORTH;
        return Direction.SOUTH;
    }
}