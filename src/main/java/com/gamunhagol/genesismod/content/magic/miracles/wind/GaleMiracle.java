package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class GaleMiracle extends MiracleSpell {
    private final double range = 15.3D;

    public GaleMiracle() {
        super("gale");
    }

    @Override
    public int getMaxChargeTicks() {
        return 90;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
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
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        executeGale(level, caster, 0.0F);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0F, (float) chargeTicks / (float) Math.max(1, getMaxChargeTicks()));
        executeGale(level, caster, ratio);
    }

    private void executeGale(Level level, LivingEntity caster, float chargeRatio) {
        if (level.isClientSide) return;

        double pushStrength;
        if (chargeRatio <= 0.10F) {
            pushStrength = 5.0D;
        } else if (chargeRatio <= 0.50F) {
            pushStrength = 8.0D;
        } else {
            pushStrength = 15.0D;
        }

        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getLookAngle().normalize();

        AABB searchBox = caster.getBoundingBox().inflate(this.range);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                e -> e != caster && e.isAlive()
        );

        float dedication = getDedicationMultiplier(caster);

        for (LivingEntity target : targets) {
            Vec3 toTarget = target.getEyePosition().subtract(eyePos);
            double distance = toTarget.length();

            if (distance <= this.range && distance > 0.001D) {
                Vec3 targetDir = toTarget.normalize();
                double dot = look.dot(targetDir);

                if (dot > 0.5D) {
                    double falloff = 1.0D - (distance / this.range);
                    double finalStrength = pushStrength * falloff * dedication;

                    Vec3 pushMotion = look.scale(finalStrength);
                    target.setDeltaMovement(target.getDeltaMovement().add(pushMotion));
                    target.hurtMarked = true;
                    target.hasImpulse = true;
                }
            }
        }
    }
}