package com.gamunhagol.genesismod.network.client;

import com.gamunhagol.genesismod.world.border.SpatialRuptureManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncSpatialRupture {
    private final ResourceLocation dimension;
    private final double x, z;
    private final double radius;
    private final long expireGameTime;

    public PacketSyncSpatialRupture(ResourceLocation dimension, double x, double z, double radius, long expireGameTime) {
        this.dimension = dimension;
        this.x = x;
        this.z = z;
        this.radius = radius;
        this.expireGameTime = expireGameTime;
    }

    public PacketSyncSpatialRupture(FriendlyByteBuf buf) {
        this.dimension = buf.readResourceLocation();
        this.x = buf.readDouble();
        this.z = buf.readDouble();
        this.radius = buf.readDouble();
        this.expireGameTime = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.dimension);
        buf.writeDouble(this.x);
        buf.writeDouble(this.z);
        buf.writeDouble(this.radius);
        buf.writeLong(this.expireGameTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SpatialRuptureManager.addZone(this.dimension, this.x, this.z, this.radius, this.expireGameTime);
        });
        context.setPacketHandled(true);
        return true;
    }
}