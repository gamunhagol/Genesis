package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.network.client.ClientPayloadHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncMentalPower {
    private final float mentalPower;
    public PacketSyncMentalPower(float mentalPower) {
        this.mentalPower = mentalPower;
    }
    public PacketSyncMentalPower(FriendlyByteBuf buf) {
        this.mentalPower = buf.readFloat();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(this.mentalPower);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPayloadHandler.handleMentalSync(this.mentalPower);
            });
        });
        return true;
    }
}