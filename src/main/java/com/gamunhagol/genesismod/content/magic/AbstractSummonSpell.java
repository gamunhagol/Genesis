package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import com.gamunhagol.genesismod.world.entity.base.SummonHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public abstract class AbstractSummonSpell extends MagicSpell {

    public AbstractSummonSpell(String id) {
        super(id);
    }


    @Override
    protected DamageSnapshot calculateSpellSnapshot(DamageSnapshot catalystSnapshot) {
        return DamageSnapshot.EMPTY;
    }

    protected double getDamageScaleRatio() {
        return 1.0D;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel) || !(caster instanceof Player player)) return;

        Mob summonEntity = createSummonEntity(serverLevel, player);
        if (summonEntity instanceof ISummonable summonable) {

            Vec3 look = player.getLookAngle();
            double distance = 1.5;
            double spawnX = player.getX() + (look.x * distance);
            double spawnY = player.getY();
            double spawnZ = player.getZ() + (look.z * distance);

            summonEntity.setPos(spawnX, spawnY, spawnZ);

            summonable.setOwnerUUID(player.getUUID());

            int arcaneLevel = WeaponRequirementHelper.getEntityStat(player, StatType.ARCANE);
            SummonHelper.applyArcaneScaling(summonEntity, arcaneLevel, getDamageScaleRatio());

            serverLevel.addFreshEntity(summonEntity);

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.addSummon(summonEntity.getUUID(), summonable.getUpkeepCost());
            });
        }
    }

    protected abstract Mob createSummonEntity(ServerLevel level, Player caster);
}