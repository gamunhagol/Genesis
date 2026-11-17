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
import com.gamunhagol.genesismod.world.item.weapon.CatalystItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class EarthExtractMiracle extends MiracleSpell {
    private static final String TAG_HAS_ROCK = "GenesisHasPulledRock";
    private static final String TAG_IS_LARGE = "GenesisIsLargeRock";
    private static final String TAG_ROCK_UUID = "GenesisPulledRockUUID";
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
        if (!(level instanceof ServerLevel serverLevel)) return;

        CompoundTag tag = caster.getPersistentData();
        float ratio = (float) chargeTicks / (float) getMaxChargeTicks();
        boolean isLarge = (ratio >= 0.95f);

        tag.putBoolean(TAG_HAS_ROCK, true);
        tag.putBoolean(TAG_IS_LARGE, isLarge);

        if (caster instanceof ServerPlayer serverPlayer) {
            GenesisNetwork.sendToPlayer(new PacketSyncPulledRockState(true, isLarge), serverPlayer);
        }

        ItemStack catalystStack = caster.getMainHandItem().getItem() instanceof CatalystItem
                ? caster.getMainHandItem()
                : caster.getOffhandItem();
        DamageSnapshot catalyst = WeaponRequirementHelper.calculateTotalDamage(caster, catalystStack, 0f);

        Entity oldEntity = getHeldRockEntity(serverLevel, tag);
        if (oldEntity != null) {
            oldEntity.discard();
        }

        if (isLarge) {
            DamageSnapshot largeSnapshot = calculateLargeRockSnapshot(caster, catalyst);
            LargeRockEntity largeRock = new LargeRockEntity(level, caster, largeSnapshot);
            largeRock.setHeld(true);
            largeRock.setPos(caster.getX(), caster.getY() + 2.2D, caster.getZ());
            largeRock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(largeSnapshot));
            level.addFreshEntity(largeRock);
            tag.putUUID(TAG_ROCK_UUID, largeRock.getUUID());
        } else {
            RockEntity rock = new RockEntity(level, caster, spellSnapshot);
            rock.setHeld(true);
            rock.setPos(caster.getX(), caster.getY() + 1.8D, caster.getZ());
            rock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(spellSnapshot));
            level.addFreshEntity(rock);
            tag.putUUID(TAG_ROCK_UUID, rock.getUUID());
        }
    }

    @Override
    protected void onExecute(Level level, LivingEntity caster, DamageSnapshot spellSnapshot) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        CompoundTag tag = caster.getPersistentData();

        if (tag.getBoolean(TAG_HAS_ROCK)) {
            boolean isLarge = tag.getBoolean(TAG_IS_LARGE);
            Entity heldEntity = getHeldRockEntity(serverLevel, tag);

            tag.remove(TAG_HAS_ROCK);
            tag.remove(TAG_IS_LARGE);
            tag.remove(TAG_ROCK_UUID);

            if (caster instanceof ServerPlayer serverPlayer) {
                GenesisNetwork.sendToPlayer(new PacketSyncPulledRockState(false, false), serverPlayer);
            }

            Vec3 look = caster.getLookAngle();

            ItemStack catalystStack = caster.getMainHandItem().getItem() instanceof CatalystItem
                    ? caster.getMainHandItem()
                    : caster.getOffhandItem();

            DamageSnapshot catalyst = WeaponRequirementHelper.calculateTotalDamage(caster, catalystStack, 0f);

            if (isLarge) {
                if (heldEntity instanceof LargeRockEntity largeRock && largeRock.isAlive()) {
                    largeRock.releaseRock(look.scale(0.85D));
                } else {
                    DamageSnapshot largeSnapshot = calculateLargeRockSnapshot(caster, catalyst);
                    LargeRockEntity largeRock = new LargeRockEntity(level, caster, largeSnapshot);
                    largeRock.setPos(caster.getX(), caster.getEyeY() + 0.3D, caster.getZ());
                    largeRock.setDeltaMovement(look.scale(0.85D));
                    largeRock.getCapability(ProjectileStatsProvider.CAPABILITY).ifPresent(cap -> cap.setSnapshot(largeSnapshot));
                    level.addFreshEntity(largeRock);
                }
            } else {
                if (heldEntity instanceof RockEntity rock && rock.isAlive()) {
                    rock.releaseRock(look.scale(0.9D));
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

    private Entity getHeldRockEntity(ServerLevel level, CompoundTag tag) {
        if (!tag.hasUUID(TAG_ROCK_UUID)) return null;
        UUID uuid = tag.getUUID(TAG_ROCK_UUID);
        return level.getEntity(uuid);
    }
}
