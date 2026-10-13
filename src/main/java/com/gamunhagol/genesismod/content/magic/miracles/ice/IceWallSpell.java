package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class IceWallSpell extends MiracleSpell {

    public IceWallSpell() {
        super("ice_wall");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 11);
    }

    @Override
    public float getMentalCost() {
        return 2.4f;
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

        HitResult hitResult = caster.pick(5.0D, 1.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;
            Direction hitFace = blockHit.getDirection();
            BlockPos targetBasePos = blockHit.getBlockPos().relative(hitFace);

            level.setBlock(targetBasePos, Blocks.PACKED_ICE.defaultBlockState(), 3);
            level.setBlock(targetBasePos.above(), Blocks.PACKED_ICE.defaultBlockState(), 3);
        }
    }
}