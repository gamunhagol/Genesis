package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.MagicMist;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MagicMistSpell extends MagicSpell {
    public MagicMistSpell() { super("magic_mist"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 10);
    }

    @Override
    public float getMentalCost() { return 1.0f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 2.5f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.5f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        net.minecraft.world.phys.HitResult hitResult = caster.pick(12.0D, 1.0F, false);
        Vec3 centerPos;

        if (hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            centerPos = hitResult.getLocation().add(0, 0.05D, 0);
        } else {
            Vec3 look = caster.getLookAngle();
            centerPos = caster.position().add(look.x * 5.0D, 0.05D, look.z * 5.0D);
        }

        double spacing = 1.0D;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                MagicMist mist = new MagicMist(level, caster, spellSnapshot);

                double offsetX = (x * spacing) + ((level.random.nextDouble() - 0.5D) * 0.5D);
                double offsetZ = (z * spacing) + ((level.random.nextDouble() - 0.5D) * 0.5D);

                mist.setPos(centerPos.x + offsetX, centerPos.y, centerPos.z + offsetZ);

                mist.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
                level.addFreshEntity(mist);
            }
        }
    }
}