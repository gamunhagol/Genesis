package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LightningSpearEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class LightningSpearMiracle extends MiracleSpell {
    private static final String CHARGING_SPEAR_UUID_KEY = "GenesisChargingSpearUUID";

    public LightningSpearMiracle() {
        super("lightning_spear");
    }

    @Override
    public int getMaxChargeTicks() {
        return 100;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return true;
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return true;
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(StatType.FAITH, 16);
    }

    @Override
    public float getMentalCost() {
        return 4.9f;
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
        float baseLightning = 12.0f;
        float finalLightning = (baseLightning + (catalyst.lightning() * 0.4f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, 0, finalLightning, 0, 0, 0);
    }

    @Override
    protected void onChargeTick(Level level, LivingEntity caster, int chargeTicks) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 holdPos = getCatalystHandPosition(caster, 0.8D, 0.35D, -0.15D);
        LightningSpearEntity spear = getChargingSpear(serverLevel, caster);

        if (spear == null || !spear.isAlive() || !spear.isCharging()) {
            spear = new LightningSpearEntity(level, caster, DamageSnapshot.EMPTY, Vec3.ZERO);
            spear.setCharging(true);
            spear.setPos(holdPos.x, holdPos.y, holdPos.z);
            level.addFreshEntity(spear);
            caster.getPersistentData().putUUID(CHARGING_SPEAR_UUID_KEY, spear.getUUID());
        } else {
            spear.setPos(holdPos.x, holdPos.y, holdPos.z);
        }
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        shootSpear(level, caster, spellSnapshot);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());
        float multiplier = (ratio > 0.5f) ? 1.35f : 1.0f;

        DamageSnapshot chargedSnapshot = new DamageSnapshot(
                0, 0, 0, spellSnapshot.lightning() * multiplier, 0, 0, 0
        );
        shootSpear(level, caster, chargedSnapshot);
    }

    private void shootSpear(Level level, LivingEntity caster, DamageSnapshot snapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 look = caster.getLookAngle();
        Vec3 releasePos = getCatalystHandPosition(caster, 0.8D, 0.35D, -0.15D);

        LightningSpearEntity spear = getChargingSpear(serverLevel, caster);
        caster.getPersistentData().remove(CHARGING_SPEAR_UUID_KEY);

        if (spear != null && spear.isAlive() && spear.isCharging()) {
            spear.setPos(releasePos.x, releasePos.y, releasePos.z);
            spear.releaseSpear(snapshot, look);
        } else {
            LightningSpearEntity newSpear = new LightningSpearEntity(level, caster, snapshot, look);
            newSpear.setPos(releasePos.x, releasePos.y, releasePos.z);
            newSpear.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
            level.addFreshEntity(newSpear);
        }
    }

    private LightningSpearEntity getChargingSpear(ServerLevel level, LivingEntity caster) {
        CompoundTag data = caster.getPersistentData();
        if (!data.hasUUID(CHARGING_SPEAR_UUID_KEY)) return null;

        UUID uuid = data.getUUID(CHARGING_SPEAR_UUID_KEY);
        Entity entity = level.getEntity(uuid);
        if (entity instanceof LightningSpearEntity spear) {
            return spear;
        }
        return null;
    }
}
