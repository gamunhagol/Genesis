package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class AreaHealMiracle extends MiracleSpell {

    public AreaHealMiracle() {
        super("area_heal");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 6.8f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float healAmount = 4.0f + catalyst.holy();
        float efficiency = 0.3f;

        float baseHeal = healAmount + (catalyst.holy() * efficiency);
        float multiplier = getDedicationMultiplier(caster);
        float finalHeal = baseHeal * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double radius = 3.0D;
        AABB area = caster.getBoundingBox().inflate(radius);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, Entity::isAlive);

        int amplifier = Math.max(0, Math.round(spellSnapshot.holy() / 4.0f) - 1);
        for (LivingEntity target : targets) {
            target.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, amplifier, false, false, true));
        }
    }
}
