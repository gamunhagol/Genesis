package com.gamunhagol.genesismod.world.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

import java.util.Optional;

public class AmethystSaplingBlock extends Block {
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    public AmethystSaplingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);

        // 자수정 조각으로 상호작용
        if (itemstack.is(Items.AMETHYST_SHARD)) {
            // [클라이언트] 초록색 입자 효과 (뼛가루 느낌)
            if (level.isClientSide) {
                for(int i = 0; i < 15; ++i) {
                    double d0 = level.random.nextGaussian() * 0.02D;
                    double d1 = level.random.nextGaussian() * 0.02D;
                    double d2 = level.random.nextGaussian() * 0.02D;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                            pos.getX() + 0.5D + (level.random.nextFloat() * 0.6D - 0.3D),
                            pos.getY() + 0.5D + (level.random.nextFloat() * 0.6D),
                            pos.getZ() + 0.5D + (level.random.nextFloat() * 0.6D - 0.3D),
                            d0, d1, d2);
                }
            }

            // [서버] 성장 로직 및 소리 재생
            if (level instanceof ServerLevel serverLevel) {
                // 45% 확률로 성장 성공
                if (serverLevel.random.nextFloat() < 0.45D) {
                    this.advanceTree(serverLevel, pos, state, serverLevel.random);
                }
                // 자수정 차임 소리 재생
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F + level.random.nextFloat() * 1.2F);
            }

            // 크리에이티브가 아니면 아이템 소모
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(7) == 0) {
            this.advanceTree(level, pos, state, random);
        }
    }

    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            if (ForgeEventFactory.blockGrowFeature(level, random, pos, null).getResult().equals(Event.Result.DENY)) {
                return;
            }
            this.performSpawning(level, pos, state, random);
        }
    }

    // 핵심 수정 부분: 구조물 소환 및 위치 보정
    public void performSpawning(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        StructureTemplateManager manager = level.getStructureManager();

        int treeNum = random.nextInt(5) + 1;
        ResourceLocation location = new ResourceLocation("genesis", "amethyst_tree_" + treeNum);
        Optional<StructureTemplate> template = manager.get(location);

        template.ifPresent(t -> {
            StructurePlaceSettings settings = new StructurePlaceSettings()
                    .setRotation(Rotation.getRandom(random)) // 무작위 회전
                    .setMirror(Mirror.NONE)
                    .addProcessor(new RuleProcessor(ImmutableList.of(
                            new ProcessorRule(
                                    new RandomBlockMatchTest(Blocks.AMETHYST_BLOCK, 0.2f),
                                    AlwaysTrueTest.INSTANCE,
                                    Blocks.BUDDING_AMETHYST.defaultBlockState()
                            )
                    )));

            // [수정됨] 회전을 고려한 중심점 좌표 계산
            Vec3i size = t.getSize();
            // 구조물의 바닥 정중앙을 기준점(Pivot)으로 설정
            BlockPos pivotPos = new BlockPos(size.getX() / 2, 0, size.getZ() / 2);

            // 회전된 상태에서의 상대적 위치를 계산하여 묘목 위치(pos)에서 뺌
            BlockPos originPos = pos.subtract(StructureTemplate.calculateRelativePosition(settings, pivotPos));

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);

            // 구조물 배치 시도
            if (!t.placeInWorld(level, originPos, originPos, settings, random, 3)) {
                level.setBlock(pos, state, 4); // 실패 시 복구
            }
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}