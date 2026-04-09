package com.gamunhagol.genesismod.world.capability.projectile;


import net.minecraft.world.entity.projectile.AbstractArrow;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisArrowPatch extends ArrowPatch {


    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        AbstractArrow arrow = (AbstractArrow) this.original;

        // 1. 차징 비율 계산 (최대 속도 5.6F 기준)
        double currentVelocity = arrow.getDeltaMovement().length();
        float chargeRatio = Math.min((float)(currentVelocity / 5.6F), 1.0F);

        // 2. 에픽 파이트 대미지 소스 생성
        EpicFightDamageSource source = super.createEpicFightDamageSource();

        // 3. 차징 비율에 따른 가변 임팩트(충격량) 설정
        // 등록한 기본 임팩트(6.0)에 비율을 곱합니다.
        source.setBaseImpact(this.impact * chargeRatio);

        // 4. 차징 수치에 따른 경직 단계(StunType) 결정
        if (chargeRatio >= 0.9F) {
            // 풀차징(90% 이상): 자빠짐
            source.setStunType(StunType.KNOCKDOWN);
        } else if (chargeRatio >= 0.5F) {
            // 반차징 이상: 긴 경직
            source.setStunType(StunType.LONG);
        } else {
            // 그 미만: 일반 경직
            source.setStunType(StunType.SHORT);
        }

        return source;
    }
}