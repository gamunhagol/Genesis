package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.PebbleEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ThrowStoneMiracle extends MiracleSpell {
    public ThrowStoneMiracle() { super("throw_stone"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override public float getMentalCost() { return 2.0f; }
    @Override public int getMemoryCost() { return 1; }
    @Override public MiracleElement getElement() { return MiracleElement.EARTH; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 5.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        PebbleEntity pebble = new PebbleEntity(level, caster, spellSnapshot);
        Vec3 look = caster.getLookAngle();
        pebble.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        pebble.setDeltaMovement(look.scale(1.2D));

        pebble.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(pebble);
    }
}