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

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(GenesisMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        // ==========================================
        // [S -> C] Server to Client Packets (동기화 등)
        // ==========================================
        INSTANCE.messageBuilder(PacketSyncStats.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncStats::new).encoder(PacketSyncStats::toBytes)
                .consumerMainThread(PacketSyncStats::handle).add();

        INSTANCE.messageBuilder(PacketSyncMentalPower.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncMentalPower::new).encoder(PacketSyncMentalPower::toBytes)
                .consumerMainThread(PacketSyncMentalPower::handle).add();

        INSTANCE.messageBuilder(PacketSyncSpellSlot.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncSpellSlot::decode).encoder(PacketSyncSpellSlot::encode)
                .consumerMainThread(PacketSyncSpellSlot::handle).add();

        // ==========================================
        // [C -> S] Client to Server Packets (요청, 조작 등)
        // ==========================================
        INSTANCE.messageBuilder(PacketConfirmLevelUp.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketConfirmLevelUp::new).encoder(PacketConfirmLevelUp::toBytes)
                .consumerMainThread(PacketConfirmLevelUp::handle).add();

        INSTANCE.messageBuilder(PacketChangeSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketChangeSpell::decode).encoder(PacketChangeSpell::encode)
                .consumerMainThread(PacketChangeSpell::handle).add();

        INSTANCE.messageBuilder(PacketStatueHeal.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketStatueHeal::decode).encoder(PacketStatueHeal::encode)
                .consumerMainThread(PacketStatueHeal::handle).add();

        INSTANCE.messageBuilder(PacketChangeSelectedSlot.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketChangeSelectedSlot::decode).encoder(PacketChangeSelectedSlot::encode)
                .consumerMainThread(PacketChangeSelectedSlot::handle).add();

        INSTANCE.messageBuilder(PacketStatueUnlockNode.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketStatueUnlockNode::new).encoder(PacketStatueUnlockNode::toBytes)
                .consumerMainThread(PacketStatueUnlockNode::handle).add();
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        if (INSTANCE != null && player != null) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
        }
    }

    public static void sendToServer(Object message) {
        if (INSTANCE != null) {
            INSTANCE.sendToServer(message);
        }
    }
}