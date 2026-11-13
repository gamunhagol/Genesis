package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.init.GenesisParticles;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.magic.child.MagicDomainEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Map;

public class MagicDomainSpell extends MagicSpell {
    public MagicDomainSpell() {
        super("magic_domain");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 20);
    }

    @Override
    public float getMentalCost() {
        return 10.0f;
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
        MagicDomainEntity domain = new MagicDomainEntity(level, caster, spellSnapshot);
        domain.setPos(caster.getX(), caster.getY(), caster.getZ());
        domain.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(domain);

        if (level instanceof ServerLevel serverLevel) {
            spawnLightPillars(serverLevel, domain.getX(), domain.getY(), domain.getZ(), 1.0, 25, GenesisParticles.MAGICAL_LIGHT.get());
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
