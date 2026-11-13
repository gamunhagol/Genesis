package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractMagicDomainEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SeaOfStarsEntity extends AbstractMagicDomainEntity {
    public SeaOfStarsEntity(EntityType<? extends SeaOfStarsEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SeaOfStarsEntity(Level level, LivingEntity owner, DamageSnapshot snapshot) {
        super(GenesisEntities.SEA_OF_STARS.get(), level, owner, snapshot);
    }

    @Override
    protected void applyEntityBuff(LivingEntity entity) {
    }

    @Override
    protected void spawnFloorParticles() {
        if (this.random.nextFloat() < 0.05F) {
            double angle = this.random.nextDouble() * Math.PI * 2;
            double radius = this.getDomainRadius() * (0.85D + this.random.nextDouble() * 0.15D);
            double px = this.getX() + Math.cos(angle) * radius;
            double pz = this.getZ() + Math.sin(angle) * radius;

            this.level().addParticle(
                    GenesisParticles.MAGIC_MIST.get(),
                    px, this.getY() + 0.05D, pz,
                    0.0D, 0.01D, 0.0D
            );
        }
    }

    @Override
    public float getMagicDamageMultiplier() {
        return 1.30F;
    }
}
