package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class StormMiracle extends MiracleSpell {

    public StormMiracle() {
        super("storm");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 21);
    }

    @Override
    public float getMentalCost() {
        return 12.3f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 3.0f;
        float finalPhysical = (basePhysical + (catalyst.holy() * 0.35f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        HitResult hit = caster.pick(12.0D, 1.0F, false);
        Vec3 spawnPos = (hit.getType() == HitResult.Type.BLOCK)
                ? hit.getLocation()
                : caster.position().add(caster.getLookAngle().scale(4.0D));

        StormSegment1Entity seg1 = new StormSegment1Entity(level, caster, spellSnapshot);
        seg1.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        seg1.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(seg1);

        StormSegment2Entity seg2 = new StormSegment2Entity(level, caster, spellSnapshot, seg1.getId());
        seg2.setPos(spawnPos.x, spawnPos.y + 1.5D, spawnPos.z);
        level.addFreshEntity(seg2);

        StormSegment3Entity seg3 = new StormSegment3Entity(level, caster, spellSnapshot, seg2.getId());
        seg3.setPos(spawnPos.x, spawnPos.y + 3.0D, spawnPos.z);
        level.addFreshEntity(seg3);

        StormSegment4Entity seg4 = new StormSegment4Entity(level, caster, spellSnapshot, seg3.getId());
        seg4.setPos(spawnPos.x, spawnPos.y + 4.5D, spawnPos.z);
        level.addFreshEntity(seg4);
    }
}