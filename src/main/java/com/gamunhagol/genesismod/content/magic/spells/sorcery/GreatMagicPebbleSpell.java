package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.GreatMagicPebble;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class GreatMagicPebbleSpell extends MagicSpell {
    public GreatMagicPebbleSpell() { super("great_magic_pebble"); }

    @Override
    public int getMaxChargeTicks() { return 60; }

    @Override
    public boolean isChargeable(LivingEntity caster) { return true; }

    @Override
    public boolean isChargePhase(LivingEntity caster) { return true; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 17);
    }

    @Override
    public float getMentalCost() { return 4.6f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 8.5f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.85f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        shootPebble(level, caster, spellSnapshot);
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
        shootPebble(level, caster, chargedSnapshot);
    }

    private void shootPebble(Level level, LivingEntity caster, DamageSnapshot snapshot) {
        GreatMagicPebble pebble = new GreatMagicPebble(level, caster, snapshot);
        Vec3 look = caster.getLookAngle();
        pebble.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        pebble.setDeltaMovement(look.scale(1.65D));

        pebble.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        level.addFreshEntity(pebble);
    }
}