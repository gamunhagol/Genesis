package com.gamunhagol.genesismod.world.block.nature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LightingCrystalClusterBlock extends AmethystClusterBlock {
    public LightingCrystalClusterBlock(int pSize, int pOffset, Properties pProperties) {
        super(pSize, pOffset, pProperties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.isThundering()) {
            if (pRandom.nextFloat() < 0.25F) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(pLevel);
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(pPos));
                    pLevel.addFreshEntity(lightningbolt);
                }
            }
        }
    }
}
