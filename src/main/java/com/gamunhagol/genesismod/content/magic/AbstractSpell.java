package com.gamunhagol.genesismod.content.magic;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.item.weapon.CatalystItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public abstract class AbstractSpell {
    private final String id;

    public AbstractSpell(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescriptionId() {
        return "spell.genesis." + this.id;
    }

    public Component getName() {
        return Component.translatable(getDescriptionId());
    }

    public int getMaxChargeTicks() {
        return 0;
    }

    public int getMinChargeTicks() {
        return 0;
    }

    public abstract Map<StatType, Integer> getRequiredStats();

    public abstract float getMentalCost();
    public abstract boolean canCast(LivingEntity caster);
    public abstract int getMemoryCost();

    public boolean isChargeable(LivingEntity caster) {
        return getMaxChargeTicks() > 0;
    }

    public boolean isChargePhase(LivingEntity caster) {
        return isChargeable(caster);
    }

    public boolean isContinuous(LivingEntity caster) {
        return false;
    }

    public int getContinuousTickInterval() {
        return 1;
    }

    public float getContinuousMentalCost() {
        return 0.0f;
    }

    public void executeContinuousTick(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot, int ticksUsing) {
        if (!level.isClientSide) {
            if (ticksUsing % getContinuousTickInterval() == 0) {
                DamageSnapshot spellSnapshot = calculateSpellSnapshot(caster, catalystSnapshot);
                onExecuteContinuous(level, caster, spellSnapshot, ticksUsing);
                consumeContinuousMental(caster);
            }
        }
    }

    protected void onExecuteContinuous(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int ticksUsing) {
    }

    public void consumeContinuousMental(LivingEntity caster) {
        if (getContinuousMentalCost() <= 0) return;

        if (caster instanceof Player player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(Math.max(0, stats.getMental() - getContinuousMentalCost()));
            });
        }
    }

    public void executeCast(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot) {
        if (!level.isClientSide) {
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(caster, catalystSnapshot);
            onExecute(level, caster, spellSnapshot);
            consumeMental(caster);
        }
    }

    public void executeCastCharged(Level level, LivingEntity caster, DamageSnapshot catalystSnapshot, int chargeTicks) {
        if (!level.isClientSide) {
            DamageSnapshot spellSnapshot = calculateSpellSnapshot(caster, catalystSnapshot);
            onExecuteCharged(level, caster, spellSnapshot, chargeTicks);
            consumeMental(caster);
        }
    }

    protected abstract DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalystSnapshot);

    protected abstract void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot);

    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        onExecute(level, caster, spellSnapshot);
    }

    public void consumeMental(LivingEntity caster) {
        if (caster instanceof Player player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(Math.max(0, stats.getMental() - getMentalCost()));
            });
        }
    }

    protected Vec3 getCatalystHandPosition(LivingEntity caster, double forwardDist, double sideOffset, double heightOffset) {
        Vec3 lookVec = caster.getLookAngle();
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0));
        if (rightVec.lengthSqr() < 1.0E-4D) {
            rightVec = Vec3.directionFromRotation(0.0F, caster.getYRot() + 90.0F);
        } else {
            rightVec = rightVec.normalize();
        }

        boolean isMainHand = caster.getMainHandItem().getItem() instanceof CatalystItem
                || !(caster.getOffhandItem().getItem() instanceof CatalystItem);
        if (caster.isUsingItem()) {
            isMainHand = caster.getUsedItemHand() == InteractionHand.MAIN_HAND;
        }

        boolean isRightHand = isMainHand;
        if (caster instanceof Player player && player.getMainArm() == HumanoidArm.LEFT) {
            isRightHand = !isRightHand;
        }

        double sideMultiplier = isRightHand ? 1.0D : -1.0D;

        return new Vec3(
                caster.getX() + lookVec.x * forwardDist + rightVec.x * sideOffset * sideMultiplier,
                caster.getEyeY() + heightOffset + lookVec.y * forwardDist,
                caster.getZ() + lookVec.z * forwardDist + rightVec.z * sideOffset * sideMultiplier
        );
    }
}
