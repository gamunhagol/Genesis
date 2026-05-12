package com.gamunhagol.genesismod.content.magic.spells;

import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends MagicSpell {
    public FireballSpell() {
        super("fireball");
    }

    @Override
    public int getCastTime() {
        return 20; // 1초 (20틱)
    }

    @Override
    public int getRequiredStatLevel() {
        return 12; // 지력 12 요구
    }

    @Override
    public float getMentalCost() {
        return 2.0f;
    }

    @Override
    public void execute(Level level, LivingEntity caster) {
        if (!level.isClientSide) {
            Vec3 look = caster.getLookAngle();
            SmallFireball fireball = new SmallFireball(level, caster, look.x, look.y, look.z);
            fireball.setPos(caster.getX(), caster.getEyeY(), caster.getZ());
            level.addFreshEntity(fireball);

            // 시전 완료 시 멘탈(마나) 소모
            consumeMental(caster);
        }
    }
}