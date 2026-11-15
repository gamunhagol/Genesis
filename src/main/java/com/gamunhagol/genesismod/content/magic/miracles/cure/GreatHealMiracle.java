package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.rune.HealRuneEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class GreatHealMiracle extends MiracleSpell {

    public GreatHealMiracle() {
        super("great_heal");
    }

    @Override
    public int getMaxChargeTicks() {
        return 50;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return true;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 18);
    }

    @Override
    public float getMentalCost() {
        return 8.4f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float healAmount = 14.0f;
        float efficiency = 0.6f;

        float baseHeal = healAmount + (catalyst.holy() * efficiency);
        float multiplier = getDedicationMultiplier(caster);
        float finalHeal = baseHeal * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;
        applyEffect(level, caster, spellSnapshot.holy());
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        if (level.isClientSide) return;

        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float multiplier = (ratio > 0.70f) ? 1.25f : 1.0f;

        applyEffect(level, caster, spellSnapshot.holy() * multiplier);
    }

    private void applyEffect(Level level, LivingEntity caster, float healAmount) {
        BlockPos center = caster.blockPosition();
        AABB searchArea = new AABB(center).inflate(1.5D, 1.0D, 1.5D);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, searchArea, Entity::isAlive);

        for (LivingEntity target : targets) {
            BlockPos targetPos = target.blockPosition();
            int dx = Math.abs(targetPos.getX() - center.getX());
            int dz = Math.abs(targetPos.getZ() - center.getZ());

            if (dx + dz <= 1) {
                if (target.isInvertedHealAndHarm()) {
                    target.hurt(GenesisDamageTypes.getSource(level, GenesisDamageTypes.HOLY, caster), healAmount);
                } else {
                    target.heal(healAmount);
                }
            }
        }

        HealRuneEntity rune = new HealRuneEntity(GenesisEntities.HEAL_RUNE.get(), level);
        rune.setPos(caster.getX(), caster.getY(), caster.getZ());
        rune.setRuneType(1);
        rune.setMaxLife(40);
        level.addFreshEntity(rune);

        if (level instanceof ServerLevel serverLevel) {
            double runeRadius = 1.5D;
            spawnRuneParticles(serverLevel, caster.getX(), caster.getY(), caster.getZ(), runeRadius, 35, GenesisParticles.DIVINE_LIGHT.get());
        }
    }

    private void spawnRuneParticles(ServerLevel level, double centerX, double centerY, double centerZ, double runeRadius, int count, SimpleParticleType particleType) {
        RandomSource random = level.getRandom();

        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radiusRatio = (random.nextDouble() < 0.7)
                    ? 0.8 + (random.nextDouble() * 0.2)
                    : Math.sqrt(random.nextDouble()) * 0.8;
            double radius = runeRadius * radiusRatio;

            double spawnX = centerX + Math.cos(angle) * radius;
            double spawnZ = centerZ + Math.sin(angle) * radius;
            double spawnY = centerY + 0.05 + (random.nextDouble() * 0.3);
            double speedY = 0.05 + (random.nextDouble() * 0.08);

            level.sendParticles(particleType, spawnX, spawnY, spawnZ, 0, 0.0, speedY, 0.0, 1.0);
        }
    }
}
