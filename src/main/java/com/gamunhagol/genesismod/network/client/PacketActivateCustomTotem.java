package com.gamunhagol.genesismod.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketActivateCustomTotem {
    private final int entityId;
    private final ItemStack totemStack;

    public PacketActivateCustomTotem(int entityId, ItemStack totemStack) {
        this.entityId = entityId;
        this.totemStack = totemStack;
    }

    public PacketActivateCustomTotem(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.totemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeItem(this.totemStack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPayloadHandler.handleTotemActivation(this.entityId, this.totemStack);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
