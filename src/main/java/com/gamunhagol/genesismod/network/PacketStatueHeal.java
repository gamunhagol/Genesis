package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStatueHeal {
    public PacketStatueHeal() {
    }

    public static void encode(PacketStatueHeal msg, FriendlyByteBuf buf) {
    }

    public static PacketStatueHeal decode(FriendlyByteBuf buf) {
        return new PacketStatueHeal();
    }

    public static void handle(PacketStatueHeal msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                long currentTime = player.level().getGameTime();

                // PersistentData에서 쿨타임 체크 (8분 = 9600틱)
                if (player.getPersistentData().contains("GenesisStatueHealReadyTick")) {
                    long readyTick = player.getPersistentData().getLong("GenesisStatueHealReadyTick");
                    if (currentTime < readyTick) {
                        // 아직 쿨타임 중이라면 불 꺼지는 소리(치익-)만 재생하고 연산 종료
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.0F);
                        return;
                    }
                }

                // [회복 효과 실행] 체력과 정신력(마나)을 모두 만땅으로 채워줍니다.
                player.setHealth(player.getMaxHealth());

                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    stats.setMental(stats.getMaxMental());
                    stats.setDirty(true);
                });

                // 성공 사운드 재생 (토템 사용 소리)
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 1.0F);

                // 다음 이용 가능 시간 설정 (현재 시간 + 8분)
                player.getPersistentData().putLong("GenesisStatueHealReadyTick", currentTime + 9600);
            }
        });
        context.setPacketHandled(true);
    }
}