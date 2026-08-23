package com.gamunhagol.genesismod.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningRodEffect extends MobEffect {
    public LightningRodEffect() {
        super(MobEffectCategory.HARMFUL, 0xC4BD62);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();

        if (!level.isClientSide() && level.isThundering() && level.canSeeSky(entity.blockPosition())) {

            float chance = 0.15F;

            if (level.random.nextFloat() < chance) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                if (lightning != null) {
                    lightning.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
                    level.addFreshEntity(lightning);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
