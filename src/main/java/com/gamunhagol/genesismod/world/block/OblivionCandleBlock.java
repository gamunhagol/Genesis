package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.init.GenesisParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OblivionCandleBlock extends CandleBlock {

    public OblivionCandleBlock(Properties properties) {
        super(properties);
        // 기본 상태: 초 1개, 불 켜짐(LIT=true), 물에 안잠김
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CANDLES, 1)
                .setValue(LIT, true)
                .setValue(WATERLOGGED, false));
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.getItem() == this.asItem() && state.getValue(CANDLES) < 4) {
            return super.use(state, level, pos, player, hand, hit);
        }

        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            return blockstate.cycle(CANDLES);
        }
        return super.getStateForPlacement(context).setValue(LIT, true);
    }

    /**
     * 바닐라 양초는 물에 잠기면 불이 꺼지지만, 이 메서드를 오버라이드하여
     * 물 속에서도 LIT=true를 유지하게 할 수 있습니다.
     */
    @Override
    protected boolean canBeLit(BlockState state) {
        return false; // 이미 켜져 있으므로 추가로 켤 수 없음 (끄는 것도 방지)
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;


        List<Vec3> offsets = getParticleOffsets(state);
        for (Vec3 vec3 : offsets) {
            double x = (double)pos.getX() + vec3.x;
            double y = (double)pos.getY() + vec3.y;
            double z = (double)pos.getZ() + vec3.z;

            level.addParticle(GenesisParticles.GREEN_FLAME.get(), x, y, z, 0.0D, 0.001D, 0.0D);

            if (random.nextFloat() < 0.15F) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }

        if (random.nextFloat() < 0.2F) {
            level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D,
                    SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    protected List<Vec3> getParticleOffsets(BlockState state) {
        int candles = state.getValue(CANDLES);
        return switch (candles) {
            case 1 -> List.of(new Vec3(0.5D, 0.5D, 0.5D));
            case 2 -> List.of(
                    new Vec3(0.375D, 0.44D, 0.5D),
                    new Vec3(0.625D, 0.5D, 0.44D)
            );
            case 3 -> List.of(
                    new Vec3(0.5D, 0.313D, 0.625D),
                    new Vec3(0.375D, 0.44D, 0.5D),
                    new Vec3(0.56D, 0.5D, 0.44D)
            );
            case 4 -> List.of(
                    new Vec3(0.44D, 0.313D, 0.56D),
                    new Vec3(0.625D, 0.44D, 0.56D),
                    new Vec3(0.375D, 0.44D, 0.375D),
                    new Vec3(0.56D, 0.5D, 0.375D)
            );
            default -> List.of(new Vec3(0.5D, 0.5D, 0.5D));
        };
    }
}
