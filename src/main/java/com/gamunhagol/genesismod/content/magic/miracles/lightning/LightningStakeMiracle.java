package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LightningStakeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class LightningStakeMiracle extends MiracleSpell {

    public LightningStakeMiracle() {
        super("lightning_stake");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 19);
    }

    @Override
    public float getMentalCost() {
        return 7.4f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.LIGHTNING;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseLightning = 16.0f;
        float finalLightning = (baseLightning + (catalyst.lightning() * 0.5f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, 0, finalLightning, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        Vec3 downwardDir = new Vec3(0, -1, 0);

        LightningStakeEntity stake = new LightningStakeEntity(level, caster, spellSnapshot, downwardDir);
        stake.setPos(caster.getX(), caster.getY() + 1.2D, caster.getZ());
        stake.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(stake);
    }
}