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
    public PullRockMiracle() {
        super("pull_rock");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 14,
                StatType.STRENGTH, 15
        );
    }

    @Override
    public float getMentalCost() {
        return 5.5f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.EARTH;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 14.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.9f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 look = caster.getLookAngle();
        Vec3 forward = Vec3.directionFromRotation(0.0F, caster.getYRot());
        int holdTicks = 10;

        RockEntity rock = new RockEntity(level, caster, spellSnapshot, look.scale(0.8D), holdTicks);
        rock.setPos(
                caster.getX() + forward.x * 0.5D,
                caster.getY() + 2.2D,
                caster.getZ() + forward.z * 0.5D
        );

        rock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(rock);
    }
}
