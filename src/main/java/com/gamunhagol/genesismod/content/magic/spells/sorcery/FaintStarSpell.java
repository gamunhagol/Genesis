package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.FaintStarEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FaintStarSpell extends MagicSpell {
    public FaintStarSpell() { super("faint_star"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 19);
    }

    @Override public float getMentalCost() { return 6.0f; }
    @Override public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 10.0f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.6f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        HitResult hitResult = caster.pick(8.0D, 1.0F, false);
        Vec3 spawnPos;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 look = caster.getLookAngle();
            spawnPos = hitResult.getLocation().subtract(look.scale(0.3D));
        } else {
            spawnPos = hitResult.getLocation();
        }

        FaintStarEntity star = new FaintStarEntity(level, caster, spellSnapshot);
        star.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        star.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(star);
    }
}