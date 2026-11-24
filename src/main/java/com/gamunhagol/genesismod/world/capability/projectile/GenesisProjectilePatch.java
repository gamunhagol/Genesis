package com.gamunhagol.genesismod.world.capability.projectile;

import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisProjectilePatch<T extends MagicEntity> extends ProjectilePatch<T> implements ITrailProjectilePatch {
    protected StunType stunType;
    @Nullable
    protected TrailInfo trailInfo = null;

    public GenesisProjectilePatch() {
        this(1.0F, 0.0F, StunType.SHORT);
    }

    public GenesisProjectilePatch(float impact, float armorNegation, StunType stunType) {
        this.impact = impact;
        this.armorNegation = armorNegation;
        this.stunType = stunType;
    }

    public GenesisProjectilePatch<T> setTrail(TrailInfo trailInfo) {
        this.trailInfo = trailInfo;
        return this;
    }

    @Nullable
    @Override
    public TrailInfo getTrailInfo() {
        return this.trailInfo;
    }

    @Override
    public void onAddedToWorld() {
        if (this.trailInfo != null && this.original.level().isClientSide()) {
            double entityId = Double.longBitsToDouble((long) this.original.getId());
            this.original.level().addParticle(
                    GenesisParticles.PROJECTILE_TRAIL.get(), entityId, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D
            );
        }
    }

    @Override
    protected void setMaxStrikes(T projectileEntity, int maxStrikes) {
    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        Entity owner = this.original.getOwner();

        EpicFightDamageSource source = new EpicFightDamageSource(
                this.original.damageSources().indirectMagic(this.original, owner)
        );

        source.setBaseImpact(this.impact);
        source.setBaseArmorNegation(this.armorNegation);
        source.setStunType(this.stunType);
        return source;
    }
}