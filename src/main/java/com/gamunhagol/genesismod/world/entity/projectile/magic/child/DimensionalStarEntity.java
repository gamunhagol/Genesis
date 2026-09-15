package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractStarAnomaly;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DimensionalStarEntity extends AbstractStarAnomaly {
    public DimensionalStarEntity(EntityType<? extends DimensionalStarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifespanTicks = 180;
        this.pullRadius = 15.0D;
        this.maxPullStrength = 0.15D;
        this.explosionPower = 10.0F;
    }

    public DimensionalStarEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.DIMENSIONAL_STAR.get(), level, owner, snapshot);
        this.maxLifespanTicks = 180;
        this.pullRadius = 15.0D;
        this.maxPullStrength = 0.15D;
        this.explosionPower = 10.0F;
    }

    @Override
    protected void detonate() {
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        eraseBlocksInSphere(15);

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.NONE
        );

        AABB damageArea = this.getBoundingBox().inflate(15.0D);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, damageArea, Entity::isAlive);
        Entity owner = this.getOwner();
        double rSqr = 15.0D * 15.0D;

        for (LivingEntity target : targets) {
            if (target.distanceToSqr(this) <= rSqr) {
                target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
                target.hurt(this.damageSources().fellOutOfWorld(), 29.0F);
            }
        }
    }

    private void eraseBlocksInSphere(int radius) {
        BlockPos centerPos = this.blockPosition();
        int rSqr = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((x * x + y * y + z * z) <= rSqr) {
                        BlockPos targetPos = centerPos.offset(x, y, z);
                        BlockState state = this.level().getBlockState(targetPos);

                        if (!state.isAir() && state.getDestroySpeed(this.level(), targetPos) >= 0.0F) {
                            this.level().setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
}