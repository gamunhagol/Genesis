package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.StoneProjectile;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
    public RockEntity(EntityType<? extends RockEntity> type, Level level) {
        super(type, level);
        this.gravity = 0.045D;
    }

    public RockEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.ROCK.get(), level, owner, snapshot);
        this.gravity = 0.045D;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
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
