package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class EvokerFangsCircleSpell extends MagicSpell {
    public EvokerFangsCircleSpell() { super("evoker_fangs_circle"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 14);
    }

    @Override
    public float getMentalCost() { return 4.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 6.0f;
        float magicEfficiency = 0.4f;

        float finalMagic = baseMagic + (catalyst.magic() * magicEfficiency);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        float f = (float) Mth.atan2(caster.getLookAngle().z, caster.getLookAngle().x);

        for(int i = 0; i < 5; ++i) {
            float f1 = f + (float)i * (float)Math.PI * 0.4F;
            this.spawnFangs(level, caster, caster.getX() + (double)Mth.cos(f1) * 1.5D, caster.getZ() + (double)Mth.sin(f1) * 1.5D, caster.getY(), caster.getY() + 1.0D, f1, 0, spellSnapshot);
        }

        for(int k = 0; k < 8; ++k) {
            float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
            this.spawnFangs(level, caster, caster.getX() + (double)Mth.cos(f2) * 2.5D, caster.getZ() + (double)Mth.sin(f2) * 2.5D, caster.getY(), caster.getY() + 1.0D, f2, 3, spellSnapshot);
        }
    }

    private void spawnFangs(Level level, LivingEntity caster, double x, double z, double minY, double maxY, float rot, int delay, DamageSnapshot spellSnapshot) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, net.minecraft.core.Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(net.minecraft.core.Direction.Axis.Y);
                    }
                }
                flag = true;
                break;
            }
            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag && !level.isClientSide) {
            EvokerFangs fangs = new EvokerFangs(level, x, (double)blockpos.getY() + d0, z, rot, delay, caster);
            fangs.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(fangs);
        }
    }
}