package com.gamunhagol.genesismod.content.magic.spells.nature;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WardenSonicBoomSpell extends MagicSpell {
    public WardenSonicBoomSpell() { super("warden_sonic_boom"); }

    @Override
    public int getCastTime() { return 30; }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 20);
    }

    @Override
    public float getMentalCost() { return 10.0f; }

    @Override
    public int getMemoryCost() { return 2; }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePower = 10.0f;
        float magicEfficiency = 0.5f;

        float finalMagic = basePower + (catalyst.magic() * magicEfficiency);

        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        LivingEntity target = ShulkerBulletSpell.getTargetInSight(caster, 15.0D);
        if (target == null) return;

        level.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 3.0F, 1.0F);

        Vec3 source = caster.position().add(0.0D, 1.6F, 0.0D);
        Vec3 targetPos = target.position().add(0.0D, target.getBbHeight() / 2.0F, 0.0D);
        Vec3 distanceVec = targetPos.subtract(source);
        Vec3 dir = distanceVec.normalize();

        int steps = Mth.floor(distanceVec.length() * 1.5D);
        for(int i = 1; i < steps; ++i) {
            Vec3 pos = source.add(dir.scale((double)i / 1.5D));
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        if (target.hurt(level.damageSources().sonicBoom(caster), spellSnapshot.magic())) {
            double kbResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            double horizontalKb = 1.2D * (1.0D - kbResistance);
            double verticalKb = 0.5D * (1.0D - kbResistance);

            target.push(dir.x() * horizontalKb, dir.y() * horizontalKb + verticalKb, dir.z() * horizontalKb);
        }
    }
}