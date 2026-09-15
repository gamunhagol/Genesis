package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicSwordEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SLMSSwordsSpell extends MagicSpell {
    public SLMSSwordsSpell() { super("summon_linked_magic_swords"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 19,
                StatType.ARCANE, 11);
    }

    @Override public float getMentalCost() { return 4.9f; }
    @Override public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 10.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.9f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        double[] sideOffsets = {-0.75D, 0.0D, 0.75D};
        double[] heightOffsets = {0.15D, 0.35D, 0.15D};

        float yawRad = caster.getYRot() * Mth.DEG_TO_RAD;
        double rightX = -Math.cos(yawRad);
        double rightZ = -Math.sin(yawRad);

        for (int i = 0; i < 3; i++) {
            MagicSwordEntity sword = new MagicSwordEntity(level, caster, spellSnapshot);
            sword.setOrbit(sideOffsets[i], heightOffsets[i]);

            double spawnX = caster.getX() + (rightX * sideOffsets[i]);
            double spawnY = caster.getY() + caster.getBbHeight() + heightOffsets[i];
            double spawnZ = caster.getZ() + (rightZ * sideOffsets[i]);
            sword.setPos(spawnX, spawnY, spawnZ);

            sword.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(sword);
        }
    }
}