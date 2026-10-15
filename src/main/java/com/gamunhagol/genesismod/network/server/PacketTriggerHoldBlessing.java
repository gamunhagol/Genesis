package com.gamunhagol.genesismod.network.server;

import com.gamunhagol.genesismod.events.common.BlessingEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTriggerHoldBlessing {
    private final BlockPos targetPos;

    public PacketTriggerHoldBlessing(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public PacketTriggerHoldBlessing(FriendlyByteBuf buf) {
        this.targetPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.targetPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                BlessingEvents.executeFireBlessing(player, this.targetPos);
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}
