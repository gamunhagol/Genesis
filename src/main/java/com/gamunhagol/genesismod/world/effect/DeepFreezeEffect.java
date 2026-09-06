package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DeepFreezeEffect extends MobEffect {
    public DeepFreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0x55FFFF);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e",
                -1.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
}