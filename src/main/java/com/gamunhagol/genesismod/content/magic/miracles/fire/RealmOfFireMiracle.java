package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class RealmOfFireMiracle extends MiracleSpell {
    public RealmOfFireMiracle() {
        super("realm_of_fire");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 17);
    }

    @Override
    public float getMentalCost() {
        return 11.7f;
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
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseFire = 10.0f;
        float finalFire = (baseFire + (catalyst.fire() * 0.6f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        caster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0));

        BlockPos center = caster.blockPosition();

        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                BlockPos targetPos = center.offset(x, 0, z);

                for (int yOffset = -2; yOffset <= 2; yOffset++) {
                    BlockPos checkPos = targetPos.offset(0, yOffset, 0);
                    BlockPos belowPos = checkPos.below();
                    BlockState belowState = level.getBlockState(belowPos);

                    if (level.getBlockState(checkPos).isAir() && belowState.isFaceSturdy(level, belowPos, net.minecraft.core.Direction.UP)) {
                        level.setBlock(checkPos, Blocks.FIRE.defaultBlockState(), 3);
                        break;
                    }
                }
            }
        }

        AABB area = new AABB(center.offset(-5, -2, -5), center.offset(5, 3, 5));
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && e != caster);

        for (LivingEntity target : targets) {
            GenesisDamageCalculator.applySnapshotDamage(target, caster, spellSnapshot);
        }
    }
}