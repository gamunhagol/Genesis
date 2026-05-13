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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class StatueBaseBlock extends Block implements EntityBlock {
    public StatueBaseBlock(Properties properties) { super(properties); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            // 클라이언트에서 스크린 호출 (자식 BE에서 넘겨주는 ID를 활용)
            if (level.getBlockEntity(pos) instanceof StatueBaseBlockEntity be) {
                openStatueGui(be);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    private void openStatueGui(StatueBaseBlockEntity be) {
        // 여기서 메인 허브 UI(StatueMainScreen)를 띄웁니다.
        // BE에서 넘겨받는 statueId를 통해 자식마다 다른 스팩업 화면을 분기할 수 있습니다.
        Minecraft.getInstance().setScreen(new StatueMainScreen(be.getStatueId()));
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}