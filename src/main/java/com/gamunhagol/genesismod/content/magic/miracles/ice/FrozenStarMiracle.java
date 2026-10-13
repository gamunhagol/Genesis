package com.gamunhagol.genesismod.content.magic.miracles.ice;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.FrozenStarEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FrozenStarMiracle extends MiracleSpell {

    public FrozenStarMiracle() {
        super("frozen_star");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 28);
    }

    @Override
    public float getMentalCost() {
        return 15.4f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.COLD;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float dedicationMul = getDedicationMultiplier(caster);

        float basePhysical = 16.0f;
        float baseFrost = 14.0f;

        float finalPhysical = (basePhysical + (catalyst.physical() * 0.8f)) * dedicationMul;
        float finalFrost = (baseFrost + (catalyst.frost() * 0.7f)) * dedicationMul;

        return new DamageSnapshot(finalPhysical, 0, 0, 0, finalFrost, 0, 0);
    }

    protected DamageSnapshot calculateAoeColdSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float dedicationMul = getDedicationMultiplier(caster);

        float baseAoeFrost = 10.0f;
        float finalAoeFrost = (baseAoeFrost + (catalyst.frost() * 0.5f)) * dedicationMul;

        return new DamageSnapshot(0, 0, 0, 0, finalAoeFrost, 0, 0);
    }

    protected float calculateShockwaveDamage(DamageSnapshot directSnapshot) {
        return directSnapshot.physical() * 0.45F;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        HitResult hitResult = caster.pick(14.0D, 1.0F, false);
        Vec3 targetPos = hitResult.getLocation();

        DamageSnapshot catalyst = caster.getMainHandItem().isEmpty() ? DamageSnapshot.EMPTY : calculateSpellSnapshot(caster, DamageSnapshot.EMPTY);
        DamageSnapshot aoeSnapshot = calculateAoeColdSnapshot(caster, catalyst);
        float shockwaveDamage = calculateShockwaveDamage(spellSnapshot);

        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = targetPos.subtract(look.scale(5.0D)).add(0.0D, 11.0D, 0.0D);

        Vec3 fallDir = targetPos.subtract(spawnPos).normalize();
        double speed = 1.35D;

        FrozenStarEntity star = new FrozenStarEntity(level, caster, spellSnapshot, aoeSnapshot, shockwaveDamage);
        star.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        star.setDeltaMovement(fallDir.scale(speed));

        level.addFreshEntity(star);
    }
}