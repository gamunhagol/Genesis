package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StarGaspEntity extends MagicEntity implements ItemSupplier {
    protected int maxLifespanTicks = 290;
    protected double pullRadius = 15.0D;
    protected double maxPullStrength = 0.25D;
    protected double contactRadius = 1.0D;
    protected float explosionPower = 3.0F;

    public StarGaspEntity(EntityType<? extends StarGaspEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
    }

    public StarGaspEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.STAR_GASP.get(), level, owner, snapshot);
        this.setNoGravity(true);
        this.setMaxLifeTicks(this.maxLifespanTicks + 10);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.SNOWBALL);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);

        handleSounds();

        if (this.level().isClientSide) {
            spawnIdleParticles();
        } else {
            applyGravitationalPull();

            if (this.tickCount % 10 == 0) {
                applyContactDamage();
            }

            if (this.tickCount >= this.maxLifespanTicks) {
                detonate();
                this.discard();
            }
        }
    }

    private void handleSounds() {
        if (this.level().isClientSide) return;

        if (this.tickCount < 230) {
            if (this.tickCount % 40 == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.PORTAL_AMBIENT, SoundSource.WEATHER, 0.25F, 1.0F);
            }
        } else {
            if (this.tickCount % 20 == 0) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.PORTAL_TRIGGER, SoundSource.WEATHER, 0.45F, 1.15F);
            }
        }
    }

    protected void applyGravitationalPull() {
        AABB box = this.getBoundingBox().inflate(this.pullRadius);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e.isAlive() && e != owner
        );

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

    protected void applyContactDamage() {
        AABB contactArea = this.getBoundingBox().inflate(this.contactRadius);
        Entity owner = this.getOwner();

        List<LivingEntity> targets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                contactArea,
                e -> e.isAlive() && e != owner
        );

        for (LivingEntity target : targets) {
            target.hurt(this.damageSources().indirectMagic(this, owner), this.damageSnapshot.magic());
            target.hurt(this.damageSources().fellOutOfWorld(), 1.5F);
        }
    }

    protected void detonate() {
        this.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> {
            cap.setSnapshot(this.damageSnapshot);
        });

        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.MOB
        );
    }

    protected void spawnIdleParticles() {
        this.level().addParticle(ParticleTypes.PORTAL,
                this.getX() + (this.random.nextDouble() - 0.5D) * 0.8D,
                this.getY() + (this.random.nextDouble() - 0.5D) * 0.8D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 0.8D,
                (this.random.nextDouble() - 0.5D) * 0.2D,
                (this.random.nextDouble() - 0.5D) * 0.2D,
                (this.random.nextDouble() - 0.5D) * 0.2D);
    }
}