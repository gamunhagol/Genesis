package com.gamunhagol.genesismod.content.magic.miracles.cold;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageCalculator;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrostDomainMiracle extends MiracleSpell {

    public FrostDomainMiracle() {
        super("frost_domain");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 20);
    }

    @Override
    public float getMentalCost() {
        return 11.7f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float multiplier = getDedicationMultiplier(caster);
        float baseFrost = 6.0f;
        float finalFrost = (baseFrost + (catalyst.holy() * 0.4f)) * multiplier;

        float baseHeal = 10.0f;
        float finalHeal = (baseHeal + (catalyst.holy() * 0.5f)) * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, finalFrost, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double cx = caster.getX();
        double cy = caster.getY();
        double cz = caster.getZ();

        double innerRadius = 5.0D;
        double innerRadiusSq = innerRadius * innerRadius;
        float healAmount = spellSnapshot.holy();

        AABB innerBox = new AABB(
                cx - innerRadius, cy - innerRadius, cz - innerRadius,
                cx + innerRadius, cy + innerRadius, cz + innerRadius
        );

        List<LivingEntity> insideEntities = level.getEntitiesOfClass(
                LivingEntity.class,
                innerBox,
                e -> e.isAlive() && e.distanceToSqr(cx, cy, cz) <= innerRadiusSq
        );

        for (LivingEntity target : insideEntities) {
            List<MobEffectInstance> harmfulEffects = new ArrayList<>();
            for (MobEffectInstance effect : target.getActiveEffects()) {
                if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    harmfulEffects.add(effect);
                }
            }
            for (MobEffectInstance effect : harmfulEffects) {
                target.removeEffect(effect.getEffect());
            }

            if (target.isInvertedHealAndHarm()) {
                target.hurt(GenesisDamageTypes.getSource(level, GenesisDamageTypes.HOLY, caster), healAmount);
            } else {
                target.heal(healAmount);
            }
        }

        BlockPos center = caster.blockPosition();
        int outerRadius = 7;
        int outerRadiusSq = outerRadius * outerRadius;
        int innerBlockRadiusSq = 25;

        for (int x = -outerRadius; x <= outerRadius; x++) {
            for (int y = -outerRadius; y <= outerRadius; y++) {
                for (int z = -outerRadius; z <= outerRadius; z++) {
                    int distSq = (x * x) + (y * y) + (z * z);
                    if (distSq > innerBlockRadiusSq && distSq <= outerRadiusSq) {
                        BlockPos targetPos = center.offset(x, y, z);
                        if (level.getBlockState(targetPos).isAir()) {
                            level.setBlock(targetPos, Blocks.ICE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        double minY = cy - 2.0D;
        double maxY = cy + 8.0D;

        AABB northBorder = new AABB(cx - 7.5D, minY, cz - 7.5D, cx + 7.5D, maxY, cz - 6.5D);
        AABB southBorder = new AABB(cx - 7.5D, minY, cz + 6.5D, cx + 7.5D, maxY, cz + 7.5D);
        AABB westBorder  = new AABB(cx - 7.5D, minY, cz - 7.5D, cx - 6.5D, maxY, cz + 7.5D);
        AABB eastBorder  = new AABB(cx + 6.5D, minY, cz - 7.5D, cx + 7.5D, maxY, cz + 7.5D);

        Set<LivingEntity> edgeTargets = new HashSet<>();
        for (AABB borderBox : List.of(northBorder, southBorder, westBorder, eastBorder)) {
            edgeTargets.addAll(level.getEntitiesOfClass(
                    LivingEntity.class,
                    borderBox,
                    e -> e.isAlive() && e != caster
            ));
        }

        DamageSnapshot edgeFrostSnapshot = new DamageSnapshot(0, 0, 0, 0, spellSnapshot.frost(), 0, 0);
        for (LivingEntity target : edgeTargets) {
            GenesisDamageCalculator.applySnapshotDamage(target, caster, edgeFrostSnapshot);
        }
    }
}
