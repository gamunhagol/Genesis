package com.gamunhagol.genesismod.world.block.custom.statue;

import com.gamunhagol.genesismod.world.block.entity.statue.StatueBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class GodStatueGenericBlock extends StatueBaseBlock {
    // 이 블록과 짝을 이룰 블록 엔티티 타입
    private final Supplier<? extends BlockEntityType<? extends StatueBaseBlockEntity>> beSupplier;

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
    public RenderShape getRenderShape(BlockState state) {
        // 커스텀 모델(BER) 렌더링을 위한 설정
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}