package com.gamunhagol.genesismod.world.block;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmethystApplePuddingBlock extends Block {
    public static final IntegerProperty PORTIONS = IntegerProperty.create("portions", 0, 4);
    // 방향 속성 추가
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);

    // 생성자는 이거 하나만 남겨두고 기존의 것은 삭제하세요.
    public AmethystApplePuddingBlock(Properties pProperties) {
        super(pProperties);
        // 기본 상태에 portions와 facing을 모두 초기화합니다.
        this.registerDefaultState(this.defaultBlockState()
                .setValue(PORTIONS, 0)
                .setValue(FACING, Direction.NORTH));
    }

    // 설치 시 플레이어가 바라보는 방향의 반대(앞면)를 향하게 설정합니다.
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);
        int portions = state.getValue(PORTIONS);

        if (portions == 4) {
            if (!level.isClientSide) {
                Block.popResource(level, pos, new ItemStack(Items.BOWL));
                level.removeBlock(pos, false);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (itemstack.is(Items.BOWL)) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                ItemStack slices = new ItemStack(GenesisItems.AMETHYST_APPLE_PUDDING.get());
                if (!player.getInventory().add(slices)) {
                    player.drop(slices, false);
                }

                level.setBlock(pos, state.setValue(PORTIONS, portions + 1), 3);
                level.playSound(null, pos, SoundEvents.SLIME_BLOCK_STEP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            if (level.isClientSide && hand == InteractionHand.MAIN_HAND) {
                player.displayClientMessage(Component.translatable("tooltip.genesis.pudding_need_bowl"), true);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PORTIONS, FACING);
    }

    // 엔티티가 블록 위에 떨어졌을 때의 동작
    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        // 침대처럼 낙하 데미지를 50% 감소시킵니다.
        super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance * 0.5F);
    }

    // 낙하 후 엔티티를 위로 튕겨내는 로직
    @Override
    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            // 플레이어가 웅크리고(Shift) 있으면 튀어오르지 않습니다.
            super.updateEntityAfterFallOn(pLevel, pEntity);
        } else {
            this.bounceEntity(pEntity);
        }
    }

    // 실제 반동(Bounce)을 주는 핵심 메서드
    private void bounceEntity(Entity pEntity) {
        Vec3 vec3 = pEntity.getDeltaMovement();
        if (vec3.y < 0.0D) {
            // 생명체인 경우 더 탄력 있게 튕겨나갑니다.
            double bounceFactor = pEntity instanceof LivingEntity ? 1.0D : 0.8D;
            // 하강 속도를 반전시켜 위로 튕겨냅니다 (0.66은 침대와 동일한 반동 계수입니다).
            pEntity.setDeltaMovement(vec3.x, -vec3.y * 0.55D * bounceFactor, vec3.z);
        }
    }
}
