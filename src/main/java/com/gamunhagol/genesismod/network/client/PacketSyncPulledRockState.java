package com.gamunhagol.genesismod.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPayloadHandler.handlePulledRockSync(this.hasRock, this.isLarge);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
