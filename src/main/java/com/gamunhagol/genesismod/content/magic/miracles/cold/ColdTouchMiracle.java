package com.gamunhagol.genesismod.content.magic.miracles.cold;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ColdTouchMiracle extends MiracleSpell {

    public ColdTouchMiracle() {
        super("cold_touch");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 6.3f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return new DamageSnapshot(0, 0, 0, 0, 1.0f, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double reach = 3.0D;
        if (caster instanceof Player player) {
            reach = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
        }

        LivingEntity target = getTargetInLook(level, caster, reach);
        boolean isSelf = (target == null || target == caster);
        LivingEntity actualTarget = isSelf ? caster : target;

        Snowball dummy = new Snowball(level, caster.getX(), caster.getY(), caster.getZ());
        dummy.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        actualTarget.hurt(caster.damageSources().indirectMagic(dummy, caster), 1.0F);
        dummy.discard();

        actualTarget.clearFire();
        actualTarget.setRemainingFireTicks(0);

        if (isSelf) {
            List<MobEffectInstance> activeEffects = new ArrayList<>(caster.getActiveEffects());
            for (MobEffectInstance inst : activeEffects) {
                if (inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    caster.removeEffect(inst.getEffect());
                }
            }
        } else {
            List<MobEffectInstance> targetEffects = new ArrayList<>(actualTarget.getActiveEffects());
            for (MobEffectInstance inst : targetEffects) {
                if (inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    int halfDuration = Math.max(1, inst.getDuration() / 2);
                    caster.addEffect(new MobEffectInstance(
                            inst.getEffect(),
                            halfDuration,
                            inst.getAmplifier(),
                            inst.isAmbient(),
                            inst.isVisible(),
                            inst.showIcon()
                    ));
                    actualTarget.removeEffect(inst.getEffect());
                }
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.SNOWFLAKE,
                    actualTarget.getX(), actualTarget.getY() + (actualTarget.getBbHeight() * 0.5D), actualTarget.getZ(),
                    15, 0.3D, 0.3D, 0.3D, 0.05D
            );
        }

        level.playSound(
                null,
                actualTarget.getX(), actualTarget.getY(), actualTarget.getZ(),
                SoundEvents.FIRE_EXTINGUISH,
                SoundSource.PLAYERS,
                0.7F,
                1.6F
        );
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

        double maxDistSqr = reach * reach;
        if (blockHit.getType() != HitResult.Type.MISS) {
            endPos = blockHit.getLocation();
            maxDistSqr = eyePos.distanceToSqr(endPos);
        }

        AABB searchBox = caster.getBoundingBox().expandTowards(lookVec.scale(reach)).inflate(1.0D);
        List<Entity> candidates = level.getEntities(caster, searchBox,
                e -> e instanceof LivingEntity && e.isAlive() && !e.isSpectator());

        LivingEntity closestTarget = null;
        double closestDistSqr = maxDistSqr;

        for (Entity candidate : candidates) {
            AABB targetBox = candidate.getBoundingBox();
            Optional<Vec3> hitOpt = targetBox.clip(eyePos, endPos);

            if (hitOpt.isPresent()) {
                double distSqr = eyePos.distanceToSqr(hitOpt.get());
                if (distSqr < closestDistSqr) {
                    closestDistSqr = distSqr;
                    closestTarget = (LivingEntity) candidate;
                }
            }
        }

        return closestTarget;
    }
}