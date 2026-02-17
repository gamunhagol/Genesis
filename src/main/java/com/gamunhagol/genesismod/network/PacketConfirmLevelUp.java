package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.util.LevelCalcHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketConfirmLevelUp {
    private final int[] increases;

    public PacketConfirmLevelUp(int[] increases) { this.increases = increases; }
    public PacketConfirmLevelUp(FriendlyByteBuf buf) { this.increases = buf.readVarIntArray(); }
    public void toBytes(FriendlyByteBuf buf) { buf.writeVarIntArray(this.increases); }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                int baseLevel = LevelCalcHelper.getCharacterLevel(stats);
                int totalCost = 0;
                int totalInc = 0;
                for (int i : increases) totalInc += i;

                // 필요한 총 XP 계산
                for (int i = 0; i < totalInc; i++) {
                    totalCost += LevelCalcHelper.getXpCostForNextLevel(baseLevel + i);
                }

                //  플레이어의 현재 총 경험치 가져오기
                int playerTotalXp = LevelCalcHelper.getPlayerTotalXp(player);

                //  비용을 지불할 수 있는지 확인 (현재 경험치 >= 필요 비용)
                if (playerTotalXp >= totalCost) {

                    //  ★ 실제 XP 차감 (음수를 넣으면 차감됩니다) ★
                    player.giveExperiencePoints(-totalCost);

                    //  스탯 적용
                    stats.setVigor(stats.getVigor() + increases[0]);
                    stats.setMind(stats.getMind() + increases[1]);
                    stats.setEndurance(stats.getEndurance() + increases[2]);
                    stats.setStrength(stats.getStrength() + increases[3]);
                    stats.setDexterity(stats.getDexterity() + increases[4]);
                    stats.setIntelligence(stats.getIntelligence() + increases[5]);
                    stats.setFaith(stats.getFaith() + increases[6]);
                    stats.setArcane(stats.getArcane() + increases[7]);


                    //  속성 재계산 및 동기화
                    StatApplier.applyAll(player, stats);

                    // 데이터 변경 알림 (동기화 패킷 자동 발송 유도)
                    stats.setDirty(true);
                }
            });
        });
        return true;
    }
}