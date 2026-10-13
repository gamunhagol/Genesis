package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class LightningStrikeMiracle extends MiracleSpell {

    public LightningStrikeMiracle() {
        super("lightning_strike");
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 13);
    }

    @Override
    public float getMentalCost() {
        return 4.1f;
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
        return DamageSnapshot.EMPTY;
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double reach = 12.0D;
        HitResult hitResult = caster.pick(reach, 1.0F, false);
        Vec3 hitPos = hitResult.getLocation();

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
        if (bolt != null) {
            bolt.moveTo(hitPos.x, hitPos.y, hitPos.z);
            if (caster instanceof ServerPlayer serverPlayer) {
                bolt.setCause(serverPlayer);
            }
            serverLevel.addFreshEntity(bolt);
        }
    }
}
