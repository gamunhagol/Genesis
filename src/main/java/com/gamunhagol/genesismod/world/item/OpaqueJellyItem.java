package com.gamunhagol.genesismod.world.item;

import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.util.LevelCalcHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class OpaqueJellyItem extends Item {
    public OpaqueJellyItem(Properties properties) {
        // 음식 설정: 허기 1, 포만감 0.1 (스탯용이므로 낮게 설정), 항상 먹을 수 있게 설정
        super(properties.food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).alwaysEat().build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                int currentLevel = LevelCalcHelper.getCharacterLevel(stats);

                if (currentLevel > 1) {
                    // 1. 환급할 XP 계산
                    int refundXp = LevelCalcHelper.calculateTotalXpSpent(currentLevel);

                    // 2. 스탯 초기화 (기존 StatCapability 기본값 적용)
                    stats.setVigor(10);
                    stats.setMind(10);
                    stats.setEndurance(10);
                    stats.setStrength(10);
                    stats.setDexterity(10);
                    stats.setIntelligence(10);
                    stats.setFaith(10);
                    stats.setArcane(9);
                    stats.updateMaxMental();

                    // 3. 어트리뷰트(체력/스태미나 등) 재적용
                    StatApplier.applyAll(player, stats);

                    // 4. 경험치 돌려주기
                    player.giveExperiencePoints(refundXp);

                    // 5. 클라이언트 동기화
                    GenesisNetwork.sendToPlayer(new PacketSyncStats(
                            stats.getVigor(), stats.getMind(), stats.getEndurance(),
                            stats.getStrength(), stats.getDexterity(), stats.getIntelligence(),
                            stats.getFaith(), stats.getArcane(),
                            stats.getMental(), stats.getMaxMental(),
                            stats.isLevelUpUnlocked()
                    ), player);

                    player.displayClientMessage(Component.translatable("message.genesis.reset_success").withStyle(ChatFormatting.AQUA), true);
                }
            });
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT; // 젤리니까 먹는 모션
    }
}
