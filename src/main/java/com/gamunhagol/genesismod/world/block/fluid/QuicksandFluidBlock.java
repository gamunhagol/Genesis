package com.gamunhagol.genesismod.world.block.fluid;

import net.minecraft.core.BlockPos;
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
            BlockPos eyePos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            if (level.getBlockState(eyePos).is(this)) {
                int currentAir = living.getAirSupply();


                if (currentAir > -20) { // 산소가 완전히 다 떨어지기 전까지
                    living.setAirSupply(currentAir - 1);
                }
            }
        }
        entity.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.45D, 0.8D, 0.45D));
    }

}
