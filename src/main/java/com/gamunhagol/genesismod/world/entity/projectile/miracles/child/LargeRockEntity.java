package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

import java.util.List;

public class LargeRockEntity extends StoneProjectile {
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

    public void setSlamRadius(double radius) {
        this.slamRadius = radius;
    }

    public void setShockwaveMultiplier(float multiplier) {
        this.shockwaveMultiplier = multiplier;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
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
        }

        super.onHitBlock(result);
    }
}