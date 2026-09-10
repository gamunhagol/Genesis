package com.gamunhagol.genesismod.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncGrabState {
    private final boolean isGrabbed;
    private final UUID entityId;

    public PacketSyncGrabState(boolean isGrabbed, UUID entityId) {
        this.isGrabbed = isGrabbed;
        this.entityId = entityId != null ? entityId : new UUID(0L, 0L);
    }

    public PacketSyncGrabState(FriendlyByteBuf buf) {
        this.isGrabbed = buf.readBoolean();
        this.entityId = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isGrabbed);
        buf.writeUUID(this.entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (this.isGrabbed) {
                    player.getPersistentData().putUUID("GenesisGrabbedEntity", this.entityId);
                } else {
                    player.getPersistentData().remove("GenesisGrabbedEntity");
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}