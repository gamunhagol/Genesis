package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicComet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MagicCometSpell extends MagicSpell {
    public MagicCometSpell() { super("magic_comet"); }

    @Override
    public int getMaxChargeTicks() { return 80; }

    @Override
    public boolean isChargeable(LivingEntity caster) { return true; }

    @Override
    public boolean isChargePhase(LivingEntity caster) { return true; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 23);
    }

    @Override
    public float getMentalCost() { return 7.5f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 15.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 1.15f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        shootComet(level, caster, spellSnapshot);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float damageMultiplier = 1.0f;

        if (ratio >= 0.75f) {
            damageMultiplier = 1.5f;
        } else if (ratio >= 0.5f) {
            damageMultiplier = 1.25f;
        }

        DamageSnapshot chargedSnapshot = new DamageSnapshot(
                0, spellSnapshot.magic() * damageMultiplier, 0, 0, 0, 0, 0
        );
        shootComet(level, caster, chargedSnapshot);
    }

    private void shootComet(Level level, LivingEntity caster, DamageSnapshot snapshot) {
        MagicComet comet = new MagicComet(level, caster, snapshot);
        Vec3 look = caster.getLookAngle();
        comet.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        comet.setDeltaMovement(look.scale(2.55D));

        comet.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        level.addFreshEntity(comet);
    }
}