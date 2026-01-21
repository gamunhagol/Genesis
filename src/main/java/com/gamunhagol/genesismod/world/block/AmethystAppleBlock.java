package com.gamunhagol.genesismod.world.block;

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

    // 설치 시 클릭한 위치를 기준으로 상태 결정
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 클릭한 면이 아래쪽(천장)이면 hanging을 true로 설정
        if (context.getClickedFace() == Direction.DOWN) {
            return this.defaultBlockState().setValue(HANGING, true);
        }
        // 그 외(바닥이나 벽면 클릭 시)에는 바닥 모드
        return this.defaultBlockState().setValue(HANGING, false);
    }

    // 상태에 따라 히트박스(선택 영역) 변경
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : FLOOR_SHAPE;
    }

    // 블록의 상태 속성을 시스템에 등록
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING);
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // 1. 파티클 생성 확률 (0.2D ~ 0.3D 정도가 적당히 은은합니다)
        if (random.nextDouble() < 0.2D) {

            // 2. 사과의 위치 계산 (중앙 0.5를 기준으로 약간의 오차 부여)
            double x = (double)pos.getX() + 0.4D + random.nextDouble() * 0.2D;
            double z = (double)pos.getZ() + 0.4D + random.nextDouble() * 0.2D;

            // 3. HANGING 상태에 따른 높이(Y) 설정
            double y;
            if (state.getValue(HANGING)) {
                // 천장에 매달려 있을 때 (모델 위쪽 중심부)
                y = (double)pos.getY() + 0.7D + random.nextDouble() * 0.2D;
            } else {
                // 바닥에 놓여 있을 때 (모델 아래쪽 중심부)
                y = (double)pos.getY() + 0.2D + random.nextDouble() * 0.2D;
            }

            // 4. 실제 자수정 반짝임 파티클 생성
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
