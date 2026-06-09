package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.stats.StatCapability;
import net.minecraft.world.entity.player.Player;

public class LevelCalcHelper {
    public static int getCharacterLevel(StatCapability stats) {
        int totalStats = stats.getVigor() + stats.getMind() + stats.getEndurance()
                + stats.getStrength() + stats.getDexterity()
                + stats.getIntelligence() + stats.getFaith() + stats.getArcane();
        return (totalStats - 79) + 1;
    }

    public static int getXpCostForNextLevel(int currentLevel) {
        if (currentLevel >= 713) return 0;
        return (int) (Math.pow(currentLevel - 1, 2.0) * 12 + 80);
    }

    public static int getPlayerTotalXp(Player player) {
        int level = player.experienceLevel;
        int total = 0;
        for (int i = 0; i < level; i++) {
            total += getXpNeededForLevel(i);
        }
        total += (int) (player.experienceProgress * getXpNeededForLevel(level));
        return total;
    }

    private static int getXpNeededForLevel(int level) {
        if (level >= 30) return 112 + (level - 30) * 9;
        if (level >= 15) return 37 + (level - 15) * 5;
        return 7 + level * 2;
    }

    public static int calculateTotalXpSpent(int currentLevel) {
        int totalCost = 0;
        for (int i = 1; i < currentLevel; i++) {
            totalCost += getXpCostForNextLevel(i);
        }
        return totalCost;
    }
}