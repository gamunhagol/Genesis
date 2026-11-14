package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SupernovaEntity extends MagicEntity {
    protected int flightTicks = 140;
    protected int decelerateTicks = 200;
    protected int maxLifespanTicks = 400;

    protected int gapDuration = 10;
    protected int lightingDuration = 6;
    protected int disruptionDuration = 40;

    protected float twinklingScale = 1.8F;
    protected float disruptionScale = 2.5F;

    protected float contactExplosionPower = 2.0F;
    protected float apexExplosionPower = 90.0F;

    private double baseSpeed = 0.19D;

    @OnlyIn(Dist.CLIENT)
    private Particle boundParticle;

    @OnlyIn(Dist.CLIENT)
    private int currentParticlePhase = 0;

    public SupernovaEntity(EntityType<? extends SupernovaEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
    }

    public SupernovaEntity(Level level, LivingEntity owner, DamageSnapshot snapshot, Vec3 direction) {
        super(GenesisEntities.SUPERNOVA.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
        this.setDeltaMovement(direction.normalize().scale(this.baseSpeed));
        this.hasImpulse = true;
    }

    @Override
    public void tick() {
        if (this.isRemoved()) {
            if (this.level().isClientSide) {
                removeBoundParticle();
            }
            return;
        }

        super.tick();

        handleMovementPhases();

        if (this.level().isClientSide) {
            handleParticles();
        } else {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }

            checkProximityContact();

            if (this.isRemoved()) return;

            if (this.tickCount >= this.maxLifespanTicks) {
                detonate(this.apexExplosionPower);
                this.discard();
                return;
            }
        }

        Vec3 motion = this.getDeltaMovement();
        this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
        this.checkInsideBlocks();
    }

    @OnlyIn(Dist.CLIENT)
    private void handleParticles() {
        int disruptionStart = this.maxLifespanTicks - this.disruptionDuration;
        int lightingStart = disruptionStart - this.lightingDuration;
        int twinklingEnd = lightingStart - this.gapDuration;

        double centerY = this.getY() + (this.getBbHeight() * 0.5D);
        Vec3 jitter = Vec3.ZERO;

        if (this.tickCount < twinklingEnd) {
            if (this.currentParticlePhase != 1) {
                removeBoundParticle();
                this.currentParticlePhase = 1;
                this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                        GenesisParticles.TWINKLING_STAR.get(),
                        this.getX(), centerY, this.getZ(),
                        0.0D, 0.0D, 0.0D
                );
                if (this.boundParticle != null) {
                    this.boundParticle.setLifetime(twinklingEnd - this.tickCount);
                    this.boundParticle.scale(this.twinklingScale);
                }
            }

            if (this.tickCount >= this.flightTicks) {
                float progress = (float) (this.tickCount - this.flightTicks) / (float) (twinklingEnd - this.flightTicks);
                jitter = calculatePhaseJitter(progress, 0.10F, 0.95F, 0.05D, 0.35D);
            }
        } else if (this.tickCount < lightingStart) {
            if (this.currentParticlePhase != 2) {
                removeBoundParticle();
                this.currentParticlePhase = 2;
            }
            return;
        } else if (this.tickCount < disruptionStart) {
            if (this.currentParticlePhase != 3) {
                removeBoundParticle();
                this.currentParticlePhase = 3;
                this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                        GenesisParticles.LIGHTING_STAR.get(),
                        this.getX(), centerY, this.getZ(),
                        0.0D, 0.0D, 0.0D
                );
                if (this.boundParticle != null) {
                    this.boundParticle.setLifetime(this.lightingDuration);
                }
            }
        } else {
            if (this.currentParticlePhase != 4) {
                removeBoundParticle();
                this.currentParticlePhase = 4;
                this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                        GenesisParticles.DISRUPTION.get(),
                        this.getX(), centerY, this.getZ(),
                        0.0D, 0.0D, 0.0D
                );
                if (this.boundParticle != null) {
                    this.boundParticle.setLifetime(this.disruptionDuration);
                    this.boundParticle.scale(this.disruptionScale);
                }
            }
        }

        if (this.boundParticle != null && this.boundParticle.isAlive()) {
            this.boundParticle.setPos(
                    this.getX() + jitter.x,
                    centerY + jitter.y,
                    this.getZ() + jitter.z
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    private Vec3 calculatePhaseJitter(float progress, float minChance, float maxChance, double minRadius, double maxRadius) {
        float currentChance = minChance + (maxChance - minChance) * (progress * progress);
        if (this.random.nextFloat() > currentChance) {
            return Vec3.ZERO;
        }

        double currentRadius = minRadius + (maxRadius - minRadius) * progress;
        double ox = (this.random.nextDouble() - 0.5D) * 2.0D * currentRadius;
        double oy = (this.random.nextDouble() - 0.5D) * 2.0D * currentRadius;
        double oz = (this.random.nextDouble() - 0.5D) * 2.0D * currentRadius;

        return new Vec3(ox, oy, oz);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.level().isClientSide()) {
            removeBoundParticle();
        }
        super.remove(reason);
    }

    @Override
    public void onClientRemoval() {
        removeBoundParticle();
        super.onClientRemoval();
    }

    @OnlyIn(Dist.CLIENT)
    private void removeBoundParticle() {
        if (this.boundParticle != null) {
            this.boundParticle.remove();
            this.boundParticle = null;
        }
    }

    private void handleMovementPhases() {
        Vec3 currentMotion = this.getDeltaMovement();

        if (this.tickCount <= this.flightTicks) {
            if (currentMotion.lengthSqr() > 1.0E-6D) {
                this.setDeltaMovement(currentMotion.normalize().scale(this.baseSpeed));
            }
        } else if (this.tickCount <= this.decelerateTicks) {
            float progress = (float) (this.tickCount - this.flightTicks) / (this.decelerateTicks - this.flightTicks);
            double currentSpeed = this.baseSpeed * Math.max(0.0D, 1.0D - progress);

            if (currentMotion.lengthSqr() > 1.0E-6D) {
                this.setDeltaMovement(currentMotion.normalize().scale(currentSpeed));
            } else {
                this.setDeltaMovement(Vec3.ZERO);
            }
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    private void checkProximityContact() {
        AABB touchBox = this.getBoundingBox().inflate(0.3D);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                touchBox,
                e -> e.isAlive() && e != this.getOwner()
        );

        if (!targets.isEmpty()) {
            handleCollision(targets.get(0));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            handleCollision(result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide && !this.isRemoved()) {
            handleCollision(null);
        }
    }

    private void handleCollision(Entity hitEntity) {
        if (hitEntity instanceof LivingEntity target && hitEntity != this.getOwner()) {
            Entity owner = this.getOwner();
            this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
                cap.setSnapshot(this.damageSnapshot);
            });
            target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
        }

        detonate(this.contactExplosionPower);
        this.discard();
    }

    protected void detonate(float explosionPower) {
        Entity owner = this.getOwner();

        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                explosionPower,
                Level.ExplosionInteraction.MOB
        );

        double range = explosionPower;
        AABB voidArea = this.getBoundingBox().inflate(range);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                voidArea,
                e -> e.isAlive() && e != owner
        );

        double rSqr = range * range;
        for (LivingEntity target : targets) {
            if (target.distanceToSqr(this) <= rSqr) {
                target.hurt(this.damageSources().fellOutOfWorld(), 30.0F);
            }
        }
    }
}
