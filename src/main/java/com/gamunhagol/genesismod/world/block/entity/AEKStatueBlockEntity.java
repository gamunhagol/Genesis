package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.world.block.AEKStatueBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AEKStatueBlockEntity extends BlockEntity implements IFadedDungeonElement {
    private int spawnTick = 0;

    public AEKStatueBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GenesisBlockEntities.AEK_STATUE_BE.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AEKStatueBlockEntity be) {
        if (!state.getValue(AEKStatueBlock.LOCKED)) {
            if (level.isClientSide) {
                // 확률적으로 파티클 생성 (0.4f = 40% 확률)
                if (level.random.nextFloat() < 0.2f) {
                    double offsetX = (level.random.nextFloat() - 0.5) * 0.1;
                    double offsetZ = (level.random.nextFloat() - 0.5) * 0.1;

                    level.addParticle(GenesisParticles.GREEN_FLAME.get(),
                            pos.getX() + 0.5 + offsetX,
                            pos.getY() + 1.2,
                            pos.getZ() + 0.5 + offsetZ,
                            0.0, 0.02, 0.0); // Y축 속도도 0.03에서 0.02로 약간 낮춤
                }
            }

            // 서버 사이드 소환 로직
            be.spawnTick++;
            if (be.spawnTick >= 700) {
                if (!level.isClientSide) {
                    spawnKnight(level, pos);
                }
                be.spawnTick = 0;
            }
        }
    }

    private static void spawnKnight(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            int spawnCount = 4;
            int range = 4;

            // 소환 시 블레이즈 화염구 발사 소리 재생
            level.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < spawnCount; i++) {
                double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * range;
                double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * range;
                double y = pos.getY() + level.random.nextInt(3) - 1;

                BlockPos spawnPos = new BlockPos((int)x, (int)y, (int)z);

                // 유령이라도 일단 공기 중에 소환되도록 체크 (벽 내부 생성 방지)
                if (level.getBlockState(spawnPos).isAir()) {
                    Pig pig = EntityType.PIG.create(level);
                    if (pig != null) {
                        pig.moveTo(x, y, z, level.random.nextFloat() * 360F, 0);
                        level.addFreshEntity(pig);

                    }
                }
            }
        }
    }

    @Override
    public void activateElement() {
        if (this.level != null) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(AEKStatueBlock.LOCKED, false), 3);
        }
    }
}
