package com.gamunhagol.genesismod.util;

import com.gamunhagol.genesismod.stats.StatCapability;
import net.minecraft.world.entity.player.Player;

public class LevelCalcHelper {
    // 기본 스탯 합 79 기준 레벨 계산
    public static int getCharacterLevel(StatCapability stats) {
        int totalStats = stats.getVigor() + stats.getMind() + stats.getEndurance()
                + stats.getStrength() + stats.getDexterity()
                + stats.getIntelligence() + stats.getFaith() + stats.getArcane();
        return (totalStats - 79) + 1;
    }

    // 다음 레벨업에 필요한 우리 모드 전용 XP 비용 (수치 살짝 조정: 80 -> 50) [cite: 2026-02-16]
    public static int getXpCostForNextLevel(int currentLevel) {
        if (currentLevel >= 713) return 0;
        return (int) (Math.pow(currentLevel - 1, 1.5) * 10 + 50);
    }

    // 플레이어의 현재 총 '바닐라 XP 포인트' 합계 계산 [cite: 2026-02-16]
    public static int getPlayerTotalXp(Player player) {
        int level = player.experienceLevel;
        int total = 0;
        for (int i = 0; i < level; i++) {
            total += getXpNeededForLevel(i);
        }
        total += (int) (player.experienceProgress * getXpNeededForLevel(level));
        return total;
    }

    // 바닐라 레벨당 요구 경험치량 (마인크래프트 공식) [cite: 2026-02-16]
    private static int getXpNeededForLevel(int level) {
        if (level >= 30) return 112 + (level - 30) * 9;
        if (level >= 15) return 37 + (level - 15) * 5;
        return 7 + level * 2;
    }
}