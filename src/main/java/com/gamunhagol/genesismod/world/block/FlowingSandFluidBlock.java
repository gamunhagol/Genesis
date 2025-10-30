package com.gamunhagol.genesismod.world.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import com.gamunhagol.genesismod.world.fluid.FlowingSandFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FlowingSandFluidBlock extends LiquidBlock {
    public FlowingSandFluidBlock(Supplier<? extends FlowingFluid> fluid, BlockBehaviour.Properties props) {
        super(fluid, props);
    }

    public void initFluidClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            private final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/sand");
            private final ResourceLocation FLOW = ResourceLocation.withDefaultNamespace("block/sand");

            @Override
            public ResourceLocation getStillTexture() { return STILL; }

            @Override
            public ResourceLocation getFlowingTexture() { return FLOW; }

            @Override
            public int getTintColor() { return 0xFFD6C08C; } // 모래색
        });
    }
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living) {


            if (living.isEyeInFluidType(FlowingSandFluidType.INSTANCE)) {
                DamageSources sources = level.damageSources();
                living.hurt(sources.inWall(), 1.0F);
            }

            living.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.5D, 0.3D, 0.5D));
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public boolean canBeReplaced(BlockState state, net.minecraft.world.item.context.BlockPlaceContext context) {
        return true;
    }
}
