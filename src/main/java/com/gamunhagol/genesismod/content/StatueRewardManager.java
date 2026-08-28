package com.gamunhagol.genesismod.content;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class StatueRewardManager {

    public static class NodeInfo {
        public final int id;
        public final int x, y;
        public final Item costItem;
        public final int costCount;
        public final ItemStack rewardItem;
        public final int[] requiredNodes;

        public NodeInfo(int id, int x, int y, Item costItem, int costCount, ItemStack rewardItem, int... requiredNodes) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.costItem = costItem;
            this.costCount = costCount;
            this.rewardItem = rewardItem;
            this.requiredNodes = requiredNodes != null ? requiredNodes : new int[0];
        }

        public boolean isPassive() {
            return this.rewardItem.isEmpty();
        }
    }

    public static List<NodeInfo> getNodesForStatue(String statueId) {
        List<NodeInfo> nodes = new ArrayList<>();
        Item cost = GenesisItems.FABRICATED_STAR.get();

        if ("god_a".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
            nodes.add(new NodeInfo(3, 800, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),2));
            nodes.add(new NodeInfo(4, 800, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),3));
            nodes.add(new NodeInfo(5, 800, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),4));
            nodes.add(new NodeInfo(6, 800, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(7, 900, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),2));
            nodes.add(new NodeInfo(8, 900, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(9, 1100, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
            nodes.add(new NodeInfo(10, 1200, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(11, 1200, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(12, 1100, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(13, 1100, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),12));
            nodes.add(new NodeInfo(14, 1100, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
            nodes.add(new NodeInfo(15, 1100, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),14));
            nodes.add(new NodeInfo(16, 1000, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
            nodes.add(new NodeInfo(17, 1000, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(18, 1000, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),17));
            nodes.add(new NodeInfo(19, 1000, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 1000, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),20));


        }
        else if ("god_b".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_c".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_d".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_e".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_f".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_g".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }
        else if ("god_h".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, new ItemStack(GenesisItems.LITTLE_HEAL.get(), 1)));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
        }

        return nodes;
    }

    public static NodeInfo getNode(String statueId, int nodeId) {
        for (NodeInfo node : getNodesForStatue(statueId)) {
            if (node.id == nodeId) {
                return node;
            }
        }
        return null;
    }
}