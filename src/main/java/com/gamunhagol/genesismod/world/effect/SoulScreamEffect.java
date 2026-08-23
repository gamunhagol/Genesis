package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SoulScreamEffect extends MobEffect {
    public SoulScreamEffect() {
        super(MobEffectCategory.HARMFUL, 0x4A4A4A);

        this.addAttributeModifier(
                Attributes.MAX_HEALTH,
                "7104b2c4-8167-4632-a560-6395dc7c50a3",
                -10.0D,
                AttributeModifier.Operation.ADDITION
        );
    }
}