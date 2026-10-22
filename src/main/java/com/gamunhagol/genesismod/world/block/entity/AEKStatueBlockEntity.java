package com.gamunhagol.genesismod.world.block.entity;

import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.util.IFadedDungeonElement;
import com.gamunhagol.genesismod.world.block.GenesisBlockEntities;
import com.gamunhagol.genesismod.world.block.custom.AEKStatueBlock;
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
        if (!state.getValue(AEKStatueBlock.LOCKED)) {

            if (level.isClientSide) {
                double height = 1.36D;
                double forward = 0.4375D;

                Direction facing = state.getValue(AEKStatueBlock.FACING);
                double offsetX = 0.0D;
                double offsetZ = 0.0D;

                switch (facing) {
                    case NORTH -> offsetZ = -forward;
                    case SOUTH -> offsetZ = forward;
                    case WEST  -> offsetX = -forward;
                    case EAST  -> offsetX = forward;
                    default -> { }
                }

                if (level.random.nextFloat() < 0.025F) {
                    level.addParticle(GenesisParticles.GREEN_FLAME.get(),
                            pos.getX() + 0.5D + offsetX,
                            pos.getY() + height,
                            pos.getZ() + 0.5D + offsetZ,
                            0.0D, 0.003D, 0.0D);
                }
            }
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
