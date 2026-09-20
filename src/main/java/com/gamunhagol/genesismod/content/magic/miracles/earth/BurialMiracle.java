package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;

public class BurialMiracle extends MiracleSpell {

    public BurialMiracle() {
        super("burial");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 15);
    }

    @Override
    public float getMentalCost() {
        return 5.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.EARTH;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double reach = 5.0D;
        AttributeInstance reachAttr = caster.getAttribute(ForgeMod.BLOCK_REACH.get());
        if (reachAttr != null) {
            reach = reachAttr.getValue();
        }

        LivingEntity target = getTargetInReach(caster, reach);

        if (target != null) {
            target.teleportTo(target.getX(), target.getY() - 3.0D, target.getZ());
        }
    }

    private LivingEntity getTargetInReach(LivingEntity caster, double range) {
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