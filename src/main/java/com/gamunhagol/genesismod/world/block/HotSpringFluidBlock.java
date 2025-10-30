package com.gamunhagol.genesismod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

public class HotSpringFluidBlock extends LiquidBlock {
    public HotSpringFluidBlock(RegistryObject<FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false));
        }
        super.entityInside(state, level, pos, entity);
    }

    // 🔥 온천수 연기 효과 (여기서 작동)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.05F) { // 15% 확률
            level.addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    pos.getX() + random.nextDouble(),
                    pos.getY() + 1.1D,
                    pos.getZ() + random.nextDouble(),
                    0.0D, 0.07D, 0.0D
            );
        }
    }
}
