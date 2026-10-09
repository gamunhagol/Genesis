package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.EmberEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class EmberMiracle extends MiracleSpell {
    public EmberMiracle() {
        super("ember");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override
    public float getMentalCost() {
        return 2.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseFire = 6.0f;
        float finalFire = (baseFire + (catalyst.fire() * 0.5f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, finalFire, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.getEyePosition().add(look.scale(1.0D));

        EmberEntity ember = new EmberEntity(level, caster, spellSnapshot);
        ember.setPos(spawnPos.x, spawnPos.y - 0.2D, spawnPos.z);
        ember.setDeltaMovement(look.scale(0.1D));

        ember.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(ember);
    }
}