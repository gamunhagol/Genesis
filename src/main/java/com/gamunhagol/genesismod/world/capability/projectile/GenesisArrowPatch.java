package com.gamunhagol.genesismod.world.capability.projectile;

import net.minecraft.world.entity.projectile.AbstractArrow;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisArrowPatch extends ArrowPatch {

    /**
     * 현재 화살의 속도를 계산하여 최종 임팩트(충격량)를 반환합니다.
     * LargeArrowEntity에서 광역 피해 계산 시 호출합니다.
     */
    public float getImpact() {
        AbstractArrow arrow = (AbstractArrow) this.original;
        double currentVelocity = arrow.getDeltaMovement().length();
        float chargeRatio = Math.min((float)(currentVelocity / 5.6F), 1.0F);

        return this.impact * chargeRatio;
    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        float finalImpact = this.getImpact();

        EpicFightDamageSource source = super.createEpicFightDamageSource();
        source.setBaseImpact(finalImpact);

        AbstractArrow arrow = (AbstractArrow) this.original;
        float chargeRatio = Math.min((float)(arrow.getDeltaMovement().length() / 5.6F), 1.0F);

        if (chargeRatio >= 0.9F) {
            source.setStunType(StunType.KNOCKDOWN);
        } else if (chargeRatio >= 0.5F) {
            source.setStunType(StunType.LONG);
        } else {
            source.setStunType(StunType.SHORT);
        }

        return source;
    }
}