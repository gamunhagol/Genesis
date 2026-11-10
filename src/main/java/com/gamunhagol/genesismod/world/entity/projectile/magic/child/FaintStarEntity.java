package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractStarAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FaintStarEntity extends AbstractStarAnomaly {

    @OnlyIn(Dist.CLIENT)
    private Particle boundParticle;

    public FaintStarEntity(EntityType<? extends FaintStarEntity> entityType, Level level) {
        super(entityType, level);
        this.maxLifespanTicks = 60;
        this.pullRadius = 3.0D;
        this.maxPullStrength = 0.02D;
        this.explosionPower = 3.0F;
    }

    public FaintStarEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.FAINT_STAR.get(), level, owner, snapshot);
        this.maxLifespanTicks = 60;
        this.pullRadius = 3.0D;
        this.maxPullStrength = 0.02D;
        this.explosionPower = 3.0F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void spawnIdleParticles() {
        super.spawnIdleParticles();

        if (this.boundParticle == null || !this.boundParticle.isAlive()) {
            this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                    GenesisParticles.LITTLE_STAR.get(),
                    this.getX(), this.getY() + 0.1D, this.getZ(),
                    0.0D, 0.0D, 0.0D
            );

            if (this.boundParticle != null) {
                int remainingTicks = Math.max(1, this.maxLifespanTicks - this.lifeTicks);
                this.boundParticle.setLifetime(remainingTicks);
                this.boundParticle.scale(2.5F);
            }
        }

        if (this.boundParticle != null && this.boundParticle.isAlive()) {
            Vec3 jitter = this.getGlitchJitterOffset(0.04F, 0.50F, 0.09D);
            this.boundParticle.setPos(
                    this.getX() + jitter.x,
                    this.getY() + 0.1D + jitter.y,
                    this.getZ() + jitter.z
            );
        }
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
}
