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
                // 이미 해금되었는지 검사
                if (cap.isNodeUnlocked(this.statueId, this.nodeId)) return;

                // 선행 노드 해금 여부 검사 추가
                for (int requiredId : nodeInfo.requiredNodes) {
                    if (!cap.isNodeUnlocked(this.statueId, requiredId)) {
                        // 선행 노드가 하나라도 해금되지 않았다면 즉시 종료
                        return;
                    }
                }

                // 인벤토리에 아이템이 충분한지 계산
                int totalFound = 0;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.is(nodeInfo.costItem)) {
                        totalFound += stack.getCount();
                    }
                }

                // 아이템이 충분하다면 소모 처리 후 보상 지급
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

                    // 해금 저장
                    cap.unlockNode(this.statueId, this.nodeId);

                    // 보상 지급
                    ItemStack reward = new ItemStack(nodeInfo.rewardItem, nodeInfo.rewardCount);
                    if (!player.getInventory().add(reward)) {
                        player.drop(reward, false);
                    }

                    // 동기화
                    GenesisNetwork.sendToPlayer(new PacketSyncStats(cap), player);
                }
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}