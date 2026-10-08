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

public class SeaSerpentBreathMiracle extends MiracleSpell {
    public SeaSerpentBreathMiracle() {
        super("sea_serpent_breath");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 17);
    }

    @Override
    public float getMentalCost() {
        return 5.0f;
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
        return 0.14f;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 2.0f;
        float finalPhysical = (basePhysical + (catalyst.holy() * 0.35f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        playCastSound(level, caster);
        for (int i = 0; i < 4; i++) {
            shootDrop(level, caster, spellSnapshot, 1.5D);
        }
    }

    @Override
    protected void onExecuteContinuous(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int ticksUsing) {
        if (ticksUsing % 3 == 0) {
            playCastSound(level, caster);
        }
        for (int i = 0; i < 4; i++) {
            shootDrop(level, caster, spellSnapshot, 1.5D);
        }
    }

    private void playCastSound(Level level, LivingEntity caster) {
        level.playSound(
                null,
                caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE,
                caster.getSoundSource(),
                0.7F,
                0.9F + (level.random.nextFloat() * 0.2F)
        );
    }

    private void shootDrop(Level level, LivingEntity caster, DamageSnapshot snapshot, double speed) {
        Vec3 look = caster.getLookAngle();
        Vec3 spawnPos = caster.getEyePosition().add(look.scale(0.3D));

        WaterDropEntity drop = new WaterDropEntity(level, caster, snapshot);
        drop.setPos(spawnPos.x, spawnPos.y - 0.1D, spawnPos.z);

        drop.setDeltaMovement(look.scale(speed));
        drop.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
        level.addFreshEntity(drop);
    }
}