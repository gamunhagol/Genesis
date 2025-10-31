package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SandTrapBucketItem extends Item {

    public SandTrapBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.FAIL;

        BlockState state = GenesisBlocks.SAND_TRAP.get().defaultBlockState();

        if (!level.getBlockState(pos).canBeReplaced()) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                player.getInventory().add(new ItemStack(Items.BUCKET));
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
