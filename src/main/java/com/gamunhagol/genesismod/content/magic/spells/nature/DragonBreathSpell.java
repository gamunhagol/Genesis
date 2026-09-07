package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class DragonBreathSpell extends MagicSpell {
    public DragonBreathSpell() { super("dragon_breath"); }

    @Override
    public int getCastTime() { return 20; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 18);
    }

    @Override
    public float getMentalCost() { return 6.7f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePower = 8.0f;
        float magicEfficiency = 0.4f;
        float fireEfficiency = 0.2f;

        float finalMagic = basePower + (catalyst.magic() * magicEfficiency) + (catalyst.fire() * fireEfficiency);

        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        DragonFireball fireball = new DragonFireball(level, caster, look.x, look.y, look.z);
        fireball.setPos(caster.getX(), caster.getEyeY(), caster.getZ());

        fireball.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(fireball);
    }
}