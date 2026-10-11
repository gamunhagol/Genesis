package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LightningSpearEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class LightningSpearMiracle extends MiracleSpell {

    public LightningSpearMiracle() {
        super("lightning_spear");
    }

    @Override
    public int getMaxChargeTicks() {
        return 100;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return true;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 4.9f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.LIGHTNING;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseLightning = 12.0f;
        float finalLightning = (baseLightning + (catalyst.lightning() * 0.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, 0, finalLightning, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        shootSpear(level, caster, spellSnapshot);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float multiplier = (ratio > 0.5f) ? 1.35f : 1.0f;

        DamageSnapshot chargedSnapshot = new DamageSnapshot(
                0, 0, 0, spellSnapshot.lightning() * multiplier, 0, 0, 0
        );
        shootSpear(level, caster, chargedSnapshot);
    }

    private void shootSpear(Level level, LivingEntity caster, DamageSnapshot snapshot) {
        Vec3 look = caster.getLookAngle();
        LightningSpearEntity spear = new LightningSpearEntity(level, caster, snapshot, look);
        spear.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        spear.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        level.addFreshEntity(spear);
    }
}