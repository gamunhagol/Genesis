package com.gamunhagol.genesismod.content.magic.miracles.earth;

import com.gamunhagol.genesismod.api.DamageSnapshot;
import com.gamunhagol.genesismod.api.StatType;
import com.gamunhagol.genesismod.content.magic.MiracleElement;
import com.gamunhagol.genesismod.content.magic.MiracleSpell;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.client.PacketSyncPulledRockState;
import com.gamunhagol.genesismod.stats.WeaponRequirementHelper;
import com.gamunhagol.genesismod.world.capability.projectile.ProjectileStatsProvider;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.LargeRockEntity;
import com.gamunhagol.genesismod.world.entity.projectile.miracles.child.RockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class EarthExtractMiracle extends MiracleSpell {
    private static final String TAG_HAS_ROCK = "GenesisHasPulledRock";
    private static final String TAG_IS_LARGE = "GenesisIsLargeRock";
    private static final int MAX_CHARGE_TICKS = 70;

    public EarthExtractMiracle() {
        super("earth_extract");
    }

    @Override
    public int getMaxChargeTicks() {
        return MAX_CHARGE_TICKS;
    }

    @Override
    public boolean isChargeable(LivingEntity caster) {
        return !caster.getPersistentData().getBoolean(TAG_HAS_ROCK);
    }

    @Override
    public boolean isChargePhase(LivingEntity caster) {
        return isChargeable(caster);
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        if (caster.getPersistentData().getBoolean(TAG_HAS_ROCK)) {
            return true;
        }
        return super.canCast(caster);
    }

    @Override
    public Map<StatType, Integer> getRequiredStats() {
        return Map.of(
                StatType.FAITH, 20,
                StatType.STRENGTH, 24
        );
    }

    @Override
    public float getMentalCost() {
        return 9.5f;
    }

    @Override
    public int getMemoryCost() {
        return 1;
    }

    @Override
    public MiracleElement getElement() {
        return MiracleElement.EARTH;
    }

    @Override
    protected DamageSnapshot calculateSpellSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 14.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 1.2f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }

    protected DamageSnapshot calculateLargeRockSnapshot(LivingEntity caster, DamageSnapshot catalyst) {
        float basePhysical = 30.0f;
        float finalPhysical = (basePhysical + (catalyst.physical() * 1.3f)) * getDedicationMultiplier(caster);
        return new DamageSnapshot(finalPhysical, 0, 0, 0, 0, 0, 0);
    }


    @Override
    protected void onExecuteCharged(Level level, LivingEntity caster, DamageSnapshot spellSnapshot, int chargeTicks) {
        if (level.isClientSide) return;

        CompoundTag tag = caster.getPersistentData();
        float ratio = (float) chargeTicks / (float) getMaxChargeTicks();
        boolean isLarge = (ratio >= 0.95f);

        tag.putBoolean(TAG_HAS_ROCK, true);
        tag.putBoolean(TAG_IS_LARGE, isLarge);

        if (caster instanceof ServerPlayer serverPlayer) {
            GenesisNetwork.sendToPlayer(new PacketSyncPulledRockState(true, isLarge), serverPlayer);
        }
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (level.isClientSide) return;

        CompoundTag tag = caster.getPersistentData();

        if (tag.getBoolean(TAG_HAS_ROCK)) {
            boolean isLarge = tag.getBoolean(TAG_IS_LARGE);

            tag.remove(TAG_HAS_ROCK);
            tag.remove(TAG_IS_LARGE);

            if (caster instanceof ServerPlayer serverPlayer) {
                GenesisNetwork.sendToPlayer(new PacketSyncPulledRockState(false, false), serverPlayer);
            }

            Vec3 look = caster.getLookAngle();
            DamageSnapshot catalyst = WeaponRequirementHelper.calculateTotalDamage(caster, caster.getMainHandItem(), 0f);

            if (isLarge) {
                DamageSnapshot largeSnapshot = calculateLargeRockSnapshot(caster, catalyst);
                LargeRockEntity largeRock = new LargeRockEntity(level, caster, largeSnapshot);
                largeRock.setPos(caster.getX(), caster.getEyeY() + 0.3D, caster.getZ());
                largeRock.setDeltaMovement(look.scale(0.85D));

                largeRock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(largeSnapshot));
                level.addFreshEntity(largeRock);
            } else {
                RockEntity rock = new RockEntity(level, caster, spellSnapshot);
                rock.setPos(caster.getX(), caster.getEyeY() + 0.2D, caster.getZ());
                rock.setDeltaMovement(look.scale(0.9D));

                rock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
                level.addFreshEntity(rock);
            }
        }
    }
}