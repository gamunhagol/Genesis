package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SandTrapBlock extends PowderSnowBlock implements BucketPickup {

    public SandTrapBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // super.entityInside(state, level, pos, entity); ❌ 동상 효과 제거
        entity.makeStuckInBlock(state, new Vec3(0.8D, 0.5D, 0.8D));

        if (!level.isClientSide && entity instanceof LivingEntity living) {
            double blockTop = pos.getY() + 1.0D;
            double entityBottom = entity.getBoundingBox().minY;
            double entityHeight = entity.getBbHeight();
            double submerged = blockTop - entityBottom; // 잠긴 깊이

            // 엔티티 높이 대비 60% 이상 잠기면 피해
            if (submerged > entityHeight * 0.6) {
                living.hurt(level.damageSources().inWall(), 1.0F);
            }
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        return new ItemStack(GenesisItems.SAND_TRAP_BUCKET.get());
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.SAND_BREAK);
    }
}

