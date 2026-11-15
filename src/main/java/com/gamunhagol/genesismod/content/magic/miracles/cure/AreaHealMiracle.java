package com.gamunhagol.genesismod.content.magic.miracles.cure;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.damagesource.GenesisDamageTypes;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.rune.HealRuneEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class AreaHealMiracle extends MiracleSpell {

    public AreaHealMiracle() {
        super("area_heal");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 6.8f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float healAmount = 4.0f;
        float efficiency = 0.3f;

        float baseHeal = healAmount + (catalyst.holy() * efficiency);
        float multiplier = getDedicationMultiplier(caster);
        float finalHeal = baseHeal * multiplier;

        return new DamageSnapshot(0, 0, 0, 0, 0, finalHeal, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        double radius = 2.5D;
        AABB area = new AABB(
                caster.getX() - radius, caster.getY() - 1.0D, caster.getZ() - radius,
                caster.getX() + radius, caster.getY() + 2.5D, caster.getZ() + radius
        );
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area, Entity::isAlive);

        float amount = spellSnapshot.holy();
        for (LivingEntity target : targets) {
            double dx = target.getX() - caster.getX();
            double dz = target.getZ() - caster.getZ();
            if ((dx * dx + dz * dz) > radius * radius) continue;

            if (target.isInvertedHealAndHarm()) {
                target.hurt(GenesisDamageTypes.getSource(level, GenesisDamageTypes.HOLY, caster), amount);
            } else {
                target.heal(amount);
            }
        }

        HealRuneEntity rune = new HealRuneEntity(GenesisEntities.HEAL_RUNE.get(), level);
        rune.setPos(caster.getX(), caster.getY(), caster.getZ());
        rune.setRuneType(0);
        rune.setMaxLife(40);
        level.addFreshEntity(rune);

        if (level instanceof ServerLevel serverLevel) {
            spawnRuneParticles(serverLevel, caster.getX(), caster.getY(), caster.getZ(), radius, 30, GenesisParticles.DIVINE_LIGHT.get());
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
