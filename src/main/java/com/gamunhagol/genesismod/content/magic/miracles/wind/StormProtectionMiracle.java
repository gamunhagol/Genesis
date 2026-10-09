package com.gamunhagol.genesismod.content.magic.miracles.wind;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.WindWallEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class StormProtectionMiracle extends MiracleSpell {
    public StormProtectionMiracle() {
        super("storm_protection");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 17);
    }

    @Override
    public float getMentalCost() {
        return 8.0f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.WIND;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!level.isClientSide) {
            Vec3 look2D = new Vec3(caster.getLookAngle().x, 0, caster.getLookAngle().z).normalize();
            Vec3 spawnPos = caster.position().add(look2D.scale(2.0D));

            WindWallEntity windWall = new WindWallEntity(GenesisEntities.WIND_WALL.get(), level);
            windWall.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            windWall.setup(look2D, 200);

            level.addFreshEntity(windWall);
        }
    }
}