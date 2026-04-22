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
        // 1. 차징 비율 계산 (최대 속도 5.6F 기준)
        double currentVelocity = arrow.getDeltaMovement().length();
        float chargeRatio = Math.min((float)(currentVelocity / 5.6F), 1.0F);

        // 부모 클래스(ArrowPatch)에 있는 impact 필드를 사용하여 계산
        return this.impact * chargeRatio;
    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        // 위에서 만든 메서드를 사용하여 임팩트 가져오기
        float finalImpact = this.getImpact();

        // 에픽 파이트 기본 대미지 소스 생성
        EpicFightDamageSource source = super.createEpicFightDamageSource();
        source.setBaseImpact(finalImpact);

        // 경직 단계 결정 (getImpact 내부의 로직을 재사용하거나 비율로 직접 계산)
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