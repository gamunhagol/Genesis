package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicMist;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
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
        float baseMagic = 2.2f;
        float finalMagic = baseMagic + (catalyst.magic() * 0.5f);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        HitResult hitResult = caster.pick(12.0D, 1.0F, false);
        Vec3 centerPos;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            centerPos = hitResult.getLocation().add(0, 0.05D, 0);
        } else {
            Vec3 look = caster.getLookAngle();
            centerPos = caster.position().add(look.x * 5.0D, 0.05D, look.z * 5.0D);
        }

        int mistCount = 16 + level.random.nextInt(9);
        double maxRadius = 4.0D;

        for (int i = 0; i < mistCount; i++) {
            MagicMist mist = new MagicMist(level, caster, spellSnapshot);

            double offsetX = (level.random.nextDouble() - 0.5D) * 2.0D * maxRadius;
            double offsetZ = (level.random.nextDouble() - 0.5D) * 2.0D * maxRadius;
            double targetX = centerPos.x + offsetX;
            double targetZ = centerPos.z + offsetZ;

            BlockPos groundPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(targetX, centerPos.y, targetZ));
            double targetY = groundPos.getY() + 0.05D;

            mist.setPos(targetX, targetY, targetZ);
            mist.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(mist);
        }
    }
}
