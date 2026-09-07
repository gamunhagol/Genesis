package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class LargeFireballSpell extends MagicSpell {
    public LargeFireballSpell() { super("large_fireball"); }

    @Override
    public int getCastTime() { return 25; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 15);
    }

    @Override
    public float getMentalCost() { return 3.5f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseFire = 5.0f;
        float magicEfficiency = 0.45f;
        float fireEfficiency = 0.2f;

        float finalFire = baseFire + (catalyst.magic() * magicEfficiency) + (catalyst.fire() * fireEfficiency);


        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        LargeFireball fireball = new LargeFireball(level, caster, look.x, look.y, look.z, 2);

        fireball.setPos(caster.getX(), caster.getEyeY(), caster.getZ());

        fireball.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(fireball);
    }
}