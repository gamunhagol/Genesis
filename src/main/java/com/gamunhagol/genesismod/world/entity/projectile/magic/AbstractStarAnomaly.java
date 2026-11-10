package com.gamunhagol.genesismod.world.entity.projectile.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class AbstractStarAnomaly extends MagicEntity implements ItemSupplier {
    protected int maxLifespanTicks = 60;
    protected double pullRadius = 4.0D;
    protected double maxPullStrength = 0.08D;
    protected float explosionPower = 4.0F;

    public AbstractStarAnomaly(EntityType<? extends AbstractStarAnomaly> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public AbstractStarAnomaly(EntityType<? extends AbstractStarAnomaly> entityType, Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(entityType, level, owner, snapshot);
        this.setNoGravity(true);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);

        if (this.level().isClientSide) {
            spawnIdleParticles();
        } else {
            applyGravitationalPull();

            if (this.lifeTicks >= this.maxLifespanTicks) {
                this.detonate();
                this.discard();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void spawnIdleParticles() {
        Vec3 entityCenter = this.getBoundingBox().getCenter();
        double pullSpeed = 0.16D;
        int rayCount = 4;

        for (int i = 0; i < rayCount; i++) {
            double theta = this.random.nextDouble() * 2.0D * Math.PI;
            double phi = Math.acos(2.0D * this.random.nextDouble() - 1.0D);

            double dirX = Math.sin(phi) * Math.cos(theta);
            double dirY = Math.cos(phi);
            double dirZ = Math.sin(phi) * Math.sin(theta);
            Vec3 dir = new Vec3(dirX, dirY, dirZ);

            Vec3 rayEnd = entityCenter.add(dir.scale(this.pullRadius));

            BlockHitResult hit = this.level().clip(new ClipContext(
                    entityCenter,
                    rayEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.ANY,
                    this
            ));

            Vec3 spawnPos;
            ParticleOptions targetParticle;
            BlockPos hitBlockPos = null;

            if (hit.getType() == HitResult.Type.BLOCK) {
                hitBlockPos = hit.getBlockPos();
                BlockState state = this.level().getBlockState(hitBlockPos);
                FluidState fluid = this.level().getFluidState(hitBlockPos);

                spawnPos = hit.getLocation().add(
                        hit.getDirection().step().x * 0.05D,
                        hit.getDirection().step().y * 0.05D,
                        hit.getDirection().step().z * 0.05D
                );
                targetParticle = resolveEnvironmentalParticle(hitBlockPos, state, fluid);
            } else {
                spawnPos = rayEnd;
                targetParticle = ParticleTypes.SMOKE;
            }

            Vec3 toCenter = entityCenter.subtract(spawnPos);
            double dist = toCenter.length();

            if (dist < 0.2D) {
                continue;
            }

            Vec3 pullDir = toCenter.normalize().scale(pullSpeed);

            Particle particle = Minecraft.getInstance().particleEngine.createParticle(
                    targetParticle,
                    spawnPos.x, spawnPos.y, spawnPos.z,
                    pullDir.x, pullDir.y, pullDir.z
            );

            if (particle != null && targetParticle == ParticleTypes.SMOKE) {
                applySmokeTint(particle, hitBlockPos);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void applySmokeTint(Particle particle, BlockPos pos) {
        if (pos == null) {
            particle.setColor(0.35F, 0.35F, 0.38F);
            return;
        }

        BlockState state = this.level().getBlockState(pos);
        FluidState fluid = this.level().getFluidState(pos);

        if (fluid.is(FluidTags.WATER) || state.is(Blocks.WATER)) {
            particle.setColor(0.25F, 0.60F, 0.90F);
        } else if (state.is(BlockTags.SAND)) {
            particle.setColor(0.85F, 0.75F, 0.45F);
        } else {
            particle.setColor(0.35F, 0.35F, 0.38F);
        }
    }

    private ParticleOptions resolveEnvironmentalParticle(BlockPos pos, BlockState state, FluidState fluid) {
        if (fluid.is(FluidTags.WATER) || state.is(Blocks.WATER)) {
            return ParticleTypes.SMOKE;
        }

        if (fluid.is(FluidTags.LAVA) || state.is(Blocks.LAVA) || state.is(BlockTags.FIRE) || state.is(Blocks.CAMPFIRE)) {
            return ParticleTypes.SMALL_FLAME;
        }

        if (state.is(Blocks.CHERRY_LEAVES)) {
            return ParticleTypes.CHERRY_LEAVES;
        }

        if (state.is(BlockTags.SAND)) {
            return ParticleTypes.SMOKE;
        }

        return ParticleTypes.SMOKE;
    }

    public float getInstability(float startThreshold) {
        float progress = (float) this.lifeTicks / (float) this.maxLifespanTicks;
        if (progress < startThreshold) {
            return 0.0F;
        }
        return (progress - startThreshold) / (1.0F - startThreshold);
    }

    public Vec3 getGlitchJitterOffset(float baseChance, float maxChance, double maxJitter) {
        float progress = (float) this.lifeTicks / (float) this.maxLifespanTicks;
        float currentChance = baseChance + (maxChance - baseChance) * (progress * progress);

        if (this.random.nextFloat() > currentChance) {
            return Vec3.ZERO;
        }

        double strength = maxJitter * (0.3D + 0.7D * progress);
        double ox = (this.random.nextDouble() - 0.5D) * 2.0D * strength;
        double oy = (this.random.nextDouble() - 0.5D) * 2.0D * strength;
        double oz = (this.random.nextDouble() - 0.5D) * 2.0D * strength;

        return new Vec3(ox, oy, oz);
    }

    protected void applyGravitationalPull() {
        AABB box = this.getBoundingBox().inflate(this.pullRadius);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, box, Entity::isAlive);
        Vec3 center = this.position();

        for (LivingEntity target : targets) {
            Vec3 toCenter = center.subtract(target.position());
            double dist = toCenter.length();

            if (dist > 0.1D && dist <= this.pullRadius) {
                double falloff = 1.0D - (dist / this.pullRadius);
                double currentPull = this.maxPullStrength * falloff;

                Vec3 pullVector = toCenter.normalize().scale(currentPull);
                target.setDeltaMovement(target.getDeltaMovement().add(pullVector));
                target.hurtMarked = true;
            }
        }
    }

    protected void detonate() {
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(this.damageSnapshot));

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.MOB
        );

        applyMagicDamageToArea(this.explosionPower * 1.5D);
    }

    protected void applyMagicDamageToArea(double range) {
        AABB damageArea = this.getBoundingBox().inflate(range);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, damageArea, Entity::isAlive);
        Entity owner = this.getOwner();
        double rSqr = range * range;

        for (LivingEntity target : targets) {
            if (target.distanceToSqr(this) <= rSqr) {
                target.hurt(this.damageSources().indirectMagic(this, owner), 1.0F);
            }
        }
    }
}
