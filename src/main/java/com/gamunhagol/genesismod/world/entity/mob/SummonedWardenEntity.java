package com.gamunhagol.genesismod.world.entity.mob;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.world.entity.base.ISummonable;
import com.gamunhagol.genesismod.world.entity.base.SummonHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SummonedWardenEntity extends Warden implements ISummonable {

    private UUID ownerUUID;

    private final float upkeepCost = 0.005f;

    public SummonedWardenEntity(EntityType<? extends Warden> type, Level level) {
        super(type, level);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Override
    public float getUpkeepCost() {
        return this.upkeepCost;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.ownerUUID != null) {
            compound.putUUID("SummonOwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("SummonOwnerUUID")) {
            this.ownerUUID = compound.getUUID("SummonOwnerUUID");
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult result = SummonHelper.handleInteraction(this, player, hand);
        if (result.consumesAction()) {
            return result;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && this.ownerUUID != null) {
            Player owner = this.level().getPlayerByUUID(this.ownerUUID);
            if (owner != null) {
                owner.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    stats.removeSummon(this.getUUID());
                });
            }
        }
        super.remove(reason);
    }
}