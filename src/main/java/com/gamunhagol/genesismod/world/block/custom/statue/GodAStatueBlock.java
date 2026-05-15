package com.gamunhagol.genesismod.world.block.custom.statue;

import com.gamunhagol.genesismod.world.block.entity.statue.GodAStatueBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GodAStatueBlock extends StatueBaseBlock {

    public GodAStatueBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GodAStatueBlockEntity(pos, state);
    }

    /*
     * 💡 중요 팁:
     * 부모 클래스인 StatueBaseBlock에서 getRenderShape를 RenderShape.MODEL로 설정하셨습니다.
     * JSON 모델 대신 Java 모델(StatueOfGodA)을 커스텀 렌더러(BER)로 띄우시려면,
     * 아래처럼 ENTITYBLOCK_ANIMATED로 오버라이드 해야 기본 블록 텍스처(보라색/검은색 큐브)가 겹쳐 보이지 않습니다.
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}