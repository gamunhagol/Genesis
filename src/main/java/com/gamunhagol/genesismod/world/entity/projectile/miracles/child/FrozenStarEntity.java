package com.gamunhagol.genesismod.world.entity.projectile.miracles.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.FrostFogEntity;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

import javax.annotation.Nullable;
import java.util.List;

public class FrozenStarEntity extends MagicEntity implements ItemSupplier {

    private double directRadius = 1.8D;
    private double aoeRadius = 5.0D;
    private double slamRadius = 6.0D;

    private DamageSnapshot aoeSnapshot = DamageSnapshot.EMPTY;
    private DamageSnapshot fogSnapshot = DamageSnapshot.EMPTY;
    private float shockwaveDamage = 0.0F;

    private boolean detonated = false;
    private boolean fogSpawned = false;

    public FrozenStarEntity(EntityType<? extends FrozenStarEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.maxLifeTicks = 600;
    }

    public FrozenStarEntity(Level level, LivingEntity owner, DamageSnapshot directSnapshot, DamageSnapshot aoeSnapshot, DamageSnapshot fogSnapshot, float shockwaveDamage) {
        super(GenesisEntities.FROZEN_STAR.get(), level, owner, directSnapshot);
        this.aoeSnapshot = aoeSnapshot;
        this.fogSnapshot = fogSnapshot;
        this.shockwaveDamage = shockwaveDamage;
        this.setNoGravity(true);
        this.maxLifeTicks = 600;
    }

    public void setSnapshots(DamageSnapshot directSnapshot, DamageSnapshot aoeSnapshot, DamageSnapshot fogSnapshot, float shockwaveDamage) {
        this.damageSnapshot = directSnapshot;
        this.aoeSnapshot = aoeSnapshot;
        this.fogSnapshot = fogSnapshot;
        this.shockwaveDamage = shockwaveDamage;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && !this.isCharging() && !this.isHit() && (this.lifeTicks + 1) >= this.maxLifeTicks) {
            spawnFrostFog(this.position());
        }

        super.tick();

        if (this.isHit() || this.isRemoved()) {
            return;
        }

        Vec3 motion = this.getDeltaMovement();

        if (this.level().isClientSide) {
            spawnTrailParticles(motion);
        } else {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }

            if (this.isRemoved() || this.isHit()) return;

            if (this.onGround()) {
                explode(this.position(), null);
                return;
            }
        }

        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    private void spawnTrailParticles(Vec3 motion) {
        double centerX = this.getX();
        double centerY = this.getY() + this.getBbHeight() * 0.5D;
        double centerZ = this.getZ();

        int count = 4;
        for (int i = 0; i < count; i++) {
            double step = (double) i / (double) count;
            double px = centerX - motion.x * step + (this.random.nextDouble() - 0.5D) * 0.35D;
            double py = centerY - motion.y * step + (this.random.nextDouble() - 0.5D) * 0.35D;
            double pz = centerZ - motion.z * step + (this.random.nextDouble() - 0.5D) * 0.35D;

            double vx = (this.random.nextDouble() - 0.5D) * 0.03D;
            double vy = (this.random.nextDouble() - 0.5D) * 0.03D;
            double vz = (this.random.nextDouble() - 0.5D) * 0.03D;

            this.level().addParticle(ParticleTypes.SNOWFLAKE, px, py, pz, vx, vy, vz);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide && !this.detonated) {
            explode(result.getLocation(), result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && !this.detonated) {
            explode(result.getLocation(), null);
        }
    }

    private void explode(Vec3 impactPos, @Nullable Entity hitTarget) {
        if (this.detonated) return;
        this.detonated = true;

        if (this.level() instanceof ServerLevel serverLevel) {
            Entity owner = this.getOwner();
            LivingEntity caster = owner instanceof LivingEntity livingOwner ? livingOwner : null;

            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                cap.setSnapshot(this.damageSnapshot);
            });

            if (caster != null) {
                Vec3 slamPos = new Vec3(impactPos.x, impactPos.y - 0.2D, impactPos.z);
                LevelUtil.circleSlamFracture(caster, serverLevel, slamPos, this.slamRadius, false, false, false);

                if (this.shockwaveDamage > 0.0F) {
                    EpicFightDamageSource shockwaveSource = EpicFightDamageSources.shockwave(caster);
                    AABB shockwaveBox = new AABB(
                            slamPos.x - this.slamRadius, slamPos.y - 1.5D, slamPos.z - this.slamRadius,
                            slamPos.x + this.slamRadius, slamPos.y + 2.5D, slamPos.z + this.slamRadius
                    );
                    List<LivingEntity> shockwaveTargets = serverLevel.getEntitiesOfClass(
                            LivingEntity.class, shockwaveBox, e -> e.isAlive() && e != caster
                    );
                    for (LivingEntity target : shockwaveTargets) {
                        target.hurt(shockwaveSource, this.shockwaveDamage);
                    }
                }
            }

            AABB directHitBox = new AABB(
                    impactPos.x - this.directRadius, impactPos.y - 1.0D, impactPos.z - this.directRadius,
                    impactPos.x + this.directRadius, impactPos.y + 2.5D, impactPos.z + this.directRadius
            );
            List<LivingEntity> directTargets = serverLevel.getEntitiesOfClass(
                    LivingEntity.class, directHitBox, e -> e.isAlive() && e != caster
            );

            if (hitTarget instanceof LivingEntity directVictim && !directTargets.contains(directVictim) && directVictim != caster) {
                directTargets.add(directVictim);
            }

            for (LivingEntity directTarget : directTargets) {
                if (caster != null) {
                    GenesisDamageCalculator.applySnapshotDamage(directTarget, caster, this.damageSnapshot);
                } else {
                    directTarget.hurt(this.damageSources().thrown(this, owner), this.damageSnapshot.physical());
                }
            }

            AABB aoeBox = new AABB(
                    impactPos.x - this.aoeRadius, impactPos.y - this.aoeRadius, impactPos.z - this.aoeRadius,
                    impactPos.x + this.aoeRadius, impactPos.y + this.aoeRadius, impactPos.z + this.aoeRadius
            );
            List<LivingEntity> aoeTargets = serverLevel.getEntitiesOfClass(
                    LivingEntity.class, aoeBox, e -> e.isAlive() && e != caster && !directTargets.contains(e)
            );

            for (LivingEntity aoeTarget : aoeTargets) {
                if (caster != null) {
                    GenesisDamageCalculator.applySnapshotDamage(aoeTarget, caster, this.aoeSnapshot);
                }
            }

            spawnFrostFog(impactPos);
        }

        this.triggerHit(impactPos);
    }

    private void spawnFrostFog(Vec3 pos) {
        if (this.fogSpawned || this.level().isClientSide) return;
        this.fogSpawned = true;

        LivingEntity caster = this.getOwner() instanceof LivingEntity livingOwner ? livingOwner : null;
        FrostFogEntity fog = new FrostFogEntity(this.level(), caster, this.fogSnapshot);
        fog.setPos(pos.x, pos.y, pos.z);
        this.level().addFreshEntity(fog);
    }
}
