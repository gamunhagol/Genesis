package com.gamunhagol.genesismod.world.block.custom;

import com.gamunhagol.genesismod.events.world.GenesisDungeonEvent;
import com.gamunhagol.genesismod.world.block.GenesisBlocks;
import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;


public class FadedGatewayBlock extends Block {
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    public FadedGatewayBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHT_LEVEL, 5));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(GenesisItems.EYE_OF_THE_EARTH.get()) && state.getValue(LIGHT_LEVEL) == 5) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LIGHT_LEVEL, 15), 3);

                level.scheduleTick(pos, this, 128);

                if (!player.getAbilities().instabuild) itemstack.shrink(1);
                level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIGHT_LEVEL) == 15) {
            triggerDungeonActivation(level, pos);
        }
    }

    private void triggerDungeonActivation(ServerLevel level, BlockPos centerPos) {
        int radius = 2;
        BlockPos.betweenClosed(centerPos.offset(-radius, -radius, -radius), centerPos.offset(radius, radius, radius)).forEach(targetPos -> {
            if (level.getBlockState(targetPos).is(GenesisBlocks.FADED_STONE.get())) {
                level.destroyBlock(targetPos, false);
            }
        });
        Block.popResource(level, centerPos, new ItemStack(GenesisItems.EYE_OF_THE_EARTH.get()));
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new GenesisDungeonEvent.Activate(level, centerPos));
        level.removeBlock(centerPos, false);
        level.playSound(null, centerPos, SoundEvents.POWDER_SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
    }
}
