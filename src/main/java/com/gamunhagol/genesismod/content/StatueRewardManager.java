package com.gamunhagol.genesismod.content;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class StatueRewardManager {

    // 노드의 모든 정보를 담는 그릇 (좌표, 비용, 보상)
    public static class NodeInfo {
        public final int id;
        public final int x, y;
        public final Item costItem;
        public final int costCount;
        public final Item rewardItem;
        public final int rewardCount;

        public NodeInfo(int id, int x, int y, Item costItem, int costCount, Item rewardItem, int rewardCount) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.costItem = costItem;
            this.costCount = costCount;
            this.rewardItem = rewardItem;
            this.rewardCount = rewardCount;
        }
    }

    // UI 렌더링용: 해당 신상의 모든 노드를 리스트로 반환
    public static List<NodeInfo> getNodesForStatue(String statueId) {
        List<NodeInfo> nodes = new ArrayList<>();

        if ("god_a".equals(statueId)) {
            // 구조: NodeInfo(노드번호, X좌표, Y좌표, 요구아이템, 요구수량, 보상아이템, 보상수량)

            nodes.add(new NodeInfo(1, 640, 620, GenesisItems.FABRICATED_STAR.get(), 1, Items.DIAMOND, 1));

            //nodes.add(new NodeInfo(2, 540, 480, Items.GOLD_INGOT, 3, Items.EMERALD, 2));
           // nodes.add(new NodeInfo(3, 740, 480, Items.GOLD_INGOT, 3, Items.EMERALD, 2));

           // nodes.add(new NodeInfo(4, 640, 320, Items.DIAMOND, 2, Items.NETHERITE_INGOT, 1));
        }
        else if ("god_b".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
           // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_c".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_d".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_e".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_f".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_g".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        else if ("god_h".equals(statueId)) {
            nodes.add(new NodeInfo(1, 640, 650, GenesisItems.FABRICATED_STAR.get(), 1, Items.GOLD_INGOT, 1));
            // nodes.add(new NodeInfo(2, 640, 450, Items.GOLD_INGOT, 5, Items.DIAMOND, 1));
        }

        return nodes;
    }

    // 2. 서버 패킷 검증용: 특정 신상의 특정 노드 정보 하나만 반환
    public static NodeInfo getNode(String statueId, int nodeId) {
        for (NodeInfo node : getNodesForStatue(statueId)) {
            if (node.id == nodeId) {
                return node;
            }
        }
        return null; // 해당 노드가 없으면 null 반환
    }
}