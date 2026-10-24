package com.gamunhagol.genesismod.network.server;

import com.gamunhagol.genesismod.content.StatueRewardManager;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.client.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTogglePassiveNode {
    private final String statueId;
    private final int nodeId;

    public PacketTogglePassiveNode(String statueId, int nodeId) {
        this.statueId = statueId;
        this.nodeId = nodeId;
    }

    public PacketTogglePassiveNode(FriendlyByteBuf buf) {
        this.statueId = buf.readUtf(256);
        this.nodeId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.statueId);
        buf.writeInt(this.nodeId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                if (cap.isNodeUnlocked(this.statueId, this.nodeId)) {
                    cap.toggleNodeActive(this.statueId, this.nodeId);

                    float pitch = cap.isNodeActive(this.statueId, this.nodeId) ? 1.2F : 0.8F;
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 0.4F, pitch);

                    GenesisNetwork.sendToPlayer(new PacketSyncStats(cap), player);
                }
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
