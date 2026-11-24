package com.gamunhagol.genesismod.content.magic.miracles.lightning;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LightningArrowEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class LightningArrowMiracle extends MiracleSpell {
    private static final String CHARGING_ARROW_UUID_KEY = "GenesisChargingArrowUUID";

    public LightningArrowMiracle() {
        super("lightning_arrow");
    }

    @Override
    public int getMaxChargeTicks() {
        return 70;
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
        return Map.of(
                StatType.FAITH, 15,
                StatType.STRENGTH, 12
        );
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
        return MiracleElement.LIGHTNING;
    }

    @Override
    protected boolean spawnEmblemOnChargeStart() {
        return true;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float baseLightning = 14.0f;
        float finalLightning = (baseLightning + (catalyst.physical() * 0.7f) + (catalyst.lightning() * 0.6f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(0, 0, 0, finalLightning, 0, 0, 0);
    }

    @Override
    protected void onChargeTick(Level level, LivingEntity caster, int chargeTicks) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 holdPos = getCatalystHandPosition(caster, 0.8D, 0.35D, -0.15D);
        LightningArrowEntity arrow = getChargingArrow(serverLevel, caster);

        if (arrow == null || !arrow.isAlive() || !arrow.isCharging()) {
            arrow = new LightningArrowEntity(level, caster, DamageSnapshot.EMPTY, Vec3.ZERO);
            arrow.setCharging(true);
            arrow.setPos(holdPos.x, holdPos.y, holdPos.z);
            level.addFreshEntity(arrow);
            caster.getPersistentData().putUUID(CHARGING_ARROW_UUID_KEY, arrow.getUUID());
        } else {
            arrow.setPos(holdPos.x, holdPos.y, holdPos.z);
        }
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        DamageSnapshot snapshot = new DamageSnapshot(
                0, 0, 0, spellSnapshot.lightning() * 0.75f, 0, 0, 0
        );
        shootArrow(level, caster, snapshot, 1.0D, 0.0F);
    }

    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        float ratio = Math.min(1.0f, (float) chargeTicks / (float) getMaxChargeTicks());

        float damageMultiplier;
        if (ratio >= 1.0f) {
            damageMultiplier = 1.35f;
        } else if (ratio >= 0.8f) {
            damageMultiplier = 1.25f;
        } else if (ratio >= 0.55f) {
            damageMultiplier = 1.0f;
        } else if (ratio > 0.25f) {
            damageMultiplier = 1.0f;
        } else {
            damageMultiplier = 0.75f;
        }

        double speed = 1.0D + (ratio * 2.3D);

        DamageSnapshot chargedSnapshot = new DamageSnapshot(
                0, 0, 0, spellSnapshot.lightning() * damageMultiplier, 0, 0, 0
        );

        shootArrow(level, caster, chargedSnapshot, speed, ratio);
    }

    private void shootArrow(Level level, LivingEntity caster, DamageSnapshot snapshot, double speed, float ratio) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 look = caster.getLookAngle();
        Vec3 motion = look.normalize().scale(speed);
        Vec3 releasePos = getCatalystHandPosition(caster, 0.8D, 0.35D, -0.15D);

        LightningArrowEntity arrow = getChargingArrow(serverLevel, caster);
        caster.getPersistentData().remove(CHARGING_ARROW_UUID_KEY);

        if (arrow != null && arrow.isAlive() && arrow.isCharging()) {
            arrow.setPos(releasePos.x, releasePos.y, releasePos.z);
            arrow.setChargeRatio(ratio);
            arrow.releaseArrow(snapshot, motion);
        } else {
            LightningArrowEntity newArrow = new LightningArrowEntity(level, caster, snapshot, motion);
            newArrow.setPos(releasePos.x, releasePos.y, releasePos.z);
            newArrow.setChargeRatio(ratio);
            newArrow.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(snapshot));
            level.addFreshEntity(newArrow);
        }
    }

    private LightningArrowEntity getChargingArrow(ServerLevel level, LivingEntity caster) {
        CompoundTag data = caster.getPersistentData();
        if (!data.hasUUID(CHARGING_ARROW_UUID_KEY)) return null;

        UUID uuid = data.getUUID(CHARGING_ARROW_UUID_KEY);
        Entity entity = level.getEntity(uuid);
        if (entity instanceof LightningArrowEntity arrow) {
            return arrow;
        }
        return null;
    }
}
