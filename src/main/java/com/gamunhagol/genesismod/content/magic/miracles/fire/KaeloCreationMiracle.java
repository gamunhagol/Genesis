package com.gamunhagol.genesismod.content.magic.miracles.fire;

import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.AbstractMiracleSummonSpell;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class KaeloCreationMiracle extends AbstractMiracleSummonSpell {

    public KaeloCreationMiracle() {
        super("kaelo_creation");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 20
        );
    }

    @Override
    public float getMentalCost() {
        return 20.0f;
    }

    @Override
    public int getMemoryCost() {
        return 2;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.FIRE;
    }

    @Override
    protected double getDamageScaleRatio() {
        return 1.2D;
    }

    @Override
    protected Mob createSummonEntity(ServerLevel level, Player caster) {
        IronGolem golem = EntityType.IRON_GOLEM.create(level);
        if (golem != null) {
            golem.setPlayerCreated(true);
        }
        return golem;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, com.gamunhagol.genesismod.api.DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel) || !(caster instanceof Player player)) return;

        Mob summonEntity = createSummonEntity(serverLevel, player);
        if (summonEntity == null) return;

        Vec3 look = player.getLookAngle();
        double distance = 2.0D;
        double spawnX = player.getX() + (look.x * distance);
        double spawnY = player.getY();
        double spawnZ = player.getZ() + (look.z * distance);

        summonEntity.setPos(spawnX, spawnY, spawnZ);

        if (summonEntity instanceof ISummonable summonable) {
            super.onExecute(level, caster, spellSnapshot);
            return;
        }

        serverLevel.addFreshEntity(summonEntity);
    }
}