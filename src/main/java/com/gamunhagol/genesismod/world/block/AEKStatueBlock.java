package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.world.block.entity.AEKStatueBlockEntity;
import com.gamunhagol.genesismod.world.block.entity.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AEKStatueBlock extends BaseEntityBlock {
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
    // [추가] 회전을 위한 방향 속성 (동서남북)
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // [추가] 히트박스: 높이 32 (2칸)으로 설정. 모델 크기에 맞춰 조절하세요.
    // 예: box(0, 0, 0, 16, 32, 16) -> 가로세로 꽉 채운 2칸 높이
    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 40, 16);

    public AEKStatueBlock(Properties pProperties) {
        super(pProperties);
        // 기본 상태: 잠김(True), 북쪽(NORTH) 바라봄
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LOCKED, true)
                .setValue(FACING, Direction.NORTH));
    }


    // [핵심 변경] MODEL -> ENTITYBLOCK_ANIMATED
    // 이제 JSON 모델 파일은 무시되고, AEKStatueRenderer(Java)가 그림을 그립니다.
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    // [추가] 설치 시 플레이어를 바라보게 설정
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // [추가] 히트박스 적용
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // [추가] 속성 등록 (FACING 추가됨)
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LOCKED, FACING);
    }

    // [추가] 회전 및 대칭 기능 (구조물 블럭 호환용)
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    // 블럭 내부나 아래가 어두워지지 않게 밝기 보정
    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    // 빛이 아래로 통과하도록 설정
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (pState.getValue(LOCKED)) {
            return 0.0F; // 잠겨있으면 파괴 불가
        }
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return GenesisBlockEntities.AEK_STATUE_BE.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, GenesisBlockEntities.AEK_STATUE_BE.get(), AEKStatueBlockEntity::tick);
    }
}