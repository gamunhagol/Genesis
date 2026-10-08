package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FireGazeMiracle extends MiracleSpell {
    private static final double RANGE = 15.0D;

    public FireGazeMiracle() {
        super("fire_gaze");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 15);
    }

    @Override
    public float getMentalCost() {
        return 3.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
    }

    @Override
    public boolean isContinuous(LivingEntity caster) {
        return true;
    }

    @Override
    public float getContinuousMentalCost() {
        return 0.09f;
    }

    @Override
    public int getContinuousTickInterval() {
        return 2;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        igniteLookTarget(level, caster);
    }

    @Override
    protected void onExecuteContinuous(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int ticksUsing) {
        igniteLookTarget(level, caster);
    }

    private void igniteLookTarget(Level level, LivingEntity caster) {
        if (level.isClientSide) return;

        Vec3 eyePos = caster.getEyePosition();
        Vec3 endPos = eyePos.add(caster.getLookAngle().scale(RANGE));

        BlockHitResult hit = level.clip(new ClipContext(
                eyePos, endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                caster
        ));

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos targetPos = hit.getBlockPos().relative(hit.getDirection());
            BlockState state = level.getBlockState(targetPos);

            if ((state.isAir() || state.canBeReplaced()) && state.getFluidState().isEmpty()) {
                level.setBlock(targetPos, BaseFireBlock.getState(level, targetPos), 3);
            }
        }
    }
}