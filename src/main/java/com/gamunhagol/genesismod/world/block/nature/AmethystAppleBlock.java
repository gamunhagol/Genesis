package com.gamunhagol.genesismod.world.block.nature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class AmethystAppleBlock extends Block {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    protected static final VoxelShape FLOOR_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D);
    protected static final VoxelShape HANGING_SHAPE = Block.box(5.0D, 9.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public AmethystAppleBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(HANGING, false));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getClickedFace() == Direction.DOWN) {
            return this.defaultBlockState().setValue(HANGING, true);
        }
        return this.defaultBlockState().setValue(HANGING, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : FLOOR_SHAPE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING);
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < 0.2D) {

            double x = (double)pos.getX() + 0.4D + random.nextDouble() * 0.2D;
            double z = (double)pos.getZ() + 0.4D + random.nextDouble() * 0.2D;

            double y;
            if (state.getValue(HANGING)) {
                y = (double)pos.getY() + 0.7D + random.nextDouble() * 0.2D;
            } else {
                y = (double)pos.getY() + 0.2D + random.nextDouble() * 0.2D;
            }

            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
