package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicPebble;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MagicPebbleSpell extends MagicSpell {
    public MagicPebbleSpell() { super("magic_pebble"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 13);
    }

    @Override
    public float getMentalCost() { return 2.2f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 5.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.6f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        MagicPebble pebble = new MagicPebble(level, caster, spellSnapshot);
        Vec3 look = caster.getLookAngle();
        pebble.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
        pebble.setDeltaMovement(look.scale(1.25D));

        pebble.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(pebble);
    }
}