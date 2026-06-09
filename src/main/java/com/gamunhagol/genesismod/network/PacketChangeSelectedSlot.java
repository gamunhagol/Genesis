package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketChangeSelectedSlot {
    private final int direction;

    public PacketChangeSelectedSlot(int direction) {
        this.direction = direction;
    }

    public static void encode(PacketChangeSelectedSlot msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.direction);
    }

    public static PacketChangeSelectedSlot decode(FriendlyByteBuf buf) {
        return new PacketChangeSelectedSlot(buf.readInt());
    }

    public static void handle(PacketChangeSelectedSlot msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {
                    int currentSlot = cap.getSelectedSlot();

                    int UI_SLOTS = 10;
                    List<String> equipped = cap.getEquippedSpells();

                    boolean hasAnySpell = false;
                    for (int i = 0; i < UI_SLOTS; i++) {
                        String s = equipped.get(i);
                        if (s != null && !s.trim().isEmpty()) {
                            hasAnySpell = true;
                            break;
                        }
                    }

                    if (hasAnySpell) {
                        int newSlot = currentSlot;

                        for (int i = 0; i < UI_SLOTS; i++) {
                            newSlot += msg.direction;

                            if (newSlot < 0) {
                                newSlot = UI_SLOTS - 1;
                            } else if (newSlot >= UI_SLOTS) {
                                newSlot = 0;
                            }

                            String spellInSlot = equipped.get(newSlot);
                            if (spellInSlot != null && !spellInSlot.trim().isEmpty()) {
                                break;
                            }
                        }

                        cap.setSelectedSlot(newSlot);
                        GenesisNetwork.sendToPlayer(
                                new PacketSyncSpellSlot(cap.getMemoryCapacity(), cap.getSelectedSlot(), cap.getEquippedSpells()),
                                player
                        );
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}