package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ShulkerBulletSpell extends MagicSpell {
    public ShulkerBulletSpell() { super("shulker_bullet"); }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 13);
    }

    @Override
    public float getMentalCost() { return 2.5f; }

    @Override
    public int getMemoryCost() { return 1; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 3.0f;
        float magicEfficiency = 0.5f;

        float finalMagic = baseMagic + (catalyst.magic() * magicEfficiency);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        LivingEntity target = getTargetInSight(caster, 30.0D);

        if (target == null) return;

        Direction.Axis axis = caster.getDirection().getAxis();
        ShulkerBullet bullet = new ShulkerBullet(level, caster, target, axis);

        bullet.setPos(caster.getX(), caster.getEyeY(), caster.getZ());
        bullet.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));

        level.addFreshEntity(bullet);
    }

    public static LivingEntity getTargetInSight(LivingEntity caster, double range) {
        Vec3 start = caster.getEyePosition();
        Vec3 look = caster.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(range));
        AABB searchBox = caster.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D);

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                caster, start, end, searchBox,
                e -> e instanceof LivingEntity && e != caster && e.isAlive(),
                range * range
        );

        if (hit != null && hit.getEntity() instanceof LivingEntity livingHit) {
            return livingHit;
        }
        return null;
    }
}
