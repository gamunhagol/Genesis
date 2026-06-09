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

                if (player.getPersistentData().contains("GenesisStatueHealReadyTick")) {
                    long readyTick = player.getPersistentData().getLong("GenesisStatueHealReadyTick");
                    if (currentTime < readyTick) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.0F);
                        return;
                    }
                }

                player.setHealth(player.getMaxHealth());

                player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
                    stats.setMental(stats.getMaxMental());
                    stats.setDirty(true);
                });
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.5F, 1.0F);

                player.getPersistentData().putLong("GenesisStatueHealReadyTick", currentTime + 9600);
            }
        });
        context.setPacketHandled(true);
    }
}