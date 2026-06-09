package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class AbstractSpell {
    private final String id;

    public AbstractSpell(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public abstract int getCastTime();

    public abstract int getRequiredStatLevel();

    public Map<StatType, Integer> getRequiredStats() {
        return Map.of();
    }

    public abstract float getMentalCost();
    public abstract boolean canCast(LivingEntity caster);
    public abstract int getMemoryCost();
    public void onCastingTick(LivingEntity caster, int remainingTicks) {}

    public void executeCast(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot) {
        if (!level.isClientSide) {
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(catalystSnapshot);

            onExecute(level, caster, spellSnapshot);

            consumeMental(caster);
        }
    }

    protected abstract DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalystSnapshot);

    protected abstract void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot);

    public void consumeMental(LivingEntity caster) {
        if (caster instanceof net.minecraft.world.entity.player.Player player) {
            player.getCapability(com.gamunhagol.genesismod.stats.StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(stats.getMental() - getMentalCost());
            });
        }
    }
}