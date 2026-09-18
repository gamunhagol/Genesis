package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class ConjureStoneWallMiracle extends MiracleSpell {
    public ConjureStoneWallMiracle() { super("conjure_stone_wall"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 13);
    }

    @Override
    public float getMentalCost() { return 5.6f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    public MiracleElement getElement() { return MiracleElement.EARTH; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        HitResult hitResult = caster.pick(5.0D, 1.0F, false);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            BlockPos basePos = blockHit.getBlockPos().relative(blockHit.getDirection());

            Direction facing = caster.getDirection();
            boolean isXAxis = (facing == Direction.EAST || facing == Direction.WEST);

            for (int w = -2; w <= 2; w++) {
                for (int h = 0; h < 3; h++) {
                    BlockPos targetPos = isXAxis ? basePos.offset(0, h, w) : basePos.offset(w, h, 0);

                    if (level.getBlockState(targetPos).canBeReplaced()) {
                        level.setBlock(targetPos, Blocks.STONE.defaultBlockState(), 3);
                    }
                }
            }
            level.playSound(null, basePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}