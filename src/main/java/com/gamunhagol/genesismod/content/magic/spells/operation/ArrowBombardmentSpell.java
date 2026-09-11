package com.gamunhagol.genesismod.content.magic.spells.operation;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MagicSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.etc.ArrowBarrageSpawnerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ArrowBombardmentSpell extends MagicSpell {

    public ArrowBombardmentSpell() {
        super("arrow_bombardment");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.INTELLIGENCE, 24);
    }

    @Override
    public float getMentalCost() {
        return 10.0f;
    }

    @Override
    public int getMemoryCost() {
        return 3;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalystSnapshot) {
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 eyePos = caster.getEyePosition();
        Vec3 lookVec = caster.getLookAngle();

        Vec3 spawnCenter = eyePos.add(0, 1.5D, 0).add(lookVec.scale(1.0D));

        ArrowBarrageSpawnerEntity spawner = new ArrowBarrageSpawnerEntity(
                GenesisEntities.ARROW_BARRAGE_SPAWNER.get(),
                serverLevel
        );
        spawner.setPos(spawnCenter.x, spawnCenter.y, spawnCenter.z);
        spawner.setup(caster, ArrowBarrageSpawnerEntity.Mode.BOMBARDMENT, lookVec);

        serverLevel.addFreshEntity(spawner);
        serverLevel.playSound(null, caster.getX(), caster.getY(), caster.getZ(),
                SoundEvents.DISPENSER_LAUNCH, SoundSource.PLAYERS, 1.2F, 0.8F);
    }
}