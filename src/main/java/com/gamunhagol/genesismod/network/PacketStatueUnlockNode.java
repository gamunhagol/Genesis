package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.content.StatueRewardManager;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStatueUnlockNode {
    private final String statueId;
    private final int nodeId;

    public PacketStatueUnlockNode(String statueId, int nodeId) {
        this.statueId = statueId;
        this.nodeId = nodeId;
    }

    public PacketStatueUnlockNode(FriendlyByteBuf buf) {
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

            StatueRewardManager.NodeInfo nodeInfo = StatueRewardManager.getNode(this.statueId, this.nodeId);
            if (nodeInfo == null) return;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                if (cap.isNodeUnlocked(this.statueId, this.nodeId)) return;

                for (int requiredId : nodeInfo.requiredNodes) {
                    if (!cap.isNodeUnlocked(this.statueId, requiredId)) {
                        return;
                    }
                }

                int totalFound = 0;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.is(nodeInfo.costItem)) {
                        totalFound += stack.getCount();
                    }
                }

                if (totalFound >= nodeInfo.costCount) {
                    int toRemove = nodeInfo.costCount;
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack.is(nodeInfo.costItem)) {
                            int shrinkAmount = Math.min(toRemove, stack.getCount());
                            stack.shrink(shrinkAmount);
                            toRemove -= shrinkAmount;
                            if (toRemove <= 0) break;
                        }
                    }
                    cap.unlockNode(this.statueId, this.nodeId);
                    ItemStack reward = new ItemStack(nodeInfo.rewardItem, nodeInfo.rewardCount);
                    if (!player.getInventory().add(reward)) {
                        player.drop(reward, false);
                    }

                    GenesisNetwork.sendToPlayer(new PacketSyncStats(cap), player);
                }
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}