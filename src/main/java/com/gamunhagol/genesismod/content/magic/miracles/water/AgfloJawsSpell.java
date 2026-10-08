package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class AgfloJawsSpell extends MiracleSpell {
    public AgfloJawsSpell() {
        super("agflo_jaws");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 20);
    }

    @Override
    public float getMentalCost() {
        return 6.0f;
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
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 12.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 0.8f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        Vec3 look = caster.getLookAngle();

        // 시전자 전방 2.5블록, 눈높이 부근 허공 위치[cite: 2]
        Vec3 spawnPos = caster.position().add(0, caster.getEyeHeight() * 0.8D, 0).add(look.scale(2.5D));

        // 시선 방향 기준으로 90도 틀어 가로 방향으로 회전[cite: 2]
        float yaw = (float) Mth.atan2(look.z, look.x) + (float) (Math.PI / 2.0);

        EvokerFangs jaw = new EvokerFangs(level, spawnPos.x, spawnPos.y, spawnPos.z, yaw, 0, caster);
        jaw.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(jaw);
    }
}