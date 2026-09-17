package com.gamunhagol.genesismod.world.entity.projectile.magic.child;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.magic.AbstractMagicDomainEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
    protected ParticleOptions getFloorParticle() {
        return ParticleTypes.PORTAL;
    }

    @Override
    public float getMagicDamageMultiplier() {
        return 1.30F;
    }
}