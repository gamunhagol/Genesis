package com.gamunhagol.genesismod.content.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class AbstractSpell {
    private final String id;

    public AbstractSpell(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public abstract int getCastTime();

    public abstract int getRequiredStatLevel();

    public abstract float getMentalCost();

    public void onCastingTick(LivingEntity caster, int remainingTicks) {}

    public abstract void execute(Level level, LivingEntity caster);

    public abstract boolean canCast(LivingEntity caster);


    public void consumeMental(LivingEntity caster) {
        if (caster instanceof net.minecraft.world.entity.player.Player player) {
            player.getCapability(com.gamunhagol.genesismod.stats.StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(stats.getMental() - getMentalCost());
            });
        }
    }
}