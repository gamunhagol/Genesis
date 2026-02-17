package com.gamunhagol.genesismod.network.client;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientPayloadHandler {
    // 패킷으로부터 받은 데이터를 클라이언트 플레이어의 스탯에 덮어씌웁니다.
    public static void handleMentalSync(float mentalPower) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                stats.setMental(mentalPower);
            });
        }
    }
}