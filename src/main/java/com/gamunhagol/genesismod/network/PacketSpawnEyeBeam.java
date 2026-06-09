package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.events.GenesisClientRenderEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSpawnEyeBeam {
    private final Vec3 targetPos;

    public PacketSpawnEyeBeam(Vec3 targetPos) {
        this.targetPos = targetPos;
    }

    public PacketSpawnEyeBeam(FriendlyByteBuf buf) {
        this.targetPos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(targetPos.x);
        buf.writeDouble(targetPos.y);
        buf.writeDouble(targetPos.z);
    }


    public static void handle(PacketSpawnEyeBeam msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            GenesisClientRenderEvents.spawnBeam(msg.targetPos);
        });
        ctx.setPacketHandled(true);
    }
}
