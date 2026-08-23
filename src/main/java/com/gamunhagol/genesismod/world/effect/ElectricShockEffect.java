package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ElectricShockEffect extends MobEffect {
    public ElectricShockEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFE332);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        float damage = 1.0F + amplifier;
        entity.hurt(entity.damageSources().lightningBolt(), damage);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}