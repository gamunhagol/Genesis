package com.gamunhagol.genesismod.world.block.custom.statue;

import com.gamunhagol.genesismod.world.block.entity.statue.StatueBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class GodStatueGenericBlock extends StatueBaseBlock {
    private final Supplier<? extends BlockEntityType<? extends StatueBaseBlockEntity>> beSupplier;

    private static final VoxelShape SHAPE_NS = box(1.0, 0.0, 4.0, 15.0, 15.0, 12.0);
    private static final VoxelShape SHAPE_EW = box(4.0, 0.0, 1.0, 12.0, 15.0, 15.0);

    public GodStatueGenericBlock(Properties properties, Supplier<? extends BlockEntityType<? extends StatueBaseBlockEntity>> beSupplier) {
        super(properties);
        this.beSupplier = beSupplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return beSupplier.get().create(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);

        return (direction == Direction.NORTH || direction == Direction.SOUTH) ? SHAPE_NS : SHAPE_EW;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}