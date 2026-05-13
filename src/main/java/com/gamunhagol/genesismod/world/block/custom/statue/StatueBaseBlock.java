package com.gamunhagol.genesismod.world.block.custom.statue;

import com.gamunhagol.genesismod.client.gui.StatueMainScreen;
import com.gamunhagol.genesismod.world.block.entity.statue.StatueBaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class StatueBaseBlock extends Block implements EntityBlock {
    public StatueBaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // 클라이언트 사이드에서만 UI를 호출합니다.
        if (level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StatueBaseBlockEntity statueBe) {
                this.openStatueGui(statueBe);
            }
        }
        // 서버와 클라이언트 모두에 성공 신호를 보냅니다.
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * @OnlyIn(Dist.CLIENT)가 붙은 메서드는 물리적 서버에서 호출되지 않도록 분리해야 합니다.
     * Minecraft.getInstance()는 클라이언트 전용 클래스이기 때문입니다.
     */
    @OnlyIn(Dist.CLIENT)
    private void openStatueGui(StatueBaseBlockEntity be) {
        // StatueMainScreen을 띄우고, BE의 ID를 넘깁니다.
        Minecraft.getInstance().setScreen(new StatueMainScreen(be.getStatueId()));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}