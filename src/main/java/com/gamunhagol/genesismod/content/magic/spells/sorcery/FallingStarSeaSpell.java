package com.gamunhagol.genesismod.content.magic.spells.sorcery;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.StarSeaSpawnerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FallingStarSeaSpell extends MagicSpell {

    public FallingStarSeaSpell() {
        super("falling_star_sea");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.INTELLIGENCE, 46);
    }

    @Override
    public float getMentalCost() {
        return 20.0F;
    }

    @Override
    public int getMemoryCost() {
        return 3;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseMagic = 4.5F;
        float finalMagic = baseMagic + (catalyst != null ? catalyst.magic() * 0.45F : 0.0F);
        return new DamageSnapshot(0, finalMagic, 0, 0, 0, 0, 0);
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!level.isClientSide) {
            caster.getPersistentData().putLong("GenesisStarSeaBuffEndTick", level.getGameTime() + 100L);

            DamageSnapshot grandSnapshot = new DamageSnapshot(
                    0, spellSnapshot.magic() * 10.5F, 5.0F, 0, 0, 0, 0
            );

            Vec3 centerPos = caster.position().add(0, 12.0D, 0);

            Vec3 look2D = new Vec3(caster.getLookAngle().x, 0, caster.getLookAngle().z).normalize();
            Vec3 right2D = new Vec3(-look2D.z, 0, look2D.x);

            double distance = 4.5D;

            Vec3[] diamondOffsets = new Vec3[]{
                    look2D.scale(distance),
                    look2D.scale(-distance),
                    right2D.scale(-distance),
                    right2D.scale(distance)
            };

            for (Vec3 offset : diamondOffsets) {
                Vec3 spawnPos = centerPos.add(offset);
                StarSeaSpawnerEntity spawner = new StarSeaSpawnerEntity(GenesisEntities.STAR_SEA_SPAWNER.get(), level);
                spawner.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                spawner.setup(caster, spellSnapshot, grandSnapshot);
                level.addFreshEntity(spawner);
            }
        }
    }
}