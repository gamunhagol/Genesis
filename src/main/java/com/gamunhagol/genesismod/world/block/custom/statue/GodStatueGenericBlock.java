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
    // 이 블록과 짝을 이룰 블록 엔티티 타입
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
        // 등록할 때 넘겨받은 BE를 자동으로 생성해 줍니다.
        return beSupplier.get().create(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // 블록의 방향(FACING)을 읽어서 알맞은 모양을 반환
        // 보통 StatueBaseBlock에 FACING 속성이 포함되어 있을 겁니다.
        Direction direction = state.getValue(FACING);

        return (direction == Direction.NORTH || direction == Direction.SOUTH) ? SHAPE_NS : SHAPE_EW;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // 커스텀 모델(BER) 렌더링을 위한 설정
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}