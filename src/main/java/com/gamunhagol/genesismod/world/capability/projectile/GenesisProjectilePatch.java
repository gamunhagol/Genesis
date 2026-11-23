package com.gamunhagol.genesismod.world.capability.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisProjectilePatch<T extends MagicEntity> extends ProjectilePatch<T> {
    protected StunType stunType;

    public GenesisProjectilePatch() {
        this(1.0F, 0.0F, StunType.SHORT);
    }

    public GenesisProjectilePatch(float impact, float armorNegation, StunType stunType) {
        this.impact = impact;
        this.armorNegation = armorNegation;
        this.stunType = stunType;
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