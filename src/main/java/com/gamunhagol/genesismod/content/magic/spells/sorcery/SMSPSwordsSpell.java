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

public class SMSPSwordsSpell extends MagicSpell {
    public SMSPSwordsSpell() { super("summon_magic_sword_phalanx"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 26,
                StatType.ARCANE, 15);
    }

    @Override public float getMentalCost() { return 7.0f; }
    @Override public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 10.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.9f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        int count = 7;
        double widthSpan = 2.4D;
        double peakHeight = 0.45D;
        double archDepth = 0.35D;

        float yawRad = caster.getYRot() * Mth.DEG_TO_RAD;
        double rightX = -Math.cos(yawRad);
        double rightZ = -Math.sin(yawRad);

        for (int i = 0; i < count; i++) {
            double t = -1.0D + (2.0D * i / (count - 1));
            double sideOffset = t * (widthSpan / 2.0D);
            double height = peakHeight - (t * t * archDepth);

            MagicSwordEntity sword = new MagicSwordEntity(level, caster, spellSnapshot);
            sword.setOrbit(sideOffset, height);

            double spawnX = caster.getX() + (rightX * sideOffset);
            double spawnY = caster.getY() + caster.getBbHeight() + height;
            double spawnZ = caster.getZ() + (rightZ * sideOffset);
            sword.setPos(spawnX, spawnY, spawnZ);

            sword.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(sword);
        }
    }
}