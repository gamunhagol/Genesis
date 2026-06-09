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
                double sideOffset = 0.15;
                double height = 2.15;
                double forward = 0.32;

                Direction facing = state.getValue(AEKStatueBlock.FACING);

                for (int i = 0; i < 2; i++) {
                    double currentSide = (i == 0) ? -sideOffset : sideOffset;
                    double offsetX = 0;
                    double offsetZ = 0;

                    switch (facing) {
                        case NORTH -> { offsetX = currentSide; offsetZ = -forward; }
                        case SOUTH -> { offsetX = -currentSide; offsetZ = forward; }
                        case WEST  -> { offsetX = -forward; offsetZ = -currentSide; }
                        case EAST  -> { offsetX = forward; offsetZ = currentSide; }
                        default -> { }
                    }

                    if (level.random.nextFloat() < 0.085f) {
                        level.addParticle(GenesisParticles.GREEN_FLAME.get(),
                                pos.getX() + 0.5 + offsetX,
                                pos.getY() + height,
                                pos.getZ() + 0.5 + offsetZ,
                                0.0, 0.001, 0.0);
                    }
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
