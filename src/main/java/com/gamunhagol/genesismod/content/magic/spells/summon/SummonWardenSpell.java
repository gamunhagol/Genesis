package com.gamunhagol.genesismod.content.magic.spells.summon;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractSummonSpell;
import com.gamunhagol.genesismod.world.entity.GenesisEntities;
import com.gamunhagol.genesismod.world.entity.mob.SummonedWardenEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class SummonWardenSpell extends AbstractSummonSpell {

    public SummonWardenSpell() {
        super("summon_warden");
    }

    @Override
    public int getCastTime() {
        return 80;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.ARCANE, 25,
                StatType.INTELLIGENCE, 20
        );
    }

    @Override
    public float getMentalCost() {
        return 20.0f;
    }

    @Override
    public int getMemoryCost() {
        return 4;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (!super.canCast(caster)) {
            return false;
        }

        if (caster instanceof Player player) {
            List<SummonedWardenEntity> existingWardens = player.level().getEntitiesOfClass(
                    SummonedWardenEntity.class,
                    player.getBoundingBox().inflate(128.0D),
                    warden -> player.getUUID().equals(warden.getOwnerUUID())
            );
            return existingWardens.isEmpty();
        }

        return true;
    }

    @Override
    protected void onExecute(net.minecraft.world.level.Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        super.onExecute(level, caster, spellSnapshot);
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        SummonedWardenEntity warden = new SummonedWardenEntity(GenesisEntities.SUMMONED_WARDEN.get(), level);

        warden.setPose(Pose.EMERGING);
        warden.getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, 134L);
        warden.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);

        return warden;
    }
}