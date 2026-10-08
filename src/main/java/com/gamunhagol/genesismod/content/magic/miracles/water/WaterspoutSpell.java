package com.gamunhagol.genesismod.content.magic.miracles.water;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.WaterspoutEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class WaterspoutSpell extends MiracleSpell {
    public WaterspoutSpell() {
        super("waterspout");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 26);
    }

    @Override
    public float getMentalCost() {
        return 14.5f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WATER;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 5.0f;
        float finalPhysical = (basePhysical + (catalyst.holy() * 0.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        HitResult hit = caster.pick(8.0D, 1.0F, false);
        Vec3 spawnBase = hit.getLocation();

        WaterspoutEntity root = new WaterspoutEntity(
                GenesisEntities.WATERSPOUT.get(),
                level,
                caster,
                spellSnapshot,
                0,
                -1
        );
        root.setPos(spawnBase.x, spawnBase.y, spawnBase.z);
        root.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
        level.addFreshEntity(root);

        int previousParentId = root.getId();
        double heightSpacing = 1.8D;

        for (int i = 1; i <= 3; i++) {
            WaterspoutEntity segment = new WaterspoutEntity(
                    GenesisEntities.WATERSPOUT.get(),
                    level,
                    caster,
                    spellSnapshot,
                    i,
                    previousParentId
            );
            segment.setPos(spawnBase.x, spawnBase.y + (heightSpacing * i), spawnBase.z);
            segment.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(segment);

            previousParentId = segment.getId();
        }
    }
}