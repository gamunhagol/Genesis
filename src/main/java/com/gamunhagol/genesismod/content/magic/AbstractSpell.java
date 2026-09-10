package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public abstract class AbstractSpell {
    private final String id;

    public AbstractSpell(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescriptionId() {
        return "spell.genesis." + this.id;
    }

    public Component getName() {
        return Component.translatable(getDescriptionId());
    }

    public int getMaxChargeTicks() {
        return 0;
    }

    public int getMinChargeTicks() {
        return 0;
    }

    public abstract Map<StatType, Integer> getRequiredStats();

    public abstract float getMentalCost();
    public abstract boolean canCast(LivingEntity caster);
    public abstract int getMemoryCost();

    public boolean isChargeable(LivingEntity caster) {
        return getMaxChargeTicks() > 0;
    }

    public boolean isChargePhase(LivingEntity caster) {
        return isChargeable(caster);
    }

    public void executeCast(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot) {
        if (!level.isClientSide) {
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(caster, catalystSnapshot);
            onExecute(level, caster, spellSnapshot);
            consumeMental(caster);
        }
    }

    public void executeCastCharged(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot, int chargeTicks) {
        if (!level.isClientSide) {
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(caster, catalystSnapshot);
            onExecuteCharged(level, caster, spellSnapshot, chargeTicks);
            consumeMental(caster);
        }
    }

    protected abstract DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalystSnapshot);

    protected abstract void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot);

    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        onExecute(level, caster, spellSnapshot);
    }

    public void consumeMental(LivingEntity caster) {
        if (caster instanceof net.minecraft.world.entity.player.Player player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(stats.getMental() - getMentalCost());
            });
        }
    }
}