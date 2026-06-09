package com.gamunhagol.genesismod.content.magic.miracles;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class HealMiracle extends MiracleSpell {
    public HealMiracle() { super("little_heal"); }

    @Override
    public int getCastTime() { return 45; }
    @Override
    public int getRequiredStatLevel() { return 10; }
    @Override
    public float getMentalCost() { return 3.0f; }
    @Override
    public int getMemoryCost() {return 1;}

    @Override
    protected DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalyst) {
        float healAmount = 4.0f + catalyst.holy();

        float efficiency = 0.3f;
        float finalHeal = healAmount + (catalyst.holy() * efficiency);

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        caster.heal(spellSnapshot.holy());
    }
}