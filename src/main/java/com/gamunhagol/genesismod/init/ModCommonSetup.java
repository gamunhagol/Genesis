package com.gamunhagol.genesismod.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.Items;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.core.dispenser.DispenseItemBehavior;

public class ModCommonSetup {
    public static void setup() {
        DispenseItemBehavior sandTrapBucketPickup = new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(net.minecraft.core.BlockSource source, ItemStack stack) {
                Level level = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof BucketPickup pickup) {
                    ItemStack result = pickup.pickupBlock(level, pos, state);
                    if (!result.isEmpty()) {
                        level.levelEvent(2001, pos, Block.getId(state));
                        return new ItemStack(Items.BUCKET);
                    }
                }
                return super.execute(source, stack);
            }
        };

        DispenserBlock.registerBehavior(Items.BUCKET, sandTrapBucketPickup);
    }
}
