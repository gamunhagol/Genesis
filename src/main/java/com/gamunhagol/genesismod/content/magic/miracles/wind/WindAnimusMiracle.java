package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.BladeStormEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WindAnimusMiracle extends MiracleSpell {
    private final float inaccuracy = 14.0F;

    public WindAnimusMiracle() {
        super("wind_animus");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 19);
    }

    @Override
    public float getMentalCost() {
        return 4.6f;
    }

    @Override
    public boolean isContinuous(LivingEntity caster) {
        return true;
    }

    @Override
    public float getContinuousMentalCost() {
        return 0.14f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 5.0f;
        float finalPhysical = (basePhysical + (catalyst.holy() * 0.35f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
    }

    @Override
    protected void onExecuteContinuous(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int ticksUsing) {
        if (level.isClientSide) return;

        if (ticksUsing % 2 == 0) {
            BladeStormEntity blade = new BladeStormEntity(level, caster);
            Vec3 look = caster.getLookAngle();

            double offsetX = (level.random.nextDouble() - 0.5D) * 0.6D;
            double offsetY = (level.random.nextDouble() - 0.5D) * 0.3D;
            double offsetZ = (level.random.nextDouble() - 0.5D) * 0.6D;

            blade.setPos(caster.getX() + offsetX, caster.getEyeY() - 0.1D + offsetY, caster.getZ() + offsetZ);
            blade.shoot(look.x, look.y, look.z, 1.8F, this.inaccuracy);

            blade.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(blade);
        }
    }
}