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
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(GenesisMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // 위에서 만든 패킷 등록
        net.messageBuilder(PacketSyncMentalPower.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncMentalPower::new)
                .encoder(PacketSyncMentalPower::toBytes)
                .consumerMainThread(PacketSyncMentalPower::handle)
                .add();
    }

    // 서버에서 특정 플레이어에게 패킷을 보내는 메서드
    public static void sendToPlayer(Object message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}