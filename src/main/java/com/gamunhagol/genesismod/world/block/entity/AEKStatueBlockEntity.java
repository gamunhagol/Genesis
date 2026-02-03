package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.world.block.AEKStatueBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AEKStatueBlockEntity extends BlockEntity implements IFadedDungeonElement {
    private int spawnTick = 0;

    public AEKStatueBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GenesisBlockEntities.AEK_STATUE_BE.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AEKStatueBlockEntity be) {
        // 잠금이 풀려있을 때만 작동
        if (!state.getValue(AEKStatueBlock.LOCKED)) {

            // 1. 클라이언트: 눈에서 불꽃 파티클 생성
            if (level.isClientSide) {
                // [설정] 눈 위치 상세 조정
                double sideOffset = 0.15; // 눈 사이 간격
                double height = 2.15;     // 눈 높이
                double forward = 0.32;    // 얼굴 튀어나옴 정도

                // 블럭이 바라보는 방향 가져오기
                Direction facing = state.getValue(AEKStatueBlock.FACING);

                for (int i = 0; i < 2; i++) {
                    double currentSide = (i == 0) ? -sideOffset : sideOffset;
                    double offsetX = 0;
                    double offsetZ = 0;

                    // 방향에 따라 좌표 회전 (Switch 문)
                    switch (facing) {
                        case NORTH -> { offsetX = currentSide; offsetZ = -forward; }
                        case SOUTH -> { offsetX = -currentSide; offsetZ = forward; }
                        case WEST  -> { offsetX = -forward; offsetZ = -currentSide; }
                        case EAST  -> { offsetX = forward; offsetZ = currentSide; }
                        default -> { } // [중요] 예외 방지용 기본값
                    }

                    if (level.random.nextFloat() < 0.085f) {
                        level.addParticle(GenesisParticles.GREEN_FLAME.get(),
                                pos.getX() + 0.5 + offsetX,
                                pos.getY() + height,
                                pos.getZ() + 0.5 + offsetZ,
                                0.0, 0.001, 0.0); // 위로 살짝 올라가는 횃불 느낌
                    }
                }
            }
            // 2. 서버: 몹 소환 로직
            else {
                be.spawnTick++;
                if (be.spawnTick >= 700) {
                    spawnKnight(level, pos);
                    be.spawnTick = 0;
                }
            }
        }
    }

    private static void spawnKnight(Level level, BlockPos pos) {
        // 서버인지 한 번 더 확인 (안전장치)
        if (!level.isClientSide) {
            int spawnCount = 4;
            int range = 4;

            level.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < spawnCount; i++) {
                double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * range;
                double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * range;
                double y = pos.getY() + level.random.nextInt(3) - 1;

                BlockPos spawnPos = new BlockPos((int)x, (int)y, (int)z);

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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("SpawnTick", this.spawnTick);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.spawnTick = pTag.getInt("SpawnTick");
    }

    @Override
    public void activateElement() {
        if (this.level != null) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(AEKStatueBlock.LOCKED, false), 3);
        }
    }
}
