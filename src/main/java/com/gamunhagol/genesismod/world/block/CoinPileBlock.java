package com.gamunhagol.genesismod.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class CoinPileBlock extends SnowLayerBlock implements SimpleWaterloggedBlock {
    // 물에 잠김 여부를 저장할 프로퍼티 가져오기
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public CoinPileBlock(BlockBehaviour.Properties properties) {
        super(properties);
        // 기본 상태 등록: 기존 눈 레이어 속성 + 물에 안 잠긴 상태(false)
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LAYERS, 1)
                .setValue(WATERLOGGED, false));
    }

    // [중요] 눈이 녹는 로직 제거
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    // 1. 블록 상태 정의에 WATERLOGGED 추가
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    // 2. 설치될 때 물 속에 설치했는지 확인
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 부모(SnowLayerBlock)의 설치 로직(레이어 합치기 등)을 먼저 가져옴
        BlockState state = super.getStateForPlacement(context);

        if (state != null) {
            // 현재 위치에 물이 있는지 확인
            FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
            // 있다면 WATERLOGGED를 true로 설정
            return state.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }
        return null;
    }

    // 3. 주변 블록이 변할 때 물 흐름 처리
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        // 물에 잠긴 상태라면 물 스케줄을 업데이트 (물이 흐르도록)
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    // 4. 현재 블록의 유체 상태 반환 (렌더링용)
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

}
