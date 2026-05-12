package com.gamunhagol.genesismod.content.magic.miracles;

import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HealMiracle extends MiracleSpell {
    public HealMiracle() {
        super("heal");
    }

    @Override
    public int getCastTime() {
        return 45; // 2.25초
    }

    @Override
    public int getRequiredStatLevel() {
        return 10;
    }

    @Override
    public float getMentalCost() {
        return 3.0f;
    }

    @Override
    public void execute(Level level, LivingEntity caster) {
        if (!level.isClientSide) {
            caster.heal(8.0F);

            // 시전 완료 시 멘탈(마나) 소모
            consumeMental(caster);
        }
    }
}