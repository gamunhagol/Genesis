package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.SeaOfStarsEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class SeaOfStarsSpell extends MagicSpell {
    public SeaOfStarsSpell() {
        super("sea_of_stars");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.INTELLIGENCE, 35);
    }

    @Override
    public float getMentalCost() {
        return 20.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        SeaOfStarsEntity seaOfStars = new SeaOfStarsEntity(level, caster, spellSnapshot);
        seaOfStars.setPos(caster.getX(), caster.getY(), caster.getZ());
        seaOfStars.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(seaOfStars);

        if (level instanceof ServerLevel serverLevel) {
            spawnLightPillars(serverLevel, seaOfStars.getX(), seaOfStars.getY(), seaOfStars.getZ(), 1.5, 40, GenesisParticles.MAGICAL_LIGHT.get());
        }
    }

    private void spawnLightPillars(ServerLevel level, double centerX, double centerY, double centerZ, double maxRadius, int count, SimpleParticleType particleType) {
        RandomSource random = level.getRandom();

        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radiusRatio = 0.5 + (random.nextDouble() * 0.5);
            double radius = maxRadius * radiusRatio;

            double spawnX = centerX + Math.cos(angle) * radius;
            double spawnZ = centerZ + Math.sin(angle) * radius;
            double spawnY = centerY + (random.nextDouble() * 1.2);
            double speedY = 0.08 + (random.nextDouble() * 0.12);

            level.sendParticles(particleType, spawnX, spawnY, spawnZ, 0, 0.0, speedY, 0.0, 1.0);
        }
    }
}
