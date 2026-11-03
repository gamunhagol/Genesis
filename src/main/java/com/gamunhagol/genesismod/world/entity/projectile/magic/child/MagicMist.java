package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicTrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagicMist extends MagicTrap {

    @OnlyIn(Dist.CLIENT)
    private Particle boundParticle;

    public MagicMist(EntityType<? extends MagicMist> type, Level level) {
        super(type, level);
        this.triggerRadius = 0.8D;
        this.maxLifeTicks = 200;
    }

    public MagicMist(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.MAGIC_MIST.get(), level, owner, snapshot);
        this.triggerRadius = 0.8D;
        this.maxLifeTicks = 200;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            ensureBodyParticle();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void ensureBodyParticle() {
        if (this.boundParticle == null || !this.boundParticle.isAlive()) {
            this.boundParticle = Minecraft.getInstance().particleEngine.createParticle(
                    GenesisParticles.MAGIC_MIST.get(),
                    this.getX(), this.getY() + 0.1D, this.getZ(),
                    0.0D, 0.0D, 0.0D
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
