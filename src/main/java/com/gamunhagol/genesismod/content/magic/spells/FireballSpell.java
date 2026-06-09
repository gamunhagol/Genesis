package com.gamunhagol.genesismod.content.magic.spells;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends MagicSpell {
    public FireballSpell() { super("fireball"); }

    @Override
    public int getCastTime() { return 20; }
    @Override
    public int getRequiredStatLevel() { return 10; }
    @Override
    public float getMentalCost() { return 2.0f; }
    @Override
    public int getMemoryCost() {return 1;}

    @Override
    protected DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalyst) {
        float basePower = 2.0f;

        float magicEfficiency = 0.4f;
        float fireEfficiency = 0.2f;

        float finalFire = basePower + (catalyst.magic() * magicEfficiency) + (catalyst.fire() * fireEfficiency);

        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }
    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        SmallFireball fireball = new SmallFireball(level, caster, look.x, look.y, look.z);
        fireball.setPos(caster.getX(), caster.getEyeY(), caster.getZ());

        fireball.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(fireball);
    }
}