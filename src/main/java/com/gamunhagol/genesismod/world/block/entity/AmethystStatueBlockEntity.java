package com.gamunhagol.genesismod.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AmethystStatueBlockEntity extends BlockEntity {

    private int spawnTimer = 100;

    public AmethystStatueBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AmethystStatueBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        pEntity.spawnTimer--;

        if (pEntity.spawnTimer <= 0) {
            ServerLevel serverLevel = (ServerLevel) level;

            long pigCount = serverLevel.getEntitiesOfClass(Pig.class, new AABB(pos).inflate(4.0D)).size();
            if (pigCount < 6) {
                spawnPigLikeSpawner(serverLevel, pos);
            }

            pEntity.spawnTimer = 200;
            pEntity.setChanged();
        }
    }

    private static void spawnPigLikeSpawner(ServerLevel level, BlockPos pos) {
        Pig pig = EntityType.PIG.create(level);
        if (pig == null) return;

        double x = pos.getX() + (level.random.nextDouble() - level.random.nextDouble()) * 4.0D + 0.5D;
        double y = pos.getY() + level.random.nextInt(3) - 1;
        double z = pos.getZ() + (level.random.nextDouble() - level.random.nextDouble()) * 4.0D + 0.5D;

        pig.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);

        if (level.noCollision(pig)) {
            pig.finalizeSpawn(level, level.getCurrentDifficultyAt(pig.blockPosition()), MobSpawnType.SPAWNER, null, null);
            level.addFreshEntity(pig);

            level.playSound(
                    null, pos, SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.BLOCKS,1.0F,1.0F
            );
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("SpawnTimer", this.spawnTimer);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.spawnTimer = pTag.getInt("SpawnTimer");
    }
}