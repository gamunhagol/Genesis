package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.main.GenesisMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class GenesisNetwork {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        // 1. 채널 생성 및 INSTANCE 초기화 (이 부분이 빠져있었습니다)
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(GenesisMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // 2. 사용할 패킷들 등록 (클래스 이름과 순서를 확인하세요)
        net.messageBuilder(PacketSyncStats.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncStats::new)
                .encoder(PacketSyncStats::toBytes)
                .consumerMainThread(PacketSyncStats::handle)
                .add();

        net.messageBuilder(PacketConfirmLevelUp.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketConfirmLevelUp::new)
                .encoder(PacketConfirmLevelUp::toBytes)
                .consumerMainThread(PacketConfirmLevelUp::handle)
                .add();
    }

    // 서버에서 특정 플레이어에게 패킷을 보내는 메서드
    public static void sendToPlayer(Object message, ServerPlayer player) {
        // INSTANCE가 null인지 체크하여 튕김 방지
        if (INSTANCE != null && player != null) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
        }
    }

    // 클라이언트에서 서버로 패킷을 보내는 메서드
    public static void sendToServer(Object message) {
        if (INSTANCE != null) {
            INSTANCE.sendToServer(message);
        }
    }
}