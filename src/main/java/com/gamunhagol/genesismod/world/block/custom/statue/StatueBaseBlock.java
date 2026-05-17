package com.gamunhagol.genesismod.world.block.custom.statue;

import com.gamunhagol.genesismod.client.gui.StatueMainScreen;
import com.gamunhagol.genesismod.world.block.entity.statue.StatueBaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class StatueBaseBlock extends Block implements EntityBlock {
    // 방향 속성 정의 (수평 방향: 북, 남, 동, 서)
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StatueBaseBlock(Properties properties) {
        super(properties);
        // 기본 상태 설정
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    // 블록을 설치할 때 플레이어를 바라보도록 설정
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        //if (level.isClientSide) {
       //     BlockEntity be = level.getBlockEntity(pos);
       //     if (be instanceof StatueBaseBlockEntity statueBe) {
      //          this.openStatueGui(statueBe);
      //      }
       // }
        //2026.5.15 기준 아직 ui 가 없어서 주석처리 해놓음
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    private void openStatueGui(StatueBaseBlockEntity be) {
        Minecraft.getInstance().setScreen(new StatueMainScreen(be.getStatueId()));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}