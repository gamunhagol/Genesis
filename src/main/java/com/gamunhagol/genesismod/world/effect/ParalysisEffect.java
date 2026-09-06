package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ParalysisEffect extends MobEffect {
    public ParalysisEffect() {
        super(MobEffectCategory.HARMFUL, 0xE5D142);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d",
                -1.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
}