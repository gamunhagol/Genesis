package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class SeaCurseMiracle extends MiracleSpell {
    private static final double TARGET_REACH = 3.0D;

    public SeaCurseMiracle() {
        super("sea_curse");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 7.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WATER;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        LivingEntity target = getTargetInLook(level, caster, TARGET_REACH);

        if (target != null) {
            int baseDurationTicks = 1800;

            int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(StatCapability::getFaith)
                    .orElse(0);

            float faithBonusMultiplier = 1.0f + (faith * 0.01f);

            float dedicationMultiplier = getDedicationMultiplier(caster);

            int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

            target.addEffect(new MobEffectInstance(GenesisEffects.SEA_CURSE.get(), finalDuration, 0));

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.BUBBLE_POP,
                        target.getX(), target.getY() + (target.getBbHeight() * 0.5D), target.getZ(),
                        12, 0.3D, 0.3D, 0.3D, 0.05D
                );
            }

            level.playSound(
                    null,
                    target.getX(), target.getY(), target.getZ(),
                    SoundEvents.ELDER_GUARDIAN_CURSE,
                    caster.getSoundSource(),
                    0.8F,
                    1.4F
            );
        } else {
            level.playSound(
                    null,
                    caster.getX(), caster.getY(), caster.getZ(),
                    SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE,
                    caster.getSoundSource(),
                    0.6F,
                    0.8F
            );
        }
    }

    private LivingEntity getTargetInLook(Level level, LivingEntity caster, double reach) {
        Vec3 eyePos = caster.getEyePosition();
        Vec3 lookVec = caster.getLookAngle();
        Vec3 endPos = eyePos.add(lookVec.scale(reach));

        HitResult blockHit = level.clip(new ClipContext(
                eyePos, endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                caster
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
            endPos = blockHit.getLocation();
        }

        AABB box = caster.getBoundingBox().expandTowards(lookVec.scale(reach)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                caster,
                eyePos,
                endPos,
                box,
                entity -> entity instanceof LivingEntity && entity.isAlive() && !entity.isSpectator()
        );

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            return livingTarget;
        }

        return null;
    }
}