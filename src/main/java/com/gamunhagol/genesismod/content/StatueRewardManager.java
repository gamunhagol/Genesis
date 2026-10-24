package com.gamunhagol.genesismod.content;

import com.gamunhagol.genesismod.world.item.GenesisItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final Map<String, List<NodeInfo>> CACHE = new ConcurrentHashMap<>();

    public static List<NodeInfo> getNodesForStatue(String statueId) {
        return CACHE.computeIfAbsent(statueId, StatueRewardManager::buildNodes);
    }

    private static List<NodeInfo> buildNodes(String statueId) {
        List<NodeInfo> nodes = new ArrayList<>();
        Item cost = GenesisItems.FABRICATED_STAR.get();
        Item sky_cost = GenesisItems.CELESTIAL_STAR.get();
        Item first_cost = GenesisItems.EPONYMOUS_STAR.get();

        if ("god_a".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(GenesisItems.CONJURE_STONE.get(), 1),1));
            nodes.add(new NodeInfo(3, 800, 1100, cost, 1, new ItemStack(GenesisItems.BURIAL.get(), 1),2));
            nodes.add(new NodeInfo(4, 800, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),3));
            nodes.add(new NodeInfo(5, 800, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),4));
            nodes.add(new NodeInfo(6, 800, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(7, 900, 1000, cost, 1, new ItemStack(GenesisItems.CONJURE_STONE_WALL.get(), 1),2));
            nodes.add(new NodeInfo(8, 900, 900, cost, 1, new ItemStack(GenesisItems.ROCK_SPHERE.get(), 1),7));
            nodes.add(new NodeInfo(9, 1100, 1100, cost, 1, new ItemStack(GenesisItems.THROW_STONE.get(), 1),1));
            nodes.add(new NodeInfo(10, 1200, 1100, cost, 1, ItemStack.EMPTY, 9));
            nodes.add(new NodeInfo(11, 1200, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(12, 1100, 1000, cost, 1, new ItemStack(GenesisItems.PULL_ROCK.get(), 1),9));
            nodes.add(new NodeInfo(13, 1100, 900, cost, 1, new ItemStack(GenesisItems.EARTH_EXTRACT.get(), 1),12));
            nodes.add(new NodeInfo(14, 1100, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
            nodes.add(new NodeInfo(15, 1100, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),14));
            nodes.add(new NodeInfo(16, 1000, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),1));
            nodes.add(new NodeInfo(17, 1000, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(18, 1000, 800, cost, 1, new ItemStack(GenesisItems.PATAMU_ROAR.get(), 1),17));
            nodes.add(new NodeInfo(19, 1000, 700, cost, 1, new ItemStack(GenesisItems.PATAMU_WRATH.get(), 1),18));
            nodes.add(new NodeInfo(20, 1000, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),20));


        }
        else if ("god_b".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1000, cost, 1, new ItemStack(GenesisItems.TINDER.get(),1), 1));
            nodes.add(new NodeInfo(3, 1100, 1000, cost, 1, new ItemStack(GenesisItems.BIO_SMELTER.get(), 1),1));
            nodes.add(new NodeInfo(4, 1000, 900, cost, 1, new ItemStack(GenesisItems.EMBER.get(), 1),2));
            nodes.add(new NodeInfo(5, 800, 900, cost, 1, ItemStack.EMPTY,2));
            nodes.add(new NodeInfo(6, 1200, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),3));
            nodes.add(new NodeInfo(7, 900, 800, cost, 1, new ItemStack(GenesisItems.FIRE_GAZE.get(), 1),5));
            nodes.add(new NodeInfo(8, 800, 700, cost, 1, new ItemStack(GenesisItems.REALM_OF_FIRE.get(), 1),7));
            nodes.add(new NodeInfo(9, 900, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),8));
            nodes.add(new NodeInfo(10, 1300, 800, cost, 1, new ItemStack(GenesisItems.FLAME_PROTECTION.get(), 1),6));
            nodes.add(new NodeInfo(11, 1200, 700, cost, 1, new ItemStack(GenesisItems.FIRE_GOD_BLESSING.get(), 1),10));
            nodes.add(new NodeInfo(12, 1400, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(13, 1300, 600, cost, 1, new ItemStack(GenesisItems.FLAME_HAMMER.get(), 1),12));
            nodes.add(new NodeInfo(14, 1400, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
            nodes.add(new NodeInfo(15, 1300, 400, cost, 1, new ItemStack(GenesisItems.KAELO_CREATION.get(), 1),14));
            nodes.add(new NodeInfo(16, 1100, 800, cost, 1, new ItemStack(GenesisItems.FLAME_GRASP.get(), 1),4));
            nodes.add(new NodeInfo(17, 1000, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(18, 1100, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),17));
            nodes.add(new NodeInfo(19, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 1100, 400, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 1000, 300, cost, 1, new ItemStack(Items.DIAMOND, 1),20));
            nodes.add(new NodeInfo(22, 1100, 200, cost, 1, new ItemStack(Items.DIAMOND, 1),21));
        }
        else if ("god_c".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(GenesisItems.FLOOD.get(), 1),1));
            nodes.add(new NodeInfo(3, 800, 1100, cost, 1, new ItemStack(Items.DIAMOND, 1),2));
            nodes.add(new NodeInfo(4, 1100, 1100, cost, 1, new ItemStack(GenesisItems.WATER_SPRAY.get(), 1),1));
            nodes.add(new NodeInfo(5, 1000, 1000, cost, 1, new ItemStack(GenesisItems.SEA_CURSE.get(), 1),2));
            nodes.add(new NodeInfo(6, 1000, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(7, 900, 800, cost, 1, new ItemStack(GenesisItems.WATERSPOUT.get(), 1),6));
            nodes.add(new NodeInfo(8, 800, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(9, 1100, 1000, cost, 1, new ItemStack(GenesisItems.SEA_LUCK.get(), 1),1));
            nodes.add(new NodeInfo(10, 1100, 900, cost, 1, new ItemStack(GenesisItems.SEA_SERPENT_FIN.get() , 1),9));
            nodes.add(new NodeInfo(11, 1100, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(12, 1000, 700, cost, 1, new ItemStack(GenesisItems.SEA_SERPENT_SCALE.get(), 1),11));
            nodes.add(new NodeInfo(13, 900, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),12));
            nodes.add(new NodeInfo(14, 1200, 1000, cost, 1, new ItemStack(GenesisItems.SEA_SERPENT_BREATH.get(), 1),4));
            nodes.add(new NodeInfo(15, 1200, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),14));
            nodes.add(new NodeInfo(16, 1200, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(17, 1200, 700, cost, 1, new ItemStack(GenesisItems.AGFLO_JAWS.get(), 1),16));
            nodes.add(new NodeInfo(18, 1100, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),17));
            nodes.add(new NodeInfo(19, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 900, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 800, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
        }
        else if ("god_d".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 1030, 1000, cost, 1, new ItemStack(GenesisItems.HOME_MEAL.get(), 1),1));
            nodes.add(new NodeInfo(3, 930, 1000, cost, 1, new ItemStack(GenesisItems.CLEAR_WEATHER.get(), 1),2));
            nodes.add(new NodeInfo(4, 960, 900, cost, 1, new ItemStack(GenesisItems.REJECTION_STORM.get(), 1),3));
            nodes.add(new NodeInfo(5, 990, 800, cost, 1, new ItemStack(GenesisItems.REFRESHING_BREEZE.get(), 1),4));
            nodes.add(new NodeInfo(6, 1100, 900, cost, 1, new ItemStack(GenesisItems.LIEN_PROTECTION.get(), 1),4));
            nodes.add(new NodeInfo(7, 1100, 800, cost, 1, new ItemStack(GenesisItems.WIND_FEATHER.get(), 1),5));
            nodes.add(new NodeInfo(8, 1200, 800, cost, 1, new ItemStack(GenesisItems.UPDRAFT.get(), 1),7));
            nodes.add(new NodeInfo(9, 1130, 700, cost, 1, new ItemStack(GenesisItems.BLADE_STORM.get(), 1),7));
            nodes.add(new NodeInfo(10, 1230, 700, cost, 1, new ItemStack(GenesisItems.STORM.get(), 1),8));
            nodes.add(new NodeInfo(11, 1030, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(12, 930, 700, cost, 1, new ItemStack(GenesisItems.STORM_PROTECTION.get(), 1),11));
            nodes.add(new NodeInfo(13, 1060, 600, cost, 1, new ItemStack(GenesisItems.GALE.get(), 1),11));
            nodes.add(new NodeInfo(14, 960, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),12));
            nodes.add(new NodeInfo(15, 1160, 600, cost, 1, new ItemStack(GenesisItems.WIND_ANIMUS.get(), 1),13));
            nodes.add(new NodeInfo(16, 1260, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(17, 1190, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(18, 1290, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(19, 1390, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(20, 1390, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(21, 1490, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),20));

        }
        else if ("god_e".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 1100, 1000, cost, 1, new ItemStack(GenesisItems.PARALYSIS.get(), 1),1));
            nodes.add(new NodeInfo(3, 1000, 1000, cost, 1, new ItemStack(Items.DIAMOND, 1),2));
            nodes.add(new NodeInfo(4, 1100, 900, cost, 1, new ItemStack(GenesisItems.LIGHTNING_PROTECTION.get(), 1),3));
            nodes.add(new NodeInfo(5, 1200, 800, cost, 1, new ItemStack(GenesisItems.CALL_STORM.get(), 1),4));
            nodes.add(new NodeInfo(6, 1300, 800, cost, 1, new ItemStack(GenesisItems.HUNTERS_INSTINCT.get(), 1),5));
            nodes.add(new NodeInfo(7, 1200, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),6));
            nodes.add(new NodeInfo(8, 1300, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(9, 1200, 1000, cost, 1, new ItemStack(GenesisItems.BATTLE_STIMULATION.get(), 1),8));
            nodes.add(new NodeInfo(10, 1400, 800, cost, 1, new ItemStack(GenesisItems.LIGHTNING_STRIKE.get(), 1),6));
            nodes.add(new NodeInfo(11, 1500, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(12, 1600, 600, cost, 1, new ItemStack(GenesisItems.LIGHTNING_ARROW.get(), 1),11));
            nodes.add(new NodeInfo(13, 1600, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),11));
            nodes.add(new NodeInfo(14, 1500, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
            nodes.add(new NodeInfo(15, 1500, 600, cost, 1, new ItemStack(GenesisItems.LIGHTNING_SPEAR.get(), 1),12));
            nodes.add(new NodeInfo(16, 1500, 600, cost, 1, new ItemStack(GenesisItems.LIGHTNING_STAKE.get(), 1),15));
            nodes.add(new NodeInfo(17, 1400, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(18, 1500, 500, cost, 1, new ItemStack(GenesisItems.VALA_ARROW.get(), 1),17));
            nodes.add(new NodeInfo(19, 1600, 400, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 1400, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(21, 1300, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),20));

        }
        else if ("god_f".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 900, 1100, cost, 1, new ItemStack(GenesisItems.BITTER_MEDICINE.get(), 1),1));
            nodes.add(new NodeInfo(3, 1100, 1100, cost, 1, new ItemStack(GenesisItems.POISON_IS_BITTER.get(), 1),1));
            nodes.add(new NodeInfo(4, 1000, 1000, cost, 1, new ItemStack(GenesisItems.PLANT_BODY.get(), 1),1));
            nodes.add(new NodeInfo(5, 1000, 900, cost, 1, new ItemStack(GenesisItems.ROOT_ENTANGLE.get(), 1),4));
            nodes.add(new NodeInfo(6, 1050, 900, cost, 1, new ItemStack(GenesisItems.POISON_ROOT.get(), 1),5));
            nodes.add(new NodeInfo(7, 1000, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(8, 900, 800, cost, 1, new ItemStack(GenesisItems.ROOT_SPEAR.get(), 1),7));
            nodes.add(new NodeInfo(9, 900, 700, cost, 1, new ItemStack(GenesisItems.ROOT_STRIKE.get(), 1),8));
            nodes.add(new NodeInfo(10, 800, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(11, 800, 650, cost, 1, new ItemStack(Items.DIAMOND, 1),10));
            nodes.add(new NodeInfo(12, 800, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),11));
            nodes.add(new NodeInfo(13, 850, 650, cost, 1, new ItemStack(Items.DIAMOND, 1),11));
            nodes.add(new NodeInfo(14, 850, 550, cost, 1, new ItemStack(Items.DIAMOND, 1),13));
            nodes.add(new NodeInfo(15, 1000, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(16, 1100, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(17, 1000, 600, cost, 1, new ItemStack(GenesisItems.ENTANGLING_FOREST.get(), 1),15));
            nodes.add(new NodeInfo(18, 1100, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),16));
            nodes.add(new NodeInfo(19, 1100, 550, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 1150, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(21, 1150, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),20));
        }
        else if ("god_g".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, ItemStack.EMPTY));
            nodes.add(new NodeInfo(2, 1000, 1000, cost, 1, new ItemStack(GenesisItems.ICE_SHIELD.get(), 1),1));
            nodes.add(new NodeInfo(3, 900, 1000, cost, 1, new ItemStack(GenesisItems.ICE_WALL.get(), 1),1));
            nodes.add(new NodeInfo(4, 800, 900, cost, 1, new ItemStack(GenesisItems.FROST_FIELD.get(), 1),3));
            nodes.add(new NodeInfo(5, 700, 800, cost, 1, new ItemStack(GenesisItems.STAND_ON_ICE.get(), 1),4));
            nodes.add(new NodeInfo(6, 600, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(7, 700, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),6));
            nodes.add(new NodeInfo(8, 800, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(9, 900, 400, cost, 1, new ItemStack(GenesisItems.ATONEMENT_FOR_SIN.get(), 1),8));
            nodes.add(new NodeInfo(10, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(11, 900, 900, cost, 1, new ItemStack(GenesisItems.ICE_BLADE.get(), 1),2));
            nodes.add(new NodeInfo(12, 800, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),11));
            nodes.add(new NodeInfo(13, 700, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),12));
            nodes.add(new NodeInfo(14, 800, 600, cost, 1, new ItemStack(GenesisItems.FROZEN_STAR.get(), 1),13));
            nodes.add(new NodeInfo(15, 900, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),14));
            nodes.add(new NodeInfo(16, 1000, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(17, 900, 800, cost, 1, new ItemStack(GenesisItems.ICE_SPEAR.get(), 1),11));
            nodes.add(new NodeInfo(18, 800, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),17));
            nodes.add(new NodeInfo(19, 900, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 900, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 1000, 700, cost, 1, new ItemStack(GenesisItems.FROST_ARMOR.get(), 1),20));
        }
        else if ("god_h".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, cost, 1, new ItemStack(GenesisItems.COLD_HEAL.get(), 1)));
            nodes.add(new NodeInfo(2, 1000, 1000, cost, 1, new ItemStack(GenesisItems.COLD_PROTECTION.get(), 1),1));
            nodes.add(new NodeInfo(3, 1100, 1000, cost, 1, new ItemStack(GenesisItems.FREEZING_WALKER.get(), 1),1));
            nodes.add(new NodeInfo(4, 1200, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),3));
            nodes.add(new NodeInfo(5, 1300, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),4));
            nodes.add(new NodeInfo(6, 1400, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),5));
            nodes.add(new NodeInfo(7, 1300, 600, cost, 1, new ItemStack(GenesisItems.BLIZZARD.get(), 1),6));
            nodes.add(new NodeInfo(8, 1200, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),7));
            nodes.add(new NodeInfo(9, 1100, 400, cost, 1, new ItemStack(Items.DIAMOND, 1),8));
            nodes.add(new NodeInfo(10, 1000, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),9));
            nodes.add(new NodeInfo(11, 1100, 900, cost, 1, new ItemStack(Items.DIAMOND, 1),2));
            nodes.add(new NodeInfo(12, 1200, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),11));
            nodes.add(new NodeInfo(13, 1300, 700, cost, 1, new ItemStack(GenesisItems.COLD_TOUCH.get(), 1),12));
            nodes.add(new NodeInfo(14, 1200, 600, cost, 1, new ItemStack(GenesisItems.FROST_DOMAIN.get(), 1),13));
            nodes.add(new NodeInfo(15, 1100, 500, cost, 1, new ItemStack(Items.DIAMOND, 1),14));
            nodes.add(new NodeInfo(16, 1000, 600, cost, 1, new ItemStack(GenesisItems.STOP_THE_TEARS.get(), 1),15));
            nodes.add(new NodeInfo(17, 1100, 600, cost, 1, new ItemStack(Items.DIAMOND, 1),15));
            nodes.add(new NodeInfo(18, 1200, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),17));
            nodes.add(new NodeInfo(19, 1100, 800, cost, 1, new ItemStack(Items.DIAMOND, 1),18));
            nodes.add(new NodeInfo(20, 1100, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),19));
            nodes.add(new NodeInfo(21, 1000, 700, cost, 1, new ItemStack(Items.DIAMOND, 1),20));
        }

        if ("god_q".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_r".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_s".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_t".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_u".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_v".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, sky_cost, 1, ItemStack.EMPTY));
        }
        if ("god_w".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, first_cost, 1, ItemStack.EMPTY));
        }
        if ("god_x".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, first_cost, 1, ItemStack.EMPTY));
        }
        if ("god_y".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, first_cost, 1, ItemStack.EMPTY));
        }
        if ("god_z".equals(statueId)) {
            nodes.add(new NodeInfo(1, 1000, 1100, first_cost, 1, ItemStack.EMPTY));
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
