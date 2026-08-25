package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActivateWindBlessing {

    public PacketActivateWindBlessing() {}

    public PacketActivateWindBlessing(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(cap -> {
                if (cap.isNodeUnlocked("god_d", 1) && cap.getWindDashCooldown() <= 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 1, false, false, true));
                    cap.setWindDashCooldown(6000);
                }
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
