package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.stats.StatCapability;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.effect.GenesisEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.Map;

public class ParalysisMiracle extends MiracleSpell {

    public ParalysisMiracle() {
        super("paralysis");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 15);
    }

    @Override
    public float getMentalCost() {
        return 6.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.LIGHTNING;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double reach = (caster instanceof Player player)
                ? player.getAttributeValue(ForgeMod.BLOCK_REACH.get())
                : 4.5D;

        LivingEntity target = getTargetInLook(level, caster, reach);

        if (target != null) {
            int baseDurationTicks = 80;

            int faith = caster.getCapability(StatCapabilityProvider.STAT_CAPABILITY)
                    .map(StatCapability::getFaith)
                    .orElse(0);

            float faithBonusMultiplier = 1.0f + (faith * 0.01f);
            float dedicationMultiplier = getDedicationMultiplier(caster);
            int finalDuration = Math.round(baseDurationTicks * faithBonusMultiplier * dedicationMultiplier);

            target.addEffect(new MobEffectInstance(GenesisEffects.PARALYSIS.get(), finalDuration, 0));
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