package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.world.block.entity.AEKStatueBlockEntity;
import com.gamunhagol.genesismod.world.block.entity.GenesisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class AEKStatueBlock extends BaseEntityBlock {
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    public AEKStatueBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LOCKED, true));
    }


    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        // LOCKED가 true(잠금 상태)이면 파괴 진행도를 0으로 만들어 절대 못 부수게 합니다.
        if (pState.getValue(LOCKED)) {
            return 0.0F;
        }
        // 잠금이 풀리면 일반적인 파괴 속도를 따릅니다.
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return GenesisBlockEntities.AEK_STATUE_BE.get().create(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LOCKED);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        // 서버와 클라이언트 모두에서 tick이 돌아가도록 설정 (입자와 스폰 모두 필요하므로)
        return createTickerHelper(pBlockEntityType, GenesisBlockEntities.AEK_STATUE_BE.get(), AEKStatueBlockEntity::tick);
    }

}