package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.events.GenesisDungeonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;


public class FadedGatewayBlock extends Block {
    // LIGHT_LEVEL은 이제 '빛의 강도'가 아니라 '애니메이션 상태 제어'용으로만 사용됩니다.
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 15);

    public FadedGatewayBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHT_LEVEL, 5));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        // 대기 상태(5)일 때 엔더의 눈으로 활성화
        if (itemstack.is(Items.ENDER_EYE) && state.getValue(LIGHT_LEVEL) == 5) {
            if (!level.isClientSide) {
                // 활성화 상태(15)로 변경하여 애니메이션 모델 출력 시작
                level.setBlock(pos, state.setValue(LIGHT_LEVEL, 15), 3);

                // 애니메이션 루프 시간에 맞춰 틱 예약 (8틱 * 16프레임 = 128틱)
                level.scheduleTick(pos, this, 128);

                if (!player.getAbilities().instabuild) itemstack.shrink(1);
                level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // 예약된 128틱이 지나면 여기가 실행됩니다.
        if (state.getValue(LIGHT_LEVEL) == 15) {
            triggerDungeonActivation(level, pos);
        }
    }

    private void triggerDungeonActivation(ServerLevel level, BlockPos centerPos) {
        // 입구 파괴 로직
        int radius = 2;
        BlockPos.betweenClosed(centerPos.offset(-radius, -radius, -radius), centerPos.offset(radius, radius, radius)).forEach(targetPos -> {
            if (level.getBlockState(targetPos).is(GenesisBlocks.FADED_STONE.get())) {
                level.destroyBlock(targetPos, false);
            }
        });
        Block.popResource(level, centerPos, new ItemStack(Items.ENDER_EYE));
        // 신호 발송 및 블록 제거
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new GenesisDungeonEvent.Activate(level, centerPos));
        level.removeBlock(centerPos, false);
        level.playSound(null, centerPos, SoundEvents.POWDER_SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
    }
}
