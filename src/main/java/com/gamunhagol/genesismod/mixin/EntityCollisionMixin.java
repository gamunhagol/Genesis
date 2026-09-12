package com.gamunhagol.genesismod.mixin;

import com.gamunhagol.genesismod.world.border.SpatialRuptureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityCollisionMixin {

    @Shadow public abstract AABB getBoundingBox();

    @Inject(method = "collide", at = @At("RETURN"), cancellable = true)
    private void genesis$enforceSpatialRuptureWall(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        if (SpatialRuptureManager.ACTIVE_ZONES.isEmpty()) return;

        Entity self = (Entity)(Object)this;
        Vec3 result = cir.getReturnValue();
        AABB box = this.getBoundingBox();

        double dx = result.x;
        double dz = result.z;
        ResourceLocation currentDim = self.level().dimension().location();

        for (SpatialRuptureManager.RuptureZone zone : SpatialRuptureManager.ACTIVE_ZONES) {
            if (!zone.dimension.equals(currentDim)) continue;

            double curX = (box.minX + box.maxX) / 2.0D;
            double curZ = (box.minZ + box.maxZ) / 2.0D;

            boolean currentlyInside = curX > zone.minX && curX < zone.maxX && curZ > zone.minZ && curZ < zone.maxZ;

            double nextMinX = box.minX + dx;
            double nextMaxX = box.maxX + dx;
            double nextMinZ = box.minZ + dz;
            double nextMaxZ = box.maxZ + dz;

            if (currentlyInside) {
                if (nextMinX <= zone.minX || nextMaxX >= zone.maxX) dx = 0;
                if (nextMinZ <= zone.minZ || nextMaxZ >= zone.maxZ) dz = 0;
            } else {
                boolean willIntersectX = nextMaxX > zone.minX && nextMinX < zone.maxX;
                boolean willIntersectZ = nextMaxZ > zone.minZ && nextMinZ < zone.maxZ;

                if (willIntersectX && willIntersectZ) {
                    if (box.maxX <= zone.minX || box.minX >= zone.maxX) dx = 0;
                    if (box.maxZ <= zone.minZ || box.minZ >= zone.maxZ) dz = 0;
                }
            }
        }

        if (dx != result.x || dz != result.z) {
            cir.setReturnValue(new Vec3(dx, result.y, dz));
        }
    }
}