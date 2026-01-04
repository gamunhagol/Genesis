package com.gamunhagol.genesismod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

public class QuicksandFluidBlock extends LiquidBlock {
    public QuicksandFluidBlock(RegistryObject<FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (!level.isClientSide && entity instanceof LivingEntity living) {
            // 압사 로직: 눈 위치가 유사 블록인지 확인
            BlockPos eyePos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            if (level.getBlockState(eyePos).is(this)) {
                // 2. 산소 게이지 조작
                int currentAir = living.getAirSupply();

                // 마인크래프트는 기본적으로 초당 15~20 정도의 산소를 회복하거나 소모합니다.
                // 여기서는 매 틱마다 산소를 추가로 1씩 더 깎아서 약 2~3배 빠르게 소모시킵니다.
                if (currentAir > -20) { // 산소가 완전히 다 떨어지기 전까지
                    living.setAirSupply(currentAir - 1);
                }
            }
        }
    }

}
