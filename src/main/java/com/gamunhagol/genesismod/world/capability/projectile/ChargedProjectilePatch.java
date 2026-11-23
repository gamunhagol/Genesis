package com.gamunhagol.genesismod.world.capability.projectile;

import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicEntity;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class ChargedProjectilePatch<T extends MagicEntity> extends GenesisProjectilePatch<T> {

    public ChargedProjectilePatch(float baseImpact, float armorNegation, StunType stunType) {
        super(baseImpact, armorNegation, stunType);
    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        EpicFightDamageSource source = super.createEpicFightDamageSource();

        float ratio = this.original.getChargeRatio();
        float impactMultiplier = 1.0F;

        if (ratio >= 1.0F) {
            impactMultiplier = 2.0F;
        } else if (ratio >= 0.75F) {
            impactMultiplier = 1.5F;
        } else if (ratio > 0.5F) {
            impactMultiplier = 1.25F;
        }

        source.setBaseImpact(this.impact * impactMultiplier);
        return source;
    }
}