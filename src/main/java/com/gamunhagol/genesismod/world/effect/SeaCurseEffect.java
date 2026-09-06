package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class SeaCurseEffect extends MobEffect {
    public SeaCurseEffect() {
        super(MobEffectCategory.HARMFUL, 0x1D2D50);
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f",
                -0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWater()) {
            Vec3 motion = entity.getDeltaMovement();
            double newY = motion.y;

            if (newY > 0) {
                newY = 0.0D;
            }

            entity.setDeltaMovement(motion.x, newY - 0.08D, motion.z);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}