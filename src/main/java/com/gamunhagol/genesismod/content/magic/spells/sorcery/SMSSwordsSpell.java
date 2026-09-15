package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicSwordEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SMSSwordsSpell extends MagicSpell {
    public SMSSwordsSpell() { super("summon_magic_sword"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 14,
                StatType.ARCANE, 11);
    }

    @Override public float getMentalCost() { return 3.5f; }
    @Override public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 10.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.9f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        MagicSwordEntity sword = new MagicSwordEntity(level, caster, spellSnapshot);
        sword.setOrbit(0.0D, 0.4D);
        sword.setPos(caster.getX(), caster.getY() + caster.getBbHeight() + 0.4D, caster.getZ());
        sword.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(sword);
    }
}