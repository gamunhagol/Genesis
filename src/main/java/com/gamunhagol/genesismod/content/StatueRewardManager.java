package com.gamunhagol.genesismod.content;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class StatueRewardManager {

    public static class NodeInfo {
        public final int id;
        public final int x, y;
        public final Item costItem;
        public final int costCount;
        public final Item rewardItem;
        public final int rewardCount;
        public final int[] requiredNodes;

        public NodeInfo(int id, int x, int y, Item costItem, int costCount, Item rewardItem, int rewardCount, int... requiredNodes) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.costItem = costItem;
            this.costCount = costCount;
            this.rewardItem = rewardItem;
            this.rewardCount = rewardCount;
            this.requiredNodes = requiredNodes != null ? requiredNodes : new int[0];
        }
    }

    public static List<NodeInfo> getNodesForStatue(String statueId) {
        List<NodeInfo> nodes = new ArrayList<>();

        // 배경 크기가 2000x2000이므로 중앙은 (1000, 1000) 입니다.
        // 시작 노드(1번)를 화면 중앙에서 약간 아래인 (1000, 1100)으로 배치합니다.

        if ("god_a".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.DIAMOND, 1));
            nodes.add(new NodeInfo(2, 900, 960, GenesisItems.FABRICATED_STAR.get(), 3, Items.EMERALD, 2, 1));
            nodes.add(new NodeInfo(3, 1100, 960, GenesisItems.FABRICATED_STAR.get(), 3, Items.EMERALD, 2, 2));

            // nodes.add(new NodeInfo(4, 1000, 800, Items.DIAMOND, 2, Items.NETHERITE_INGOT, 1, 3));
        }
        else if ("god_b".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1, 1));
        }

        else if ("god_c".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_d".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_e".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_f".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_g".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_h".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, GenesisItems.FABRICATED_STAR.get(), 1, GenesisItems.LITTLE_HEAL.get(), 1));
            // nodes.add(new NodeInfo(2, 1000, 900, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
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