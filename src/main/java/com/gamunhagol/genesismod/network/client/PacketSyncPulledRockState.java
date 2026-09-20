package com.gamunhagol.genesismod.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPulledRockState {
    private final boolean hasRock;
    private final boolean isLarge;

    public PacketSyncPulledRockState(boolean hasRock, boolean isLarge) {
        this.hasRock = hasRock;
        this.isLarge = isLarge;
    }

    public PacketSyncPulledRockState(FriendlyByteBuf buf) {
        this.hasRock = buf.readBoolean();
        this.isLarge = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.hasRock);
        buf.writeBoolean(this.isLarge);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                CompoundTag tag = player.getPersistentData();
                if (this.hasRock) {
                    tag.putBoolean("GenesisHasPulledRock", true);
                    tag.putBoolean("GenesisIsLargeRock", this.isLarge);
                } else {
                    tag.remove("GenesisHasPulledRock");
                    tag.remove("GenesisIsLargeRock");
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}