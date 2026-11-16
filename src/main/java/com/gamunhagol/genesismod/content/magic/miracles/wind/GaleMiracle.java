package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 eyePos = caster.getEyePosition();
        Vec3 look = caster.getLookAngle().normalize();

        spawnGaleParticles(serverLevel, eyePos, look, chargeRatio);

        double pushStrength;
        if (chargeRatio <= 0.10F) {
            pushStrength = 5.0D;
        } else if (chargeRatio <= 0.50F) {
            pushStrength = 8.0D;
        } else {
            pushStrength = 15.0D;
        }

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

    private void spawnGaleParticles(ServerLevel level, Vec3 eyePos, Vec3 look, float chargeRatio) {
        RandomSource random = level.getRandom();
        Vec3 startOrigin = eyePos.add(look.scale(0.8D));

        int smokeCount = 20 + (int) (chargeRatio * 25);
        for (int i = 0; i < smokeCount; i++) {
            double offsetX = (random.nextDouble() - 0.5D) * 1.4D;
            double offsetY = (random.nextDouble() - 0.5D) * 1.4D;
            double offsetZ = (random.nextDouble() - 0.5D) * 1.4D;

            double vx = (random.nextDouble() - 0.5D) * 0.1D;
            double vy = (random.nextDouble() - 0.5D) * 0.1D;
            double vz = (random.nextDouble() - 0.5D) * 0.1D;

            level.sendParticles(
                    ParticleTypes.CLOUD,
                    startOrigin.x + offsetX,
                    startOrigin.y + offsetY,
                    startOrigin.z + offsetZ,
                    0,
                    vx, vy, vz,
                    1.0D
            );
        }

        int campfireCount = 15 + (int) (chargeRatio * 30);
        double baseSpeed = 0.4D + (chargeRatio * 1.2D);

        for (int i = 0; i < campfireCount; i++) {
            double offsetX = (random.nextDouble() - 0.5D) * 0.6D;
            double offsetY = (random.nextDouble() - 0.5D) * 0.6D;
            double offsetZ = (random.nextDouble() - 0.5D) * 0.6D;

            double spread = 0.25D;
            Vec3 dir = look.add(
                    (random.nextDouble() - 0.5D) * spread,
                    (random.nextDouble() - 0.5D) * spread,
                    (random.nextDouble() - 0.5D) * spread
            ).normalize();

            double speed = baseSpeed * (0.8D + random.nextDouble() * 0.4D);

            level.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    startOrigin.x + offsetX,
                    startOrigin.y + offsetY,
                    startOrigin.z + offsetZ,
                    0,
                    dir.x, dir.y, dir.z,
                    speed
            );
        }
    }
}
