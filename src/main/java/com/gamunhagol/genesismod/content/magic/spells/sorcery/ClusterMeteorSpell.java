package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicMeteor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ClusterMeteorSpell extends MagicSpell {
    public ClusterMeteorSpell() { super("cluster_meteor"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 19);
    }

    @Override public float getMentalCost() { return 4.4f; }
    @Override public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float finalMagic = 8.0f + (catalyst.magic() * 1.15f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        fireMeteors(level, caster, spellSnapshot, 3, 0.28D, true);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        onExecute(level, caster, spellSnapshot);
    }

    private void fireMeteors(Level level, LivingEntity caster, DamageSnapshot snapshot, int count, double spread, boolean split) {
        Vec3 look = caster.getLookAngle();
        Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
        double angleStep = (count > 1) ? (spread * 2) / (count - 1) : 0;
        double startOffset = -spread;

        for (int i = 0; i < count; i++) {
            double currentOffset = (count > 1) ? startOffset + (angleStep * i) : 0.0D;
            Vec3 dir = look.add(right.scale(currentOffset)).normalize();

            MagicMeteor meteor = new MagicMeteor(level, caster, snapshot);
            meteor.setPos(caster.getX(), caster.getEyeY() - 0.1D, caster.getZ());
            meteor.setDeltaMovement(dir.scale(0.25D));
            if (split) {
                meteor.setSplitting(true, 20);
            }

            meteor.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
            level.addFreshEntity(meteor);
        }
    }
}