package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.WaterDropEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WaterSprayMiracle extends MiracleSpell {
    public WaterSprayMiracle() {
        super("water_spray");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 10);
    }

    @Override
    public float getMentalCost() {
        return 2.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WATER;
    }

    @Override
    public boolean isContinuous(LivingEntity caster) {
        return true;
    }

    @Override
    public float getContinuousMentalCost() {
        return 0.04f;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 2.0f;
        float finalPhysical = (basePhysical + (catalyst.holy() * 0.2f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        playCastSound(level, caster);
        shootDrop(level, caster, spellSnapshot, 0.85D, 0.08D);
    }

    @Override
    protected void onExecuteContinuous(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int ticksUsing) {
        if (ticksUsing % 4 == 0) {
            playCastSound(level, caster);
        }
        shootDrop(level, caster, spellSnapshot, 0.85D, 0.08D);
    }

    private void playCastSound(Level level, LivingEntity caster) {
        level.playSound(
                null,
                caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE,
                caster.getSoundSource(),
                0.5F,
                1.1F + (level.random.nextFloat() * 0.2F)
        );
    }

    private void shootDrop(Level level, LivingEntity caster, DamageSnapshot snapshot, double speed, double spread) {
        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.getEyePosition().add(look.scale(0.3D));

        WaterDropEntity drop = new WaterDropEntity(level, caster, snapshot);
        drop.setPos(spawnPos.x, spawnPos.y - 0.1D, spawnPos.z);

        Vec3 dir = look.add(
                (level.random.nextDouble() - 0.5D) * spread,
                (level.random.nextDouble() - 0.5D) * spread,
                (level.random.nextDouble() - 0.5D) * spread
        ).normalize();

        drop.setDeltaMovement(dir.scale(speed));
        drop.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        level.addFreshEntity(drop);
    }
}