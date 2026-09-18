package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.RockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PullRockMiracle extends MiracleSpell {
    public PullRockMiracle() { super("pull_rock"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 14,
        StatType.STRENGTH, 15);
    }

    @Override public float getMentalCost() { return 5.5f; }
    @Override public int getMemoryCost() { return 1; }
    @Override public MiracleElement getElement() { return MiracleElement.EARTH; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 14.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.9f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        RockEntity rock = new RockEntity(level, caster, spellSnapshot);
        Vec3 look = caster.getLookAngle();
        rock.setPos(caster.getX(), caster.getEyeY() + 0.2D, caster.getZ());
        rock.setDeltaMovement(look.scale(0.8D));

        rock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(rock);
    }
}