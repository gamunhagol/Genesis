package com.gamunhagol.genesismod.network;

import com.gamunhagol.genesismod.content.magic.AbstractSpell;
import com.gamunhagol.genesismod.content.magic.GenesisSpells;
import com.gamunhagol.genesismod.world.capability.spell.SpellSlotProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketChangeSpell {
    private final int slotIndex;
    private final String spellId;

    public PacketChangeSpell(int slotIndex, String spellId) {
        this.slotIndex = slotIndex;
        this.spellId = spellId;
    }

    public static void encode(PacketChangeSpell msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.slotIndex);
        buf.writeUtf(msg.spellId);
    }

    public static PacketChangeSpell decode(FriendlyByteBuf buf) {
        return new PacketChangeSpell(buf.readInt(), buf.readUtf());
    }

    public static void handle(PacketChangeSpell msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(SpellSlotProvider.SPELL_SLOT).ifPresent(cap -> {

                    if (msg.spellId == null || msg.spellId.isEmpty()) {
                        cap.setSelectedSlot(msg.slotIndex);
                        cap.equipSpell("");
                    }
                    else {
                        AbstractSpell targetSpell = GenesisSpells.get(msg.spellId);
                        if (targetSpell != null) {
                            int newSpellCost = targetSpell.getMemoryCost();
                            int currentTotalCost = 0;

                            List<String> equipped = cap.getEquippedSpells();
                            for (int i = 0; i < equipped.size(); i++) {
                                if (i == msg.slotIndex) continue;

                                String spellId = equipped.get(i);
                                if (spellId != null && !spellId.isEmpty()) {
                                    AbstractSpell s = GenesisSpells.get(spellId);
                                    if (s != null) {
                                        currentTotalCost += s.getMemoryCost();
                                    }
                                }
                            }

                            if (currentTotalCost + newSpellCost <= cap.getMemoryCapacity()) {
                                cap.setSelectedSlot(msg.slotIndex);
                                cap.equipSpell(msg.spellId);
                            } else {
                                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                        SoundEvents.FIRE_EXTINGUISH,
                                        SoundSource.PLAYERS, 1.0F, 1.0F);
                            }
                        }
                    }

                    GenesisNetwork.sendToPlayer(
                            new PacketSyncSpellSlot(cap.getMemoryCapacity(), cap.getSelectedSlot(), cap.getEquippedSpells()),
                            player
                    );
                });
            }
        });
        context.setPacketHandled(true);
    }
}