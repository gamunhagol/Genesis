package com.gamunhagol.genesismod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LightingCrystalClusterBlock extends AmethystClusterBlock {
    public LightingCrystalClusterBlock(int pSize, int pOffset, Properties pProperties) {
        // 부모 생성자를 호출하며 반드시 .randomTicks()가 포함된 설정을 받아야 합니다.
        super(pSize, pOffset, pProperties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        // 부모 클래스가 false를 반환할 수도 있으므로 true로 강제합니다.
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        // 1. 현재 월드에 번개가 치는 날씨인지 확인
        if (pLevel.isThundering()) {
            // 2. 확률 설정 (0.05F는 약 5% 확률, 바닐라 피뢰침과 유사한 느낌을 주려면 조절 가능)
            if (pRandom.nextFloat() < 0.25F) {
                // 3. 블록 바로 위에 번개 생성
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(pLevel);
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(pPos));
                    // 번개를 소환한 주체가 없으므로 null 전달 (피뢰침 효과와 동일)
                    pLevel.addFreshEntity(lightningbolt);
                }
            }
        }
    }
}
