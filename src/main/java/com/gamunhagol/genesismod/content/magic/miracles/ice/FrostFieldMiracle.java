package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.Map;

public class FrostFieldMiracle extends MiracleSpell {

    private static final int MAX_RADIUS = 3;
    private static final double INNER_RADIUS = 1.5D;
    private static final int VERTICAL_SEARCH_RANGE = 6;

    public FrostFieldMiracle() {
        super("frost_field");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 14);
    }

    @Override
    public float getMentalCost() {
        return 4.5f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        BlockPos centerPos = caster.blockPosition();

        for (int dx = -MAX_RADIUS; dx <= MAX_RADIUS; dx++) {
            for (int dz = -MAX_RADIUS; dz <= MAX_RADIUS; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > MAX_RADIUS) continue;

                if (dist > INNER_RADIUS) {
                    double chance = 1.0D - ((dist - INNER_RADIUS) / (MAX_RADIUS - INNER_RADIUS));
                    if (level.random.nextDouble() > chance) {
                        continue;
                    }
                }

                int targetX = centerPos.getX() + dx;
                int targetZ = centerPos.getZ() + dz;

                freezeSurface(level, centerPos.getY(), targetX, targetZ);
            }
        }
    }

    private void freezeSurface(Level level, int baseY, int x, int z) {
        BlockPos foundPos = null;

        for (int y = baseY + VERTICAL_SEARCH_RANGE; y >= baseY - VERTICAL_SEARCH_RANGE; y--) {
            BlockPos currentPos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(currentPos);
            FluidState fluid = state.getFluidState();

            boolean isWaterSurface = !fluid.isEmpty();
            boolean isSolidSurface = state.isSolid() && state.getBlock() != Blocks.ICE && state.getBlock() != Blocks.PACKED_ICE;

            if (isWaterSurface || isSolidSurface) {
                BlockState aboveState = level.getBlockState(currentPos.above());
                if (aboveState.isAir() || !aboveState.isSolid() || !aboveState.getFluidState().isEmpty()) {
                    foundPos = currentPos;
                    break;
                }
            }
        }

        if (foundPos == null) {
            for (int y = baseY; y <= baseY + VERTICAL_SEARCH_RANGE; y++) {
                BlockPos wallPos = new BlockPos(x, y, z);
                BlockState state = level.getBlockState(wallPos);
                if (state.isAir() || !state.isSolid()) {
                    foundPos = wallPos.below();
                    break;
                }
            }
        }

        if (foundPos != null) {
            BlockState targetState = level.getBlockState(foundPos);
            if (targetState.getDestroySpeed(level, foundPos) >= 0.0F) {
                level.setBlock(foundPos, Blocks.ICE.defaultBlockState(), 3);
            }
        }
    }
}