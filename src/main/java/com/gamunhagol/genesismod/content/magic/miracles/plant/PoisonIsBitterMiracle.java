package com.gamunhagol.genesismod.content.magic.miracles.plant;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PoisonIsBitterMiracle extends MiracleSpell {

    public PoisonIsBitterMiracle() {
        super("poison_is_bitter");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 12
        );
    }

    @Override
    public float getMentalCost() {
        return 4.1f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FOREST;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double interactionRange = 4.5D;
        LivingEntity target = getTargetInSight(caster, interactionRange);

        if (target != null) {
            int baseDurationTicks = 200;

            int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(StatCapability::getFaith)
                    .orElse(0);

            float faithBonusMultiplier = 1.0f + (faith * 0.01f);
            float dedicationMultiplier = getDedicationMultiplier(caster);

            int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

            target.addEffect(new MobEffectInstance(MobEffects.POISON, finalDuration, 2));

        }
    }

    private LivingEntity getTargetInSight(LivingEntity caster, double range) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getViewVector(1.0F);
        Vec3 reachVec = eyePos.add(look.scale(range));
        AABB aabb = caster.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D);

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                caster,
                eyePos,
                reachVec,
                aabb,
                e -> e instanceof LivingEntity && e != caster && e.isAlive(),
                range * range
        );

        if (hit != null && hit.getEntity() instanceof LivingEntity living) {
            return living;
        }
        return null;
    }
}