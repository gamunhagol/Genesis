package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractStarAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class DimensionalStarEntity extends AbstractStarAnomaly {

    private static final int PHASE_1_END = 60;
    private static final int PHASE_GAP = 15;
    private static final int PHASE_2_START = PHASE_1_END + PHASE_GAP;

    @OnlyIn(Dist.CLIENT)
    private Particle boundParticle;

    public DimensionalStarEntity(EntityType<? extends DimensionalStarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifespanTicks = 600;
        this.pullRadius = 15.0D;
        this.maxPullStrength = 0.15D;
        this.explosionPower = 9.0F;
        this.pullPeakProgress = 400.0F / 600.0F;
    }

    public DimensionalStarEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.DIMENSIONAL_STAR.get(), level, owner, snapshot);
        this.maxLifespanTicks = 600;
        this.pullRadius = 15.0D;
        this.maxPullStrength = 0.15D;
        this.explosionPower = 9.0F;
        this.pullPeakProgress = 400.0F / 600.0F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void spawnIdleParticles() {
        super.spawnIdleParticles();

        Vec3 jitter = Vec3.ZERO;

        if (this.lifeTicks < PHASE_1_END) {
            if (this.boundParticle == null || !this.boundParticle.isAlive()) {
                this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                        GenesisParticles.BLUE_STAR.get(),
                        this.getX(), this.getY() + 0.1D, this.getZ(),
                        0.0D, 0.0D, 0.0D
                );

                if (this.boundParticle != null) {
                    this.boundParticle.setLifetime(PHASE_1_END - this.lifeTicks);
                    this.boundParticle.scale(2.5F);
                }
            }

            float p1Progress = (float) this.lifeTicks / (float) PHASE_1_END;
            jitter = calculatePhaseJitter(p1Progress, 0.05F, 0.90F, 0.08D, 0.25D);
        }
        else if (this.lifeTicks < PHASE_2_START) {
            if (this.boundParticle != null) {
                this.boundParticle.remove();
                this.boundParticle = null;
            }
            return;
        }
        else {
            if (this.boundParticle == null || !this.boundParticle.isAlive()) {
                this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                        GenesisParticles.DIMENSION_STAR.get(),
                        this.getX(), this.getY() + 0.1D, this.getZ(),
                        0.0D, 0.0D, 0.0D
                );

                if (this.boundParticle != null) {
                    int remainingTicks = Math.max(1, this.maxLifespanTicks - this.lifeTicks);
                    this.boundParticle.setLifetime(remainingTicks);
                    this.boundParticle.scale(5.0F);
                }
            }

            int p2Age = this.lifeTicks - PHASE_2_START;
            if (p2Age < 40) {
                float entryGlitchRatio = 1.0F - ((float) p2Age / 40.0F);
                jitter = calculateCustomJitter(0.95F, 0.35D * (0.5D + 0.5D * entryGlitchRatio));
            } else {
                float p2Progress = (float) (this.lifeTicks - (PHASE_2_START + 40)) / (float) (this.maxLifespanTicks - (PHASE_2_START + 40));
                jitter = calculatePhaseJitter(p2Progress, 0.20F, 0.85F, 0.15D, 0.45D);
            }

            if (this.lifeTicks >= 300) {
                spawnMagicLightningParticles();
            }

            if (this.lifeTicks >= 400) {
                spawnNearPortalParticles();
            }
        }

        if (this.boundParticle != null && this.boundParticle.isAlive()) {
            this.boundParticle.setPos(
                    this.getX() + jitter.x,
                    this.getY() + 0.1D + jitter.y,
                    this.getZ() + jitter.z
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnMagicLightningParticles() {
        if (this.random.nextFloat() < 0.21F) {
            double ox = (this.random.nextDouble() - 0.5D) * 2.4D;
            double oy = (this.random.nextDouble() - 0.5D) * 2.4D + 0.1D;
            double oz = (this.random.nextDouble() - 0.5D) * 2.4D;

            Particle lightningParticle = Minecraft.getInstance().particleEngine.createParticle(
                    GenesisParticles.MAGIC_LIGHTNING.get(),
                    this.getX() + ox,
                    this.getY() + oy,
                    this.getZ() + oz,
                    0.0D, 0.0D, 0.0D
            );

            if (lightningParticle != null) {
                float lightningScale = 2.5F + (this.random.nextFloat() * 2.0F);
                lightningParticle.scale(lightningScale);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnNearPortalParticles() {
        int count = 2;
        for (int i = 0; i < count; i++) {
            double theta = this.random.nextDouble() * 2.0D * Math.PI;
            double phi = Math.acos(2.0D * this.random.nextDouble() - 1.0D);
            double dist = 1.5D + (this.random.nextDouble() * 1.0D);

            double ox = dist * Math.sin(phi) * Math.cos(theta);
            double oy = dist * Math.cos(phi) + 0.1D;
            double oz = dist * Math.sin(phi) * Math.sin(theta);

            this.level().addParticle(
                    ParticleTypes.PORTAL,
                    this.getX() + ox,
                    this.getY() + oy,
                    this.getZ() + oz,
                    -ox,
                    -oy,
                    -oz
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

    @OnlyIn(Dist.CLIENT)
    private Vec3 calculateCustomJitter(float chance, double radius) {
        if (this.random.nextFloat() > chance) {
            return Vec3.ZERO;
        }
        double ox = (this.random.nextDouble() - 0.5D) * 2.0D * radius;
        double oy = (this.random.nextDouble() - 0.5D) * 2.0D * radius;
        double oz = (this.random.nextDouble() - 0.5D) * 2.0D * radius;

        return new Vec3(ox, oy, oz);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.level().isClientSide()) {
            removeBoundParticle();
        }
        super.remove(reason);
    }

    @OnlyIn(Dist.CLIENT)
    private void removeBoundParticle() {
        if (this.boundParticle != null) {
            this.boundParticle.remove();
            this.boundParticle = null;
        }
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
