package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.network.client.ClientPayloadHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketSyncSpellSlot {
    private final int maxSlots;
    private final int selectedSlot;
    private final List<String> equippedSpells;

    public PacketSyncSpellSlot(int maxSlots, int selectedSlot, List<String> equippedSpells) {
        this.maxSlots = maxSlots;
        this.selectedSlot = selectedSlot;
        this.equippedSpells = equippedSpells;
    }

    public static void encode(PacketSyncSpellSlot msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.maxSlots);
        buf.writeInt(msg.selectedSlot);
        buf.writeCollection(msg.equippedSpells, FriendlyByteBuf::writeUtf);
    }

    public static PacketSyncSpellSlot decode(FriendlyByteBuf buf) {
        int maxSlots = buf.readInt();
        int selectedSlot = buf.readInt();
        List<String> equippedSpells = buf.readCollection(ArrayList::new, FriendlyByteBuf::readUtf);
        return new PacketSyncSpellSlot(maxSlots, selectedSlot, equippedSpells);
    }

    public static void handle(PacketSyncSpellSlot msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPayloadHandler.handleSpellSlotSync(msg.maxSlots, msg.selectedSlot, msg.equippedSpells));
        context.setPacketHandled(true);
    }
}